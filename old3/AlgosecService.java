package com.ibm.sec.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.UserSession;
import com.ibm.sec.model.algosec.FieldConfig;
import com.ibm.sec.model.algosec.FireFlowAPIAuthRequest;
import com.ibm.sec.model.algosec.FireFlowAPIAuthResponse;
import com.ibm.sec.model.algosec.ObjectChangeRequest;
import com.ibm.sec.model.algosec.RuleRemovalChangeRequest;
import com.ibm.sec.model.algosec.TrafficChangeRequest;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import com.ibm.sec.util.RestUtil;
import com.ibm.sec.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * A single point of contact for calling APIs.
 */
@Service
@Slf4j
public class AlgosecService {

    @Autowired
    private ServicesConfig servicesConfig;
    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private JsonUtil jsonUtil;
	@Autowired
	private FieldConfig fieldConfig;
    @Autowired
    private Util util;
    
    @Value("${algosec-integration-ms.algosec.username}")
    private String algosecUsername;
    
    @Value("${algosec-integration-ms.algosec.password}")
    private String algosecPassword;

    public FireFlowAPIAuthResponse authenticate(UserSession session) {
        FireFlowAPIAuthRequest request = new FireFlowAPIAuthRequest();
        request.setUserName(algosecUsername);
        request.setPassword(algosecPassword);

        FireFlowAPIAuthResponse authResponse = null;
        Mono<ClientResponse> responseMono = restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowAuthenticationUrl(), null, request);
        ClientResponse response = responseMono.block();
        ResponseEntity<String> responseEntity = response.toEntity(String.class).block();
        String responseBody = responseEntity.getBody();
        if (response.rawStatusCode() != 200) {
            log.error("Algosec authentication API call failed with http status code:" + response.rawStatusCode() + " and error response is:" + responseBody);
            throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError());
        } else {
            authResponse = new FireFlowAPIAuthResponse();
            authResponse.setSessionId(jsonUtil.getFieldValueAsText(responseBody, "$.data.sessionId"));
            authResponse.setFaSessionId(jsonUtil.getFieldValueAsText(responseBody, "$.data.faSessionId"));
            authResponse.setPhpSessionId(jsonUtil.getFieldValueAsText(responseBody, "$.data.phpSessionId"));
        }
        return authResponse;
    }

    public List<String> getDisplayNamesByNames(List<String> algosecDeviceNames, UserSession session) {
        String cookieHeader = String.format("PHPSESSID=%s", session.getAlgoSecPhpSessionId());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookieHeader);
        String response = restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecDeviceMappingUrl(), String.class, headerMap).block();
        List<String> algosecDisplayNames = new ArrayList<>();
        algosecDeviceNames.forEach(algosecDeviceName -> {
            String algoSecDeviceNameJsonPath = String.format("$..[?(@.name=='%s')].display_name", algosecDeviceName); //two dots after $ to denote recursive descent. The "name" attribute can be in first level or 2nd level array elements
            String algoSecDisplayName = jsonUtil.getFieldValuesAsText(response, algoSecDeviceNameJsonPath).get(0);
            algosecDisplayNames.add(algoSecDisplayName);
        });
        return algosecDisplayNames;
    }

    /**
     * Returns a map where key is device name and value another map where key is object name and value is the IP address details.
     */
    public Map<String, Map<String, String>> getNetworkObjectIPAddresesByDeviceNames(List<String> algosecDeviceNames, UserSession session) {
        Map<String, Map<String, String>> algoSecDeviceNameObjectDetailsMap = new HashMap<>();
        String cookieHeader = String.format("PHPSESSID=%s", session.getAlgoSecPhpSessionId());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookieHeader);
        String response = restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecFindNetworkObjectsByDeviceName(), String.class, headerMap, util.listToCommaSeparated(algosecDeviceNames)).block();
        algosecDeviceNames.forEach(algoSecDeviceName -> {
            if (!algoSecDeviceNameObjectDetailsMap.containsKey(algoSecDeviceName)) {
                algoSecDeviceNameObjectDetailsMap.put(algoSecDeviceName, new HashMap<>());
            }
            String algoSecDeviceNameToObjectDetailsJsonPath = String.format("$.content[?(@.objectContainer.name=='%s')]", algoSecDeviceName);
            List<Map> objectDetailsMaps = jsonUtil.getFieldValuesAsMaps(response, algoSecDeviceNameToObjectDetailsJsonPath);
            objectDetailsMaps.forEach(objectDetailsMap -> {
                algoSecDeviceNameObjectDetailsMap.get(algoSecDeviceName).put((String) objectDetailsMap.get("canonizedName"), (String) objectDetailsMap.get("ipaddress"));
            });
        });
        return algoSecDeviceNameObjectDetailsMap;
    }

    /**
     * Returns a map where key is algosec device name and value is another map where key is a service object's name and value is
     * a list of service definitions for that object.
     */
    public Map<String, Map<String, List<String>>> getServiceObjectValuesByDeviceEntityName(List<String> algosecDeviceNames, UserSession session) throws JsonProcessingException {
        List<String> algosecDisplayNames = getDisplayNamesByNames(algosecDeviceNames, session);
        Map<String, Map<String, List<String>>> algoSecDeviceNameServiceObjectDetailsMap = new HashMap<>();
        String cookieHeader = String.format("PHPSESSID=%s", session.getAlgoSecPhpSessionId());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookieHeader);
        List<Mono<String>> monos = new ArrayList<>();
        algosecDisplayNames.forEach(algosecDisplayName -> {
            monos.add(restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecFindServiceObjectsByRemedyDeviceId(), String.class, headerMap, algosecDisplayName).subscribeOn(Schedulers.elastic()));
        });

        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();

        int i = 0;
        for (Object result : allResults) {
            JsonNode serviceObjectDetailsResponse = jsonUtil.parse((String) result);
            //String algosecDeviceName = serviceObjectDetailsResponse.get("entitiesReponses").get(0).get("name").asText();
            Map<String, List<String>> serviceObjectNameValuesMap = new HashMap<>();
            serviceObjectDetailsResponse.get("entitiesReponses").iterator().forEachRemaining(entityResponse -> {
                JsonNode valueNodeoeArray = entityResponse.get("values");
                for (JsonNode valueNode : valueNodeoeArray) {
                    String serviceObjectName = valueNode.get("name").asText();
                    JsonNode serviceDefinitionNodes = valueNode.get("serviceDefinitions");
                    List<String> serviceObjectValues = new ArrayList<>(serviceDefinitionNodes.size());
                    for (JsonNode serviceDefinitionNode : serviceDefinitionNodes) {
                        serviceObjectValues.add(serviceDefinitionNode.asText());
                    }
                    serviceObjectNameValuesMap.put(serviceObjectName, serviceObjectValues);
                }
            });
            String algosecDeviceName = algosecDeviceNames.get(i);
            algoSecDeviceNameServiceObjectDetailsMap.put(algosecDeviceName, serviceObjectNameValuesMap);
            i++;
        }
        return algoSecDeviceNameServiceObjectDetailsMap;
    }

