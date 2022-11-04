package com.ibm.sec.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sec.dao.FirewallChangeDAO;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.*;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import com.ibm.sec.util.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sec.TestData;
import com.ibm.sec.error.BusinessLogicException;
import org.springframework.http.HttpStatus;


@SpringBootTest
public class ServiceFacadeTest {
    @Autowired
    private ServiceFacade facade;
    @MockBean
    private AlgosecService algosecService;
	@MockBean
	private FirewallChangeDAO firewallChangeDAO;
	@Autowired
	private ErrorConfig errorConfig;
	@MockBean
	private RuleRemovalRequester ruleRemovalRequester;
    @MockBean
    private TrafficChangeRequester trafficChangeRequester;
    @MockBean
    private DeviceService deviceService;
    
    @Autowired
    private Util util;
//    @Autowired
//    private ObjectsResolver objectsResolver;
    @Autowired
    private JsonUtil jsonUtil;

	private static Devices devices;
    private static JsonNode firewallChanges;
    private static UserSession session;
    private static Map<String, Map<String, String>> deviceNameNetworkObjectDetailsMapAsInAlgosec = new HashMap<>();
    private static Map<String, Map<String, List<String>>> deviceNameServiceObjectDetailsMap = new HashMap<>();

	@BeforeAll
	public static void setup() throws IOException {
        firewallChanges = new ObjectMapper().readTree(Thread.currentThread().getContextClassLoader().getResource("test_input.json"));
        devices = new Devices();
        devices.getRemedyDeviceNameAlgosecNameMap().put("atl_msslab_R8040_fw", "atl_msslab_R8040_fw");
        session = new UserSession();
        session.setSessionId("some_random_session_id");

        Map<String, String> networkObjectNameValueMap = new HashMap<>();
        networkObjectNameValueMap.put("networkObjectInAlgosec1", "5.5.5.5");
        networkObjectNameValueMap.put("networkObjectInAlgosec2", "6.6.6.6");
        networkObjectNameValueMap.put("networkObjectInAlgosec3", "5.5.5.5,networkObjectInAlgosec2");
        deviceNameNetworkObjectDetailsMapAsInAlgosec.put("atl_msslab_R8040_fw", networkObjectNameValueMap);

        Map<String, List<String>> serviceObjectNameValueMap = new HashMap<>();
        serviceObjectNameValueMap.put("serviceObjectInAlgosec1", Arrays.asList("tcp/443"));
        serviceObjectNameValueMap.put("serviceObjectInAlgosec2", Arrays.asList("udp/333"));
        serviceObjectNameValueMap.put("serviceObjectInAlgosec3", Arrays.asList("tcp/443","serviceObjectInAlgosec2"));
        deviceNameServiceObjectDetailsMap.put("atl_msslab_R8040_fw", serviceObjectNameValueMap);
	}

    @Test
    public void testSaveChangeRequest() throws JsonProcessingException {
        when(firewallChangeDAO.saveRequest(anyString(), anyString())).thenReturn(null);
        String sessionId = facade.saveChangeRequest("change request");
        assertNotNull(sessionId);
    }

