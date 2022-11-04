package com.ibm.sec.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.AlgosecObject;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.NetmaskUtil;
import com.ibm.sec.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolves network and service objects to get their complete list of ip addresses or protocol+port combo.
 */
@Slf4j
@Component
public class ObjectsResolver {

    @Autowired
    private Util util;
    @Autowired
    private ObjectsUtil objectsUtil;
    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    private NetmaskUtil netmaskUtil;

    public ObjectsData init(JsonNode firewallChangesNode, Map<String, Map<String, String>> deviceNameNetworkObjectDetailsMap, Map<String, Map<String, List<String>>> deviceNameServiceObjectDetailsMap) {
        JsonNode objectUpdateNode = firewallChangesNode.get(IConstant.OBJECT_UPDATE_CHANGES);
        ObjectsData objectsData = new ObjectsData();
        objectsData.setDeviceNameNetworkObjectDetailsMapAsInAlgosec(deviceNameNetworkObjectDetailsMap);
        objectsData.setDeviceNameServiceObjectDetailsMap(deviceNameServiceObjectDetailsMap);
        populateMap(objectUpdateNode, objectsData);
        return objectsData;
    }


    private void populateMap(JsonNode objectUpdateNodeArray, ObjectsData objectsData) {
    	if(objectUpdateNodeArray != null) {
	        for(int i = 0; i< objectUpdateNodeArray.size(); i++) {
	            JsonNode objectUpdateNode = objectUpdateNodeArray.get(i);
	                if(objectsUtil.shouldProcessObjectChange(objectUpdateNode)) {
	                String objectName = objectUpdateNode.get("object_name").asText();
	                if(!objectsData.getAlgosecObjectNameObjectMap().containsKey(objectName)) {
	                    objectsData.getAlgosecObjectNameObjectMap().put(objectName, new ArrayList<>());
	                }
	                List<String> values = Arrays.asList(objectUpdateNode.get("action_items").asText().split(","));
	                String algosecDeviceName = objectUpdateNode.get("firewall_policy").asText();
	                AlgosecObject object = new AlgosecObject();
	                object.setObjectName(objectName);
	                object.setValues(values);
	                object.setAlgosecDeviceName(algosecDeviceName);
	                objectsData.getAlgosecObjectNameObjectMap().get(objectName).add(object);
	            }
	        }
        }
    }