//    public int createChangeRequest(ChangeRequest request, UserSession session) {
//        String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Cookie", cookieHeader);
//        Mono<ClientResponse> responseMono = restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowChangeRequestUrl(), headers, request);
//        ClientResponse response = responseMono.block();
//        ResponseEntity<String> responseEntity = response.toEntity(String.class).block();
//        String responseBody = responseEntity.getBody();
//        if (response.rawStatusCode() != 200) {
//            log.error("Algosec change request API call failed with http status code:" + response.rawStatusCode() + " and error response is:" + responseBody);
//            throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError());
//        } else {
//            return jsonUtil.getFieldValueAsInt(responseBody, "$.data.changeRequestId");
//        }
//    }

    /*
     * To Ask , if multiple id comes and one of them failed , then what we should we do?
     */
    public  Map<String, List<String>> updateChangeStatus(List<String> changeIds, UserSession session) {
        String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        headers.put("Content-Type", "application/json");
        List<Mono<ClientResponse>> monos = new ArrayList<>();
        Map<String, List<String>> responseData = new HashMap<String, List<String>>();
        responseData.put(IConstant.CHANGE_ID_STATUS_UPDATED, new ArrayList<String>());
        responseData.put(IConstant.CHANGE_ID_STATUS_NOT_UPDATED, new ArrayList<String>());
        
        ArrayNode updateFields = jsonUtil.newArrayNode();
        ObjectNode fieldMap = jsonUtil.newNode();
        ArrayNode fieldValues = jsonUtil.newArrayNode();
        fieldValues.add(fieldConfig.getValue());
        fieldMap.put("name", fieldConfig.getField());
        fieldMap.set("values", fieldValues);
        updateFields.add(fieldMap);

        changeIds.forEach(ci -> {
        	monos.add(restUtil.makePutCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowChangeRequestUrl() + "/" + ci + "/fields", headers, updateFields.toString()).subscribeOn(Schedulers.elastic()));
        });
        
        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();
        
        for (int i = 0 ; i < changeIds.size(); i++) {
            ClientResponse response = ((ClientResponse)allResults[i]);
            String changeId = changeIds.get(i);
            if(response.rawStatusCode() != 200) {
            	responseData.get(IConstant.CHANGE_ID_STATUS_NOT_UPDATED).add(changeId);
                log.error("Algosec change request API call failed for change id: " + changeId + " with http status code: " + response.rawStatusCode() + " and error response is: " + response.toEntity(String.class).block().getBody());
            } else {
            	 responseData.get(IConstant.CHANGE_ID_STATUS_UPDATED).add(changeId);
            }
        }
        return responseData;
    }

    public Map<String, String> getRiskReport(List<String> changeIds, UserSession session) {
        String cookieHeader = String.format("RT_SID_FireFlow.443=%s; PHPSESSID=%s", session.getAlgoSecSessionId(), session.getAlgoSecPhpSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        headers.put("Content-Type", "text/plain");
        Map<String, String> resultMap = new HashMap<String, String>();
        List<Mono<String>> monos = new ArrayList<>();
        List<String> uniqueChangeIds = changeIds.stream().distinct().collect(Collectors.toList());
        uniqueChangeIds.forEach(changeId -> {
            monos.add(restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowRiskReportUrl() + "/" + changeId, String.class, headers).subscribeOn(Schedulers.elastic()));
        });

        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();
        resultMap = IntStream.range(0, uniqueChangeIds.size()).boxed().collect(Collectors.toMap(i -> uniqueChangeIds.get(i), i -> (String) allResults[i]));
        return resultMap;
    }

    /**
     * This method is calling traffic simulation service API to fetch change needed information bases on request payload
     */
    public List<String> getChangeNeededInfo(List<String> queryJsons, UserSession session) {
        String cookieHeader = String.format("RT_SID_FireFlow.443=%s; PHPSESSID=%s", session.getAlgoSecSessionId(), session.getAlgoSecPhpSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        headers.put("Content-Type", "text/plain");

        List<Mono<ClientResponse>> monos = new ArrayList<>();
        List<String> responseResults = new ArrayList<String>();

        queryJsons.forEach(queryJson -> {
            monos.add(restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecTrafficSimulationUrl(), headers, queryJson).subscribeOn(Schedulers.elastic()));
        });

        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();

        int i = 0;
        for (Object result : allResults) {
            ClientResponse response = ((ClientResponse)result);
            ResponseEntity<String> responseEntity = response.toEntity(String.class).block();
            String responseBody = responseEntity.getBody();
            if(response.rawStatusCode() != 200) {
                //log.error("Algosec change request API call failed with http status code:" + response.rawStatusCode() + " and error response is:" + responseBody);
                log.error("SessionId: " + session.getSessionId() + " traffic change request: " + queryJsons.get(i++) + ", response: " + responseBody + ", IsError? TRUE http code: " + response.rawStatusCode());
                throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError());
            } else {
                responseResults.add(responseBody);
                log.error("SessionId: " + session.getSessionId() + " traffic change request: " + queryJsons.get(i++) + ", response: " + responseBody + ", IsError? FALSE http code: " + response.rawStatusCode());
            }
        }
        return responseResults;
    }

    /**
     * This method is calling ticket details API service to fetch details based on change request id.
     * In the map returned the key is the algosec change id and value is the content of the change.
     */
    public Map<String,String> getAlgosecChangeInfo(List<String> changeIds, UserSession session) {
    	String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        headers.put("Content-Type", "text/plain");
        Map<String, String> resultMap = new HashMap<String, String>();
        List<Mono<String>> monos = new ArrayList<>();
        List<String> uniqueChangeIds = changeIds.stream().distinct().collect(Collectors.toList());
        uniqueChangeIds.forEach(changeId -> {
        	monos.add(restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowChangeRequestUrl()+"/"+changeId, String.class, headers).subscribeOn(Schedulers.elastic()));
        });

        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();
        resultMap = IntStream.range(0, uniqueChangeIds.size()).boxed().collect(Collectors.toMap(i -> uniqueChangeIds.get(i), i -> (String) allResults[i]));
        return resultMap;
    }

    public Object[] getRuleInformation(List<String> algosecDeviceNames, UserSession session) {
        String cookieHeader = String.format("RT_SID_FireFlow.443=%s; PHPSESSID=%s", session.getAlgoSecSessionId(), session.getAlgoSecPhpSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        headers.put("Content-Type", "text/plain");
        List<Mono<String>> monos = new ArrayList<>();
        algosecDeviceNames.forEach(algosecDeviceName -> {
            monos.add(restUtil.makeGetCallToAlgosecAPIs(session, servicesConfig.getAlgosecRulesByDeviceNameUrl(), String.class, headers, algosecDeviceName));
        });
        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();
        return allResults;
    }

    public Object[] createRuleRemovalRequest(List<RuleRemovalChangeRequest> algosecRequests, UserSession session) {
        String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        List<Mono<ClientResponse>> monos = new ArrayList<>();
        algosecRequests.forEach(algosecChangeRequest -> {
//            try {
//                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//                String json = ow.writeValueAsString(algosecChangeRequest);
//                System.out.println("check here--->" + json);
//            } catch (JsonProcessingException e) {
//
//            }
            monos.add(restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowRuleRemovalRequestUrl(), headers, algosecChangeRequest));
        });
        Object[] allResults = Mono.zip(monos, values -> {
            return values;
        }).block();
        return allResults;
    }

    public Object[] createTrafficRequest(List<TrafficChangeRequest> algosecRequests, UserSession session) {

        String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        List<Mono<ClientResponse>> monos = new ArrayList<>();
        algosecRequests.forEach(algosecChangeRequest -> {
//            try {
//                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//                String json = ow.writeValueAsString(algosecChangeRequest);
//                System.out.println("check here--->" + json);
//            } catch (JsonProcessingException e) {
//
//            }
            monos.add(restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowChangeRequestUrl(), headers, algosecChangeRequest));
        });
        Object[] allResults = Mono.zip(monos, values -> {
            System.out.println("values---->"+values);
            return values;
        }).block();
        return allResults;
    }

    public Object[] createObjectChangeRequest(List<ObjectChangeRequest> algosecRequests, UserSession session) {
        String cookieHeader = String.format("FireFlow_Session=%s; Path=/aff/api/external; Secure;", session.getAlgoSecSessionId());
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieHeader);
        List<Mono<ClientResponse>> monos = new ArrayList<>();
        algosecRequests.forEach(algosecChangeRequest -> {
//            try {
//                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//                String json = ow.writeValueAsString(algosecChangeRequest);
//                System.out.println("check here--->" + json);
//            } catch (JsonProcessingException e) {
//
//            }
            monos.add(restUtil.makePostCallToAlgosecAPIs(session, servicesConfig.getAlgosecFireflowObjectChangeRequestUrl(), headers, algosecChangeRequest));
        });
        Object[] allResults = Mono.zip(monos, values -> {
            System.out.println("values---->"+values);
            return values;
        }).block();
        return allResults;
    }
    public void updateSocTicket(List<String> algosecChangeIds){

    }
}
