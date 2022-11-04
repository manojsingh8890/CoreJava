package com.ibm.sec.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.sec.dao.FirewallChangeDAO;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.*;
import com.ibm.sec.model.algosec.FireFlowAPIAuthResponse;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import com.ibm.sec.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the API. It validates input, makes service
 * calls and validates their output.
 */
@Component
@Slf4j
public class ServiceFacade {

	@Autowired
	private ErrorConfig errorConfig;
    @Autowired
    private TrafficChangeRequester trafficChangeRequester;
    @Autowired
    private JsonUtil jsonUtil;
	@Autowired
    FirewallChangeDAO firewallChangeDAO;
	@Autowired
	private AlgosecService algoSecService;
	@Autowired
	private Util util;
    @Autowired
    private ObjectsResolver objectsResolver;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RuleRemovalRequester ruleRemovalRequester;

	public String saveChangeRequest(String changeRequest) throws JsonProcessingException {
		String sessionId = util.getUniqueId();
		firewallChangeDAO.saveRequest(mapper.writeValueAsString(changeRequest), sessionId);
		return sessionId;
	}

    public List<AlgosecChangeId> createChange(JsonNode firewallChanges, Devices devices, IConstant.AlgosecChangeRententionType changeRententionType, UserSession session) throws JsonProcessingException {
        checkIfAlgosecActionableWork(firewallChanges);
        List<ChangeRequester> changeRequesters = new ArrayList<>(1);
        changeRequesters.add(trafficChangeRequester);
        changeRequesters.add(ruleRemovalRequester);

		ObjectsData objectsData = null;
		if(changeRententionType == IConstant.AlgosecChangeRententionType.TEMPORARY) {
			Map<String, Map<String, String>> deviceNameObjectDetailsMap = algoSecService.getNetworkObjectIPAddresesByDeviceNames(new ArrayList<>(devices.getRemedyDeviceNameAlgosecNameMap().values()), session);
			Map<String, Map<String, List<String>>> deviceNameServiceObjectDetailsMap = algoSecService.getServiceObjectValuesByDeviceEntityName(new ArrayList<>(devices.getRemedyDeviceNameAlgosecNameMap().values()), session);
			objectsData = objectsResolver.init(firewallChanges, deviceNameObjectDetailsMap, deviceNameServiceObjectDetailsMap);
		}

		List<AlgosecChangeId> algosecChangeIds = new ArrayList<>();
		for(ChangeRequester cr : changeRequesters) {
			try {
				algosecChangeIds.addAll(cr.createChangeRequest(firewallChanges, devices, changeRententionType, objectsData, session));
			} catch (BusinessLogicException e) {
				throw e;
			} catch (Exception e) {
                throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError(), e);
            }
		}