    @Test
    public void testNoAlgosecActionableWorkError() throws JsonProcessingException {
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.createChange(jsonUtil.newNode(), devices, IConstant.AlgosecChangeRententionType.TEMPORARY, session);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getCode()));
        assertEquals("No algosec work found", ex.getMessage());
    }

    @Test
    public void testCreateTemporaryChangeSuccess() throws JsonProcessingException {
        createTempChangeCommonSetup();

        when(firewallChangeDAO.saveAll(argThat(new ArgumentMatcher<List<FirewallChangeEntity>>() {
            @Override
            public boolean matches(List<FirewallChangeEntity> entities) {
                if(entities.get(0).getChangeId().equals("111") && entities.get(1).getChangeId().equals("222")) {
                    return true;
                } else {
                    throw new AssertionError();
                }
            }
        }))).thenReturn(null);

        List<AlgosecChangeId> changeIds = facade.createChange(firewallChanges, devices, IConstant.AlgosecChangeRententionType.TEMPORARY, session);
        assertEquals(2, changeIds.size());
        List<String> actualChangeIds = changeIds.stream().map(AlgosecChangeId::getChangeId).collect(Collectors.toList());
        assertEquals(Arrays.asList("111", "222"), actualChangeIds);
    }

    @Test
    public void testCreateTemporaryChangeDatabaseError() throws JsonProcessingException {
        createTempChangeCommonSetup();

        when(firewallChangeDAO.saveAll(anyList())).thenThrow(new PersistenceException());

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.createChange(firewallChanges, devices, IConstant.AlgosecChangeRententionType.TEMPORARY, session);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getCode()));
        assertEquals("Internal Error", ex.getMessage());
    }

    private void createTempChangeCommonSetup() throws JsonProcessingException {
        when(algosecService.getNetworkObjectIPAddresesByDeviceNames(anyList(), any(UserSession.class))).thenReturn(deviceNameNetworkObjectDetailsMapAsInAlgosec);
        when(algosecService.getServiceObjectValuesByDeviceEntityName(anyList(), any(UserSession.class))).thenReturn(deviceNameServiceObjectDetailsMap);
        AlgosecChangeId trafficChangeReqId = new AlgosecChangeId();
        trafficChangeReqId.setChangeId("111");
        trafficChangeReqId.setChangeType(IConstant.AlgosecChangeType.POLICY_CREATE);
        when(trafficChangeRequester.createChangeRequest(any(JsonNode.class), any(Devices.class), eq(IConstant.AlgosecChangeRententionType.TEMPORARY), any(ObjectsData.class), any(UserSession.class))).thenReturn(Arrays.asList(trafficChangeReqId));
        AlgosecChangeId ruleDeleteChangeReqId = new AlgosecChangeId();
        ruleDeleteChangeReqId.setChangeId("222");
        ruleDeleteChangeReqId.setChangeType(IConstant.AlgosecChangeType.POLICY_DELETE);
        when(ruleRemovalRequester.createChangeRequest(any(JsonNode.class), any(Devices.class), eq(IConstant.AlgosecChangeRententionType.TEMPORARY), any(ObjectsData.class), any(UserSession.class))).thenReturn(Arrays.asList(ruleDeleteChangeReqId));
    }

    @Test
    public void testUpdateStatusFieldSuccess() {
        updateStatusFieldCommonTestSetup();
        when(firewallChangeDAO.updateStatus(anyList(), eq(IConstant.AlgosecChangeStatus.DISCARDED))).thenReturn(null);
        AlgosecUpdateStatusResponse response = facade.updateStatusField(session);
        assertEquals(Arrays.asList("1", "2"), response.getStatusUpdated());
        assertEquals(Arrays.asList("3"), response.getStatusNotUpdated());
    }

    @Test
    public void testUpdateStatusFieldDatabaseError() {
        updateStatusFieldCommonTestSetup();
        when(firewallChangeDAO.updateStatus(anyList(), eq(IConstant.AlgosecChangeStatus.DISCARDED))).thenThrow(new PersistenceException());
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.updateStatusField(session);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getCode()));
        assertEquals("Internal Error", ex.getMessage());
        assertTrue(ex.getCause().getClass().equals(PersistenceException.class));
    }

    @Test
    public void testUpdateStatusFieldGeneralError() {
        updateStatusFieldCommonTestSetup();
        when(firewallChangeDAO.updateStatus(anyList(), eq(IConstant.AlgosecChangeStatus.DISCARDED))).thenThrow(new RuntimeException());
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.updateStatusField(session);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Integer.parseInt(ex.getCode()));
        assertEquals("Internal Error", ex.getMessage());
        assertTrue(ex.getCause().getClass().equals(RuntimeException.class));
    }

    private void updateStatusFieldCommonTestSetup() {
        FirewallChangeEntity changeEntity1 = new FirewallChangeEntity();
        changeEntity1.setChangeId("1");
        FirewallChangeEntity changeEntity2 = new FirewallChangeEntity();
        changeEntity2.setChangeId("2");
        FirewallChangeEntity changeEntity3 = new FirewallChangeEntity();
        changeEntity3.setChangeId("3");
        when(firewallChangeDAO.getAllTemporaryPolicyChangeRequestsBySessionId(anyString())).thenReturn(Arrays.asList(changeEntity1, changeEntity2, changeEntity3));
        Map<String, List<String>> apiResponseMap = new HashMap<>();
        apiResponseMap.put(IConstant.CHANGE_ID_STATUS_UPDATED, Arrays.asList("1", "2"));
        apiResponseMap.put(IConstant.CHANGE_ID_STATUS_NOT_UPDATED, Arrays.asList("3"));
        when(algosecService.updateChangeStatus(eq(Arrays.asList("1", "2", "3")), any(UserSession.class))).thenReturn(apiResponseMap);
    }

    @Test
    public void testGetAndSaveRiskInfoErrorWhenTempChangesIncomplete() {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(false);
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.getAndSaveRiskInfo(session);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getCode()));
        assertEquals("Temporary changes are still not complete for session id " + session.getSessionId(), ex.getMessage());
    }

    @Test
    public void testGetAndSaveRiskInfoErrorWhenNoChangeIdsFound() throws JsonProcessingException {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(Collections.EMPTY_LIST);
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.getAndSaveRiskInfo(session);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getCode()));
        assertEquals("No temporary policy create change ids available for session id " + session.getSessionId(), ex.getMessage());
    }

