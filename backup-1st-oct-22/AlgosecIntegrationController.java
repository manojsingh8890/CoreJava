package com.ibm.sec.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ibm.sec.dao.FirewallChangeDAO;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.*;
import com.ibm.sec.service.DeviceService;
import com.ibm.sec.service.ServiceFacade;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/algosec-integration")
@Slf4j
public class AlgosecIntegrationController {

    @Autowired
    private ServiceFacade businessLogic;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private DeviceService deviceService;
//    @Autowired
//    private TaskQueue taskQueue;
    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    @Qualifier("controllerAsyncTaskExecutor")
    private Executor taskExecutor;
    @Autowired
    private FirewallChangeDAO firewallChangeDAO;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, value = "/submit-task/create-temporary-change")
    public AlgosecTempChangeTaskAckResponse createTemporaryChangeRequests(@RequestBody String requestJsonString) throws JsonProcessingException {
        String sessionId = businessLogic.saveChangeRequest(requestJsonString);
        UserSession session = (UserSession) RequestContextHolder.currentRequestAttributes().getAttribute(IConstant.USER_SESSION_REQ_ATTR_KEY, RequestAttributes.SCOPE_REQUEST);
        session.setSessionId(sessionId);
        AlgosecTempChangeTaskAckResponse response = new AlgosecTempChangeTaskAckResponse();
        response.setSessionId(sessionId);

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                runAsyncCreateTemporaryRequests(requestJsonString, session);
            }
        });

        return response;
    }

    protected void runAsyncCreateTemporaryRequests(String requestJsonString, UserSession session) {
        try {
            firewallChangeDAO.initiateFirewallChangeSession(session.getSessionId());
            JsonNode requestJson = jsonUtil.parse(requestJsonString);
            businessLogic.addAlgosecSessionIdsToUserSession(session);
            Devices devices = deviceService.fetchDeviceInfo(requestJson, session);
            List<AlgosecChangeId> algosecChangeIds = businessLogic.createChange(requestJson, devices, IConstant.AlgosecChangeRententionType.TEMPORARY, session);
            log.info("Algosec change ids {} created for session id {}", String.join(",", algosecChangeIds.stream().map(AlgosecChangeId::getChangeId).collect(Collectors.toList()).toArray(new String[algosecChangeIds.size()])));
        } catch (Exception e) {
            log.error("Error occurred for sessonId " + session.getSessionId(), e);
            firewallChangeDAO.saveError(e.getMessage(), IConstant.Functionality.TEMPORAY_CHANGE_CREATION, session.getSessionId());
        } finally {
            firewallChangeDAO.markCompleteTemporaryRequests(session.getSessionId());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/fetch-task-status/temporary-change/{sessionId}")
    public AlgosecTaskStatusResponse getStatusOfTemporaryChange(@PathVariable(value = "sessionId", required = true) String sessionId) {
        return businessLogic.getStatusOfTemporaryChange(sessionId);
    }

//    @DeleteMapping("/discard-temporary-change/{sessionId}")
//	public AlgosecUpdateStatusResponse discardTemporaryChangeRequests(@PathVariable(value = "sessionId", required = true) String sessionId) {
//        UserSession session = (UserSession) RequestContextHolder.currentRequestAttributes().getAttribute(IConstant.USER_SESSION_REQ_ATTR_KEY, RequestAttributes.SCOPE_REQUEST);
//		return businessLogic.updateStatusField(session);
//	}

    @PostMapping("/submit-task/fetch-risk-info/{sessionId}")
    public String startFetchRiskInfo(@PathVariable("sessionId") String sessionId) throws JsonProcessingException {
        UserSession session = (UserSession) RequestContextHolder.currentRequestAttributes().getAttribute(IConstant.USER_SESSION_REQ_ATTR_KEY, RequestAttributes.SCOPE_REQUEST);
        session.setSessionId(sessionId);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    businessLogic.addAlgosecSessionIdsToUserSession(session);
                    businessLogic.getAndSaveRiskInfo(session);
                } catch (Exception e) {
                    log.error("Error occurred for sessonId " + session.getSessionId(), e);
                    firewallChangeDAO.saveError(e.getMessage(), IConstant.Functionality.RISK_INFO_FETCH, session.getSessionId());
                } finally {
                    firewallChangeDAO.markCompleteRiskInfoFetch(sessionId);
                }
            }
        });
        return "{\"message\": \"acknowledged\"}";
    }

    @GetMapping(value="/fetch-task-status/risk-info/{sessionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getStatusOfFetchRiskInfo(@PathVariable("sessionId") String sessionId) {
        return businessLogic.getStatusOfFetchRiskInfo(sessionId);
    }

//    @PutMapping("/LinkITSM")
//    public FirewallChangeEntity linkRemedyTicketToAlgosecInDatabase(@RequestParam(name = "itsmId") String itsmId, @RequestParam(name = "changeId") String changeId) {
//    return businessLogic.updateITSMField(itsmId, changeId);
//    }

    @PostMapping("/submit-task/fetch-change-needed-info/{sessionId}")
    public String startFetchChangeNeededInfo(@PathVariable("sessionId") String sessionId) {
        UserSession session = (UserSession) RequestContextHolder.currentRequestAttributes().getAttribute(IConstant.USER_SESSION_REQ_ATTR_KEY, RequestAttributes.SCOPE_REQUEST);
        session.setSessionId(sessionId);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    businessLogic.addAlgosecSessionIdsToUserSession(session);
                    businessLogic.getAndSaveChangeNeededInfo(session);
                } catch (Exception e) {
                    log.error("Error occurred for sessonId " + session.getSessionId(), e);
                    firewallChangeDAO.saveError(e.getMessage(), IConstant.Functionality.CHANGE_NEEDED_INFO_FETCH, session.getSessionId());
                } finally {
                    firewallChangeDAO.markCompleteChangeNeededInfoFetch(sessionId);
                }
            }
        });
        return "{\"message\": \"acknowledged\"}";
    }
    
    @GetMapping(value="/fetch-task-status/change-needed-info/{sessionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getStatusOfChangeNeededInfo(@PathVariable("sessionId") String sessionId) {
        return businessLogic.getStatusOfChangeNeededInfo(sessionId);
    }

//    @PostMapping(value = "/create-final-change/{socTicketId}/{sessionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String initiateCreationOfFinalChanges(@PathVariable(value = "socTicketId", required = true) String socTicketId, @PathVariable(value = "sessionId", required = true) String sessionId) {
//        UserSession session = (UserSession) RequestContextHolder.currentRequestAttributes().getAttribute(IConstant.USER_SESSION_REQ_ATTR_KEY, RequestAttributes.SCOPE_REQUEST);
//        ObjectCreationTask task = new ObjectCreationTask();
//        task.setSessionId(sessionId);
//        task.setSocTicketId(socTicketId);
//        taskQueue.addTask(task);
//        return "{\"message\": \"acknowledged\"}";
//    }
}