        if (algosecChangeIds.size() > 0) {
            algosecChangeIds.removeIf(Objects::isNull);

            try {
                List<FirewallChangeEntity> firewallChange = algosecChangeIds.stream().map(algosecId -> {
                    FirewallChangeEntity firewallChangeEntity = new FirewallChangeEntity();
					firewallChangeEntity.setSessionId(session.getSessionId());
					firewallChangeEntity.setChangeId(algosecId.getChangeId());
					firewallChangeEntity.setChangeType(algosecId.getChangeType());
					firewallChangeEntity.setAlgoSecChangeStatus(IConstant.AlgosecChangeStatus.NOT_APPLICABLE);
					firewallChangeEntity.setAlgoSecChangeRetentionType(changeRententionType);
                    return firewallChangeEntity;
                }).collect(Collectors.toList());
                firewallChangeDAO.saveAll(firewallChange);
            } catch (PersistenceException e) {
                throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError(), e);
            }
        }
		return algosecChangeIds;
    }

    private void checkIfAlgosecActionableWork(JsonNode firewallChangesNode) {
        if ((!firewallChangesNode.has(IConstant.POLICY_UPDATE_CHANGES) || firewallChangesNode.get(IConstant.POLICY_UPDATE_CHANGES).size() == 0)
        && (!firewallChangesNode.has(IConstant.OBJECT_UPDATE_CHANGES) || firewallChangesNode.get(IConstant.OBJECT_UPDATE_CHANGES).size() == 0)) {
            throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), errorConfig.getMessage().getNoAlgosecActionableWorkFound());
        }
    }

	public AlgosecUpdateStatusResponse updateStatusField(UserSession session) {
		AlgosecUpdateStatusResponse response = new AlgosecUpdateStatusResponse();
		try {
			List<FirewallChangeEntity> firewallChanges = firewallChangeDAO.getAllTemporaryPolicyChangeRequestsBySessionId(session.getSessionId());
			if (firewallChanges.size() > 0) {
				String algoSecSessionId = session.getAlgoSecSessionId();
				log.debug("AlgoSec SessionId: " + algoSecSessionId);
				List<String>  changeids = firewallChanges.stream().map(fwChange -> {
					return fwChange.getChangeId();
				}).collect(Collectors.toList());
				Map<String,List<String>> apiResponse = algoSecService.updateChangeStatus(changeids, session);
				if (apiResponse.get(IConstant.CHANGE_ID_STATUS_UPDATED) != null && !apiResponse.get(IConstant.CHANGE_ID_STATUS_UPDATED).isEmpty()) {
					List<FirewallChangeEntity> updatedStatus = firewallChanges.stream().filter(f -> apiResponse.get(IConstant.CHANGE_ID_STATUS_UPDATED).contains(f.getChangeId()))
							.collect(Collectors.toList());
					firewallChangeDAO.updateStatus(updatedStatus,IConstant.AlgosecChangeStatus.DISCARDED);
					response.setStatusUpdated(apiResponse.get(IConstant.CHANGE_ID_STATUS_UPDATED));
				}
				response.setStatusNotUpdated(apiResponse.get(IConstant.CHANGE_ID_STATUS_NOT_UPDATED));
			}
		} catch (PersistenceException e) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError(), e);
		} catch (Exception e) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError(), e);
		}
		return response;
	}

	 /**
     * This method used to fetch Risk Report from given change request Ids
     * Throws {@link JsonProcessingException}
     * @throws JsonProcessingException if get failure Change Requests Response
     */
	public String getAndSaveRiskInfo(UserSession session) throws JsonProcessingException {
		if(!firewallChangeDAO.areTemporaryChangeRequestsComplete(session.getSessionId())) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getTempCreateChangesIncomplete(), session.getSessionId()));
		}
		List<String> changeRequestIds = firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(session.getSessionId());
		if(changeRequestIds.isEmpty()) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getNoTempPolicyChangesFound(), session.getSessionId()));
		}

		ObjectNode rootNode = jsonUtil.newNode();
		ArrayNode riskReportNode = mapper.createArrayNode();
		Map<String,String> apiRisksResponse = algoSecService.getRiskReport(changeRequestIds, session);
		apiRisksResponse.forEach((changeId, algosecChangeContent) -> {
			log.info("Algosec Change Request content for session id {} and change id {} is {}", session.getSessionId(), changeId, algosecChangeContent);
		});
		ObjectNode valueNode = null;
		for(Map.Entry<String,String> entry: apiRisksResponse.entrySet()) {
			valueNode = jsonUtil.newNode();
			JsonNode riskReport = jsonUtil.parse(util.riskReportRegexMatch(entry.getValue()).replaceFirst("\"Risk\" :", ""));
			if(!riskReport.isEmpty()) {
			  valueNode.putArray("Risk").add(riskReport);
			  String riskDate = util.riskAnalysisDateRegexMatch(entry.getValue()).replaceAll("\"Date\" :", "").replaceAll("\"", "");
			  valueNode.put("Date", riskDate);
			  riskReportNode.add(valueNode);
			}
		}
		rootNode.set("RiskReport", riskReportNode);
		String riskInfo = rootNode.toString();
		firewallChangeDAO.saveRiskInfo(riskInfo, session.getSessionId());
		return riskInfo;
	}