    public List<String> resolveIfObject(String potentialObject, String algosecDeviceName, IConstant.AlgosecObjectType algosecObjectType, ObjectsData objectsData) {
        List<String> values = new LinkedList<>();
        if(objectsData.getAlgosecObjectNameObjectMap().containsKey(potentialObject)) {
            //validate if this object is defined for the same device as the algosecDeviceName argument
            List<AlgosecObject> algosecObjectsMatchingGivenDeviceName = objectsData.getAlgosecObjectNameObjectMap().get(potentialObject).stream().filter(algosecObject -> algosecObject.getAlgosecDeviceName().equals(algosecDeviceName)).collect(Collectors.toList());
            if(algosecObjectsMatchingGivenDeviceName.isEmpty()) {
                throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getObjectNotFoundOnDeviceForRule(), potentialObject, algosecDeviceName));
            }
            if(algosecObjectsMatchingGivenDeviceName.size() > 1) {
                throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getDuplicateObjectNameForSameDevice(), potentialObject));
            }
            AlgosecObject algosecObject = algosecObjectsMatchingGivenDeviceName.get(0);
            if(algosecObject.getValues().size() > 1) {//if multiple action items then each item must be an object and not an ip address, hence resolve each of them
                algosecObject.getValues().forEach(value -> {
                    values.addAll(resolveIfObject(value, algosecDeviceName, algosecObjectType, objectsData));
                });
            } else {
                if(algosecObjectType == IConstant.AlgosecObjectType.NETWORK) {
                    String santizedIPAddress = netmaskUtil.santizeIPAddress(algosecObject.getValues().get(0));
                    if (util.isIpValid(santizedIPAddress)) {
                        values.add(santizedIPAddress);
                    } else {
                        throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getInvalidObjectDefinition(), potentialObject));
                    }
                } else if(algosecObjectType == IConstant.AlgosecObjectType.SERVICE) {
                    String[] serviceObjectValue = algosecObject.getValues().get(0).split("_|/"); //_ and / both are acceptable
                    if(serviceObjectValue.length == 2 && util.isProtocolNameValid(serviceObjectValue[0]) && util.isPortNumberValid(serviceObjectValue[1])) {
                        values.add(String.join("/", serviceObjectValue[0], serviceObjectValue[1]));
                    } else {
                        throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getInvalidObjectDefinition(), potentialObject));
                    }
                }
            }
        } else {
            if(algosecObjectType == IConstant.AlgosecObjectType.NETWORK) {
                List<String> resolvedObjectValues = resolveIfNetworkObjectExistsInAlgoSec(potentialObject, algosecDeviceName, objectsData);
                if(resolvedObjectValues.isEmpty()) {
                    throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getCouldNotResolveObject(), potentialObject));
                }
                values.addAll(resolvedObjectValues);
            } else if(algosecObjectType == IConstant.AlgosecObjectType.SERVICE) {
                List<String> resolvedObjectValues = resolveIfServiceObjectExistsInAlgoSec(potentialObject, algosecDeviceName, objectsData);
//                if(resolvedObjectValues.isEmpty()) {
//                    throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getCouldNotResolveObject(), potentialObject));
//                }
                if(!resolvedObjectValues.isEmpty()) { //service object check is exactly opposite to network object check
                    //for network objects first IP address check happens then object check happens, but for service objects for object
                    //check happens then protocol+port check happens. That's why not throwing exception here just returning.
                    values.addAll(resolvedObjectValues);
                }
            }
        }
        return values;
    }

    /**
     * Checks if the given network object exists in Algosec. If not, returns empty list. If the object exists then it recursively
     * checks, for each of the values, if any of those values, is an object itself. This continues till all objects and nested objects
     * are resolved and a final list of network IP addresses is returned.
     * @param potentialObject
     * @param algosecDeviceName
     * @return
     */
    private List<String> resolveIfNetworkObjectExistsInAlgoSec(String potentialObject, String algosecDeviceName, ObjectsData objectsData) {
        List<String> values = new LinkedList<>();
        if (objectsData.getDeviceNameNetworkObjectDetailsMapAsInAlgosec().containsKey(algosecDeviceName)
            && objectsData.getDeviceNameNetworkObjectDetailsMapAsInAlgosec().get(algosecDeviceName).containsKey(potentialObject)) {
            String ipAddressRange = objectsData.getDeviceNameNetworkObjectDetailsMapAsInAlgosec().get(algosecDeviceName).get(potentialObject);
            String[] ipAddressArray = ipAddressRange.split(",");
            for (String ipAddress : ipAddressArray) {
                if(util.isIpValid(ipAddress)) {
                    values.add(ipAddress);
                } else {
                    values.addAll(resolveIfNetworkObjectExistsInAlgoSec(ipAddress, algosecDeviceName, objectsData));
                }
            }
        }
        return values;
    }

    /**
     * Checks if the given service object exists in Algosec. If not, returns empty list. If the object exists, it recursively
     * checks, for each of the values, if any of those values, is an object itself. This continues till all objects and nested objects
     * are resolved and a final list of protocol+port values is returned..
     * @param potentialObject
     * @param algosecDeviceName
     * @return
     */
    private List<String> resolveIfServiceObjectExistsInAlgoSec(String potentialObject, String algosecDeviceName, ObjectsData objectsData) {
        List<String> values = new LinkedList<>();
        if (objectsData.getDeviceNameServiceObjectDetailsMap().containsKey(algosecDeviceName)
            && objectsData.getDeviceNameServiceObjectDetailsMap().get(algosecDeviceName).containsKey(potentialObject)) {
            List<String> serviceDefinitions = objectsData.getDeviceNameServiceObjectDetailsMap().get(algosecDeviceName).get(potentialObject);
            serviceDefinitions.forEach(serviceDefinition -> {
                if(serviceDefinition.contains("/")) { //valid protocol and port
                    values.add(serviceDefinition);
                } else {
                    values.addAll(resolveIfServiceObjectExistsInAlgoSec(serviceDefinition, algosecDeviceName, objectsData));
                }
            });
        }
        return values;
    }
}
