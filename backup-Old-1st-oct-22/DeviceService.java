package com.ibm.sec.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.Devices;
import com.ibm.sec.model.UserSession;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

/**
 * Holds data of the devices passed in as input. Also, provides various information/validation on these devices.
 */
@Component
public class DeviceService {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    private JsonUtil jsonUtil;

    /**
     * Does the following:
     * 1. Validates if devices are algosec enabled.
     * 2. Gets and keeps algosec device names for the given remedy device names.
     * @param firewallChanges input firewall changes json
     * @throws JsonProcessingException If any error in parsing JSON response from services called from this method
     */
    public Devices fetchDeviceInfo(JsonNode firewallChanges, UserSession session) throws JsonProcessingException {
        Devices devices = new Devices();
        Set<String> remedyDeviceNamesSet = new LinkedHashSet<>();
        if (firewallChanges.has(IConstant.POLICY_UPDATE_CHANGES)) {
            JsonNode policyUpdateNode = firewallChanges.get(IConstant.POLICY_UPDATE_CHANGES);
            String policyUpdateString = jsonUtil.toString(policyUpdateNode);
            remedyDeviceNamesSet.addAll(jsonUtil.getFieldValuesAsText(policyUpdateString, "$.[*].firewall_policy"));
        }
        if (firewallChanges.has(IConstant.OBJECT_UPDATE_CHANGES)) {
            JsonNode objectUpdateNode = firewallChanges.get(IConstant.OBJECT_UPDATE_CHANGES);
            String objectUpdateString = jsonUtil.toString(objectUpdateNode);
            remedyDeviceNamesSet.addAll(jsonUtil.getFieldValuesAsText(objectUpdateString, "$.[*].firewall_policy"));
        }
        devices.getRemedyDeviceNames().addAll(remedyDeviceNamesSet);
        String devicesNames = prepareDeviceNamesInput(List.copyOf(remedyDeviceNamesSet));
        String devicesResponse = applicationService.getDevicesWithHostName(session, devicesNames).block();
        String deviceIdsString = prepareDeviceEnablementInput(devicesNames, devicesResponse);
        String deviceEnablementResponse = applicationService.getDeviceEnablement(session, deviceIdsString).block();
        validateDevicesAreAlgoSecEnabled(deviceEnablementResponse, remedyDeviceNamesSet.size());
        devices.getRemedyDeviceIds().addAll(Arrays.asList(deviceIdsString.split(",")));
        JsonNode deviceResponseNode = jsonUtil.parse(devicesResponse);
        Map<String, String> remedyDeviceNameAlgosecNameMap = new HashMap<>();
        for (JsonNode valueNode : deviceResponseNode) {
            String hostName = valueNode.get("hostName").asText();
            String algoSecUniqueName = valueNode.get("algoSecUniqueName").asText();
            remedyDeviceNameAlgosecNameMap.put(hostName, algoSecUniqueName);
        }
        devices.getRemedyDeviceNameAlgosecNameMap().putAll(remedyDeviceNameAlgosecNameMap);
        return devices;
    }

//    /**
//     * Returns remedy names of the input devices.
//     * @return Remedy names of devices
//     */
//    public List<String> getRemedyDeviceNames() {
//        return remedyDeviceNames;
//    }
//
//    /**
//     * Returns algosec names of the input devices.
//     * @return Algosec names of devices
//     */
//    public Map<String, String> getAlgosecDeviceNames() {
//        return algosecDeviceNames;
//    }
//
//    /**
//     * Returns remedy ID of input devices.
//     * @return
//     */
//    public List<String> getRemedyDeviceIds() {
//        return remedyDeviceIds;
//    }

    /**
     * Returned a comma separated concatenated string from the given list of device names.
     * This string is used as input for calling device service to fetch details of the devices.
     * @param devicesNames Remedy names of input devices
     * @return Comma separated string of remedy device names
     */
    private String prepareDeviceNamesInput(List<String> devicesNames) {
        return String.join(",", devicesNames);
    }

    /**
     * Returns a comma separated string of remedy device ids for the given remedy device names from the device service response.
     * @param devicesNames Remedy names of input devices
     * @param devicesResponse Response from device service for the given input devices
     * @return Comma separated string of device ids
     */
    private String prepareDeviceEnablementInput(String devicesNames, String devicesResponse) {
        List<String> deviceIds = jsonUtil.getFieldValuesAsText(devicesResponse, "$.[*].id");
        if (deviceIds.isEmpty()) {
            throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), errorConfig.getMessage().getNoDeviceFound());
        }
        if (devicesNames.split(",").length != deviceIds.size()) { //requested device count does not match with actual device count
            throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), errorConfig.getMessage().getIncorrectDeviceCount());
        }
        return String.join(",", deviceIds);
    }

    /**
     * Validates whether input devices are algosec enabled or not. If not, we cannot proceed to do anything on these devices on algosec.
     * Throws {@link BusinessLogicException} if validation fails.
     * @param deviceEnablementResponse Response from device enablement service for the input remedy devices' ids
     * @throws JsonProcessingException if there is any error in parsing deviceEnablementResponse
     */
    private void validateDevicesAreAlgoSecEnabled(String deviceEnablementResponse, int remedyDeviceCount) throws JsonProcessingException {
        ArrayNode deviceEnablementJson = (ArrayNode) jsonUtil.parse(deviceEnablementResponse);
        if (remedyDeviceCount != deviceEnablementJson.size()) {
            throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), errorConfig.getMessage().getNoAlgosecEnabledDeviceFound());
        }
    }
}