//	@Test
//	public void testCleanupFirewallNullExpected() throws Exception {
//		ServiceFacade sf = new ServiceFacade();
//		Assertions.assertThrows(NullPointerException.class, () -> {
//			sf.updateStatusField();
//		});
//	}

    @Test
    public void testGetAndSaveRiskInfoSuccess() throws Exception {
        Map<String,String> serviceResponse = new HashMap<String, String>();
        Map<String,String> deviceResponse = new HashMap<String, String>();
        Map<String,String> changeDetails = new HashMap<String, String>();
        serviceResponse.put("001", TestData.getAlgosecApiResponseForRiskReport());
        changeDetails.put("001", TestData.getTicketDetailsData());
        deviceResponse.put("001", "host001");
        List<String> changeIds = new ArrayList<String>();
        changeIds.add("ch001");
        String response = "{\"RiskReport\":[{\"Risk\":[{\"profile\":\"test\",\"name\":\"test\",\"severity\":\"low\"}],\"Date\":\" Tue Jul 26 13:04:29 2022\",\"hostName\":\"host001\"}]}";
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(algosecService.getAlgosecChangeInfo(anyList(), any(UserSession.class))).thenReturn(changeDetails);
        when(deviceService.fetchDeviceInfo(anyMap(), any(UserSession.class))).thenReturn(deviceResponse);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(changeIds);
        when(algosecService.getRiskReport(changeIds, session)).thenReturn(serviceResponse);
        assertEquals(response, facade.getAndSaveRiskInfo(session));
    }
    
    @Test
    public void testGetAndSaveRiskInfoSuccessForJsonObjectResponse() throws Exception {
        Map<String,String> serviceResponse = new HashMap<String, String>();
        Map<String,String> deviceResponse = new HashMap<String, String>();
        Map<String,String> changeDetails = new HashMap<String, String>();
        serviceResponse.put("001", TestData.getAlgosecApiResponseForRiskReportForJsonObjectFormat());
        changeDetails.put("001", TestData.getTicketDetailsData());
        deviceResponse.put("001", "host001");
        List<String> changeIds = new ArrayList<String>();
        changeIds.add("ch001");
        String response = "{\"RiskReport\":[{\"Risk\":[{\"profile\":\"test\",\"name\":\"test\",\"severity\":\"low\"}],\"Date\":\" Tue Jul 26 13:04:29 2022\",\"hostName\":\"host001\"}]}";
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(algosecService.getAlgosecChangeInfo(anyList(), any(UserSession.class))).thenReturn(changeDetails);
        when(deviceService.fetchDeviceInfo(anyMap(), any(UserSession.class))).thenReturn(deviceResponse);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(changeIds);
        when(algosecService.getRiskReport(changeIds, session)).thenReturn(serviceResponse);
        assertEquals(response, facade.getAndSaveRiskInfo(session));
    }

    @Test
    public void testGetAndSaveRiskInfoSuccessForMultipleChanges() throws Exception {
        Map<String,String> serviceResponse = new HashMap<String, String>();
        serviceResponse.put("001", TestData.getAlgosecApiResponseForRiskReport());
        serviceResponse.put("002", TestData.getAlgosecApiResponseForRiskReport());
        
       
        Map<String,String> changeDetails = new HashMap<String, String>();
        changeDetails.put("001", TestData.getTicketDetailsData());
        changeDetails.put("002", TestData.getTicketDetailsData());
        
        Map<String,String> deviceResponse = new HashMap<String, String>();
        deviceResponse.put("001", "host001");
        deviceResponse.put("002", "host001");
        
        List<String> changeIds = new ArrayList<String>();
        changeIds.add("ch001");
        changeIds.add("ch002");
        String response = "{\"RiskReport\":[{\"Risk\":[{\"profile\":\"test\",\"name\":\"test\",\"severity\":\"low\"}],\"Date\":\" Tue Jul 26 13:04:29 2022\",\"hostName\":\"host001\"},{\"Risk\":[{\"profile\":\"test\",\"name\":\"test\",\"severity\":\"low\"}],\"Date\":\" Tue Jul 26 13:04:29 2022\",\"hostName\":\"host001\"}]}";
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(algosecService.getAlgosecChangeInfo(anyList(), any(UserSession.class))).thenReturn(changeDetails);
        when(deviceService.fetchDeviceInfo(anyMap(), any(UserSession.class))).thenReturn(deviceResponse);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(changeIds);
        when(algosecService.getRiskReport(changeIds, session)).thenReturn(serviceResponse);
        assertEquals(response, facade.getAndSaveRiskInfo(session));
    }

	@Test
	public void testGetAndSaveChangeNeededInfoSuccess() throws Exception {
		List<String> changeIds = new ArrayList<String>();
        changeIds.add("694");
        List<String> result = new ArrayList<String>();
        result.add(TestData.getChangeNeededData());
        Map<String, String> ticketDetails = new HashMap<String, String>();
        ticketDetails.put("694", TestData.getTicketDetailsDataForChangeNeededInfo());
        String response = "{\"ChangeNeededInfo\":[{\"source\":\"10.0.0.5\",\"destination\":\"192.168.0.0/16\",\"service\":\"tcp/22\",\"isChangeNeeded\":true,\"queryItem\":[{\"message\":\"Partially allowed (x2)\",\"deviceName\":\"atl_msslab_R8040_fw\",\"displayName\":\"atl-msslab-R8040_fw\"}]}]}";
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(changeIds);
        when(algosecService.getAlgosecChangeInfo(anyList(), any(UserSession.class))).thenReturn(ticketDetails);
        when(algosecService.getChangeNeededInfo(anyList(), any(UserSession.class))).thenReturn(result);
        assertEquals(response, facade.getAndSaveChangeNeededInfo(session));
     }

     @Test
     public void testGetAndSaveChangeNeededInfoWhenTempChangesIncomplete() {
         when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(false);
         BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
             facade.getAndSaveChangeNeededInfo(session);
         });
         assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getHttpStatus()));
         assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getCode()));
         assertEquals("Temporary changes are still not complete for session id " + session.getSessionId(), ex.getMessage());
     }

    @Test
    public void testGetAndSaveChangeNeededInfoErrorWhenNoChangeIdsFound() throws JsonProcessingException {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getTemporaryPolicyCreateChangesBySessionId(eq(session.getSessionId()))).thenReturn(Collections.EMPTY_LIST);
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            facade.getAndSaveChangeNeededInfo(session);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getHttpStatus()));
        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(ex.getCode()));
        assertEquals("No temporary policy create change ids available for session id " + session.getSessionId(), ex.getMessage());
    }

    @Test
    public void testGetStatusOfTemporaryChangePending() {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(false);
        AlgosecTaskStatusResponse response = facade.getStatusOfTemporaryChange(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.PENDING, response.getStatus());
        assertEquals(session.getSessionId(), response.getSessionId());
    }

    @Test
    public void testGetStatusOfTemporaryChangeCompleteWithSuccess() {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.TEMPORAY_CHANGE_CREATION))).thenReturn(Collections.emptyList());
        AlgosecTaskStatusResponse response = facade.getStatusOfTemporaryChange(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS, response.getStatus());
        assertEquals(session.getSessionId(), response.getSessionId());
    }

    @Test
    public void testGetStatusOfTemporaryChangeCompleteWithError() {
        when(firewallChangeDAO.areTemporaryChangeRequestsComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.TEMPORAY_CHANGE_CREATION))).thenReturn(Arrays.asList("error1"));
        AlgosecTaskStatusResponse response = facade.getStatusOfTemporaryChange(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR, response.getStatus());
        assertEquals("error1", response.getErrors().get(0).getMessage());
        assertEquals(session.getSessionId(), response.getSessionId());
    }

    @Test
    public void testGetStatusOfFetchRiskInfoPending() throws JsonProcessingException {
        when(firewallChangeDAO.isRisksFetchComplete(anyString())).thenReturn(false);
        String response = facade.getStatusOfFetchRiskInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.PENDING.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals(session.getSessionId(),  jsonUtil.parse(response).get("SESSION_ID").asText());
    }

    @Test
    public void testGetStatusOfFetchRiskInfoCompleteWithSuccess() throws JsonProcessingException {
        when(firewallChangeDAO.isRisksFetchComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.RISK_INFO_FETCH))).thenReturn(Collections.emptyList());
        String response = facade.getStatusOfFetchRiskInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals(session.getSessionId(), jsonUtil.parse(response).get("SESSION_ID").asText());
    }

    @Test
    public void testGetStatusOfFetchRiskInfoCompleteWithError() throws JsonProcessingException {
        when(firewallChangeDAO.isRisksFetchComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.RISK_INFO_FETCH))).thenReturn(Arrays.asList("error1"));
        String response = facade.getStatusOfFetchRiskInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals("error1", jsonUtil.parse(response).get("errors").get(0).get("message").asText());
        assertEquals(session.getSessionId(), jsonUtil.parse(response).get("SESSION_ID").asText());
    }

    @Test
    public void testGetStatusOfChangeNeededInfoFetchPending() throws JsonProcessingException {
        when(firewallChangeDAO.isChangeNeededInfoFetchComplete(anyString())).thenReturn(false);
        String response = facade.getStatusOfChangeNeededInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.PENDING.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals(session.getSessionId(), jsonUtil.parse(response).get("SESSION_ID").asText());
    }

    @Test
    public void testGetStatusOfChangeNeededInfoFetchCompleteWithSuccess() throws JsonProcessingException {
        when(firewallChangeDAO.isChangeNeededInfoFetchComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.CHANGE_NEEDED_INFO_FETCH))).thenReturn(Collections.emptyList());
        String response = facade.getStatusOfChangeNeededInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_SUCCESS.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals(session.getSessionId(), jsonUtil.parse(response).get("SESSION_ID").asText());
    }

    @Test
    public void testGetStatusOfChangeNeededInfoFetchCompleteWithError() throws JsonProcessingException {
        when(firewallChangeDAO.isChangeNeededInfoFetchComplete(anyString())).thenReturn(true);
        when(firewallChangeDAO.getErrorsBySessionIdAndFunctionality(eq(session.getSessionId()), eq(IConstant.Functionality.CHANGE_NEEDED_INFO_FETCH))).thenReturn(Arrays.asList("error1"));
        String response = facade.getStatusOfChangeNeededInfo(session.getSessionId());
        assertEquals(IConstant.AlgosecTaskStatus.COMPLETE_WITH_ERROR.toString(), jsonUtil.parse(response).get("status").asText());
        assertEquals("error1", jsonUtil.parse(response).get("errors").get(0).get("message").asText());
        assertEquals(session.getSessionId(), jsonUtil.parse(response).get("SESSION_ID").asText());
    }
}