//    public FirewallChangeEntity updateITSMField(String itsmId, String changeId) {
//        FirewallChangeEntity changeEntity = firewallChangeDAO.loadFirewallChangeByChangeId(changeId);
//        changeEntity.setItsmId(itsmId);
//        changeEntity.setStatus(IConstant.Status.COMPLETED.getStatus());
//        return firewallChangeDAO.save(changeEntity);
//    }
    
    
    /**
     * This method used to fetch change needed information from given change request Ids
     * Throws {@link JsonProcessingException}
     * @throws JsonProcessingException if get failure Change Requests Response or details not found
     */
    public String getAndSaveChangeNeededInfo(UserSession session) throws JsonProcessingException {
		if(!firewallChangeDAO.areTemporaryChangeRequestsComplete(session.getSessionId())) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getTempCreateChangesIncomplete(), session.getSessionId()));
		}
		List<String> changeRequestIds = firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(session.getSessionId());
		if(changeRequestIds.isEmpty()) {
			throw new BusinessLogicException(errorConfig.getHttpStatus().getBadRequest(), errorConfig.getCode().getBadRequest(), String.format(errorConfig.getMessage().getNoTempPolicyChangesFound(), session.getSessionId()));
		}
		ObjectNode rootNode = jsonUtil.newNode();
		Map<String,String> algosecChangeIdContentMap = algoSecService.getAlgosecChangeInfo(changeRequestIds, session);
		List<String> responses = algoSecService.getChangeNeededInfo(getTrafficSimulationRequestPayload(algosecChangeIdContentMap), session);
		ArrayNode finalResult = buildChangeNeededInfoResponse(responses);
    	rootNode.set("ChangeNeededInfo", finalResult);
		String changeNeededInfo = rootNode.toString();
		firewallChangeDAO.saveChangeNeededInfo(changeNeededInfo, session.getSessionId());
		return changeNeededInfo;
	}

	public AlgosecTaskStatusResponse getStatusOfTemporaryChange(String sessionId) {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(sessionId);
		if(firewallChangeDAO.areTemporaryChangeRequestsComplete(sessionId)) {
			List<String> errors = firewallChangeDAO.getErrorsBySessionIdAndFunctionality(sessionId, IConstant.Functionality.TEMPORAY_CHANGE_CREATION);
			errors.forEach(error -> response.addError(error));
			if(errors.isEmpty()) {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS);
			} else {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR);
			}
		} else {
			response.setStatus(IConstant.AlgosecTaskStatus.PENDING);
		}
		return response;
	}

	public AlgosecTaskStatusResponse getStatusOfFetchRiskInfo(String sessionId) {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(sessionId);
		if(firewallChangeDAO.isRisksFetchComplete(sessionId)) {
			List<String> errors = firewallChangeDAO.getErrorsBySessionIdAndFunctionality(sessionId, IConstant.Functionality.RISK_INFO_FETCH);
			if(errors.isEmpty()) {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS);
				response.setData(firewallChangeDAO.getRiskInfoBySessionId(sessionId));
			} else {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR);
				errors.forEach(error -> response.addError(error));
			}
		} else {
			response.setStatus(IConstant.AlgosecTaskStatus.PENDING);
		}
		return response;
	}

	public AlgosecTaskStatusResponse getStatusOfChangeNeededInfo(String sessionId) {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(sessionId);
		if(firewallChangeDAO.isChangeNeededInfoFetchComplete(sessionId)) {
			List<String> errors = firewallChangeDAO.getErrorsBySessionIdAndFunctionality(sessionId, IConstant.Functionality.CHANGE_NEEDED_INFO_FETCH);
			if(errors.isEmpty()) {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS);
				response.setData(firewallChangeDAO.getChangeNeededInfoBySessionId(sessionId));
			} else {
				response.setStatus(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR);
				errors.forEach(error -> response.addError(error));
			}
		} else {
			response.setStatus(IConstant.AlgosecTaskStatus.PENDING);
		}
		return response;
	}

	public void addAlgosecSessionIdsToUserSession(UserSession session) {
		FireFlowAPIAuthResponse authResponse = algoSecService.authenticate(session);
		session.setAlgoSecSessionId(authResponse.getSessionId());
		session.setAlgoSecFaSessionId(authResponse.getFaSessionId());
		session.setAlgoSecPhpSessionId(authResponse.getPhpSessionId());
	}

	/**
	 * Preparing request payload for Traffic Simulation Service
	 * Throws {@link BusinessLogicException JsonProcessingException}
	 * @param algosecChangeIdContentMap Requests Response from Change Requests traffic service
	 * @throws JsonProcessingException if get failure Change Requests Response or details not found
	 */
	private List<String> getTrafficSimulationRequestPayload(Map<String,String> algosecChangeIdContentMap){
		List<String> requestPayload = new ArrayList<>();
		algosecChangeIdContentMap.entrySet().forEach(algosecChangeIdContentMapEntry -> {
			ObjectNode rootNode = jsonUtil.newNode();
			rootNode.put("includeDevicesPaths", true);
			rootNode.put("includeRulesZones", true);

			ArrayNode arrayNode = mapper.createArrayNode();
			try {
				JsonNode jsonTicketDetails = jsonUtil.parse(algosecChangeIdContentMapEntry.getValue());

				if(jsonTicketDetails.get("status") == null || !"success".equalsIgnoreCase(jsonTicketDetails.get("status").asText())) {
					throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), String.format(errorConfig.getMessage().getTicketNotInitializedYet(), algosecChangeIdContentMapEntry.getKey()));
				}

				JsonNode fields = jsonTicketDetails.get("data").get("fields");
				String deviceName = null;
				for (JsonNode fieldsObjNode : fields) {
					if(fieldsObjNode.get("name")  != null && "Devices".equals(fieldsObjNode.get("name").asText())) {
						if(fieldsObjNode.get("values").get(0) != null) {
							deviceName = fieldsObjNode.get("values").get(0).asText();
							break;
						}
					}
				}

				if(deviceName == null) {
					throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), String.format(errorConfig.getMessage().getFetchedTicketDetailsInvalidOrIncomplete(), algosecChangeIdContentMapEntry.getKey()));
				}

				if(jsonTicketDetails.get("data") == null || jsonTicketDetails.get("data").get("originalTraffic") == null ) {
					throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(), errorConfig.getCode().getInternalError(), String.format(errorConfig.getMessage().getFetchedTicketDetailsInvalidOrIncomplete(), algosecChangeIdContentMapEntry.getKey()));
				}

				JsonNode originalRequest = jsonTicketDetails.get("data").get("originalTraffic");
				ObjectNode dataObj = null;
				for (JsonNode objNode : originalRequest) {
					dataObj = mapper.createObjectNode();
					dataObj.set("source", extractTicketDetailsJsonResponse(objNode.get("source")));
					dataObj.set("destination", extractTicketDetailsJsonResponse(objNode.get("destination")));
					dataObj.set("service", extractTicketDetailsJsonResponse(objNode.get("service")));
					dataObj.set("application", extractTicketDetailsJsonResponse(objNode.get("application")));
					dataObj.set("user", extractTicketDetailsJsonResponse(objNode.get("user")));
					arrayNode.add(dataObj);
				}

				if(arrayNode.size() > 0) {
					rootNode.put("queryTarget", deviceName);
					rootNode.set("queryInput", arrayNode);
					requestPayload.add(rootNode.toString());
				}

			} catch (JsonProcessingException e) {
				throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError());
			}
		});
		return requestPayload;
	}

	/**
	 * Extracting json values returned from ticket details to create traffic simulation request payload
	 * @return ArrayNode
	 */
	private ArrayNode extractTicketDetailsJsonResponse(JsonNode node) {
		ArrayNode arrayNode = mapper.createArrayNode();
		for (JsonNode objNode : node.get("items")) {
			arrayNode.add(toLowerCaseIfValueIsANY(objNode.get("value").asText()));
		}
		return arrayNode;
	}

	/*
	* if value is "ANY" then return "any"
	 */
	private String toLowerCaseIfValueIsANY(String value) {
		if(value.equals("ANY")) {
			return "any";
		} else {
			return value;
		}
	}

	/**
	 * Building final response of change needed info by extracting traffic simulation API returned response
	 * Throws {@link JsonProcessingException}
	 * @param actualResponse simulation API response
	 * @throws JsonProcessingException if get failure traffic simulation API response
	 */
	private ArrayNode buildChangeNeededInfoResponse(List<String> actualResponse)  throws JsonProcessingException {
		ArrayNode arrayNode = mapper.createArrayNode();

		if(actualResponse.size() > 0) {
			actualResponse.forEach(x ->{
				JsonNode jsonResponse;
				try {
					jsonResponse = jsonUtil.parse(x);

//					if(jsonResponse == null || jsonResponse.get("queryResult") == null || jsonResponse.get("queryResult").size() == 0 ) {
//						throw new BusinessLogicException(errorConfig.getHttpStatus().getNoAlgosecChangeNeededInfoFound(), errorConfig.getCode().getNoAlgosecChangeNeededInfoFound(), errorConfig.getMessage().getNoAlgosecChangeNeededInfoFound());
//					}

					ObjectNode dataObj = null;
					for (JsonNode objNode : jsonResponse.get("queryResult")) {
//						if(objNode.get("queryDescription") == null) {
//							throw new BusinessLogicException(errorConfig.getHttpStatus().getNoAlgosecChangeNeededInfoFound(), errorConfig.getCode().getNoAlgosecChangeNeededInfoFound(), errorConfig.getMessage().getNoAlgosecChangeNeededInfoFound());
//						}
						String queryDesc = objNode.get("queryDescription").asText();
						String[] ruleFields = queryDesc.split(":");
						dataObj = mapper.createObjectNode();
						dataObj.put("source", ruleFields[0].split("=>")[0]);
						dataObj.put("destination", ruleFields[0].split("=>")[1]);
						dataObj.put("service", ruleFields[1]);

						if(objNode.get("finalResult") != null && ("Blocked".toLowerCase().equals(objNode.get("finalResult").asText().toLowerCase()) || "Partially allowed".toLowerCase().equals(objNode.get("finalResult").asText().toLowerCase()))){
							dataObj.put("isChangeNeeded", true);
						}else {
							dataObj.put("isChangeNeeded", false);
						}

						if(objNode.get("queryItem") != null) {
							ArrayNode queryItem = mapper.createArrayNode();
							ObjectNode queryItemObj = null;
							for(JsonNode queryItems : objNode.get("queryItem")) {
								queryItemObj = mapper.createObjectNode();
								queryItemObj.set("message", queryItems.get("isAllowed"));
								queryItemObj.set("deviceName", queryItems.get("deviceName"));
								queryItemObj.set("displayName", queryItems.get("displayName"));
								queryItem.add(queryItemObj);
							}
							dataObj.set("queryItem", queryItem);
						}
						if(dataObj != null) {
							arrayNode.add(dataObj);
						}
					}
				} catch (JsonProcessingException e) {
					throw new BusinessLogicException(errorConfig.getHttpStatus().getInternalError(),errorConfig.getCode().getInternalError(), errorConfig.getMessage().getInternalError());
				}
			});
		}
		return arrayNode;
	}
}
