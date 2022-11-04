package com.ibm.sec.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibm.sec.dao.FirewallChangeDAO;
import com.ibm.sec.model.AlgosecTaskStatusResponse;
import com.ibm.sec.model.Devices;
import com.ibm.sec.model.UserSession;
import com.ibm.sec.service.DeviceService;
import com.ibm.sec.service.ServiceFacade;
import com.ibm.sec.util.IConstant;
import com.ibm.sec.util.JsonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AlgosecIntegrationControllerTest {

	public static final String TEST_SESSION_ID = "some_session_id";
	@MockBean
	private FirewallChangeDAO firewallChangeDAO;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ServiceFacade businessLogic;

	@MockBean
	private DeviceService deviceService;

	@Autowired
	private JsonUtil jsonUtil;

	private static UserSession session;

	@BeforeAll
	public static void setUp() {
		session = new UserSession();

		MockedStatic<RequestContextHolder> mockedRCHolder = mockStatic(RequestContextHolder.class);
		RequestAttributes mockedRCAttributes = mock(RequestAttributes.class);
		mockedRCHolder.when(() -> RequestContextHolder.currentRequestAttributes()).thenReturn(mockedRCAttributes);
		when(mockedRCAttributes.getAttribute(eq(IConstant.USER_SESSION_REQ_ATTR_KEY), eq(RequestAttributes.SCOPE_REQUEST))).thenReturn(session);
	}

	@Test
	public void testCreateTemporaryChangeRequests() throws Exception {
		when(businessLogic.saveChangeRequest(anyString())).thenReturn(TEST_SESSION_ID);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/algosec-integration/submit-task/create-temporary-change").contentType(MediaType.APPLICATION_JSON).content("{}");
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"SESSION_ID\":\"some_session_id\"}")).andReturn();
	}

//	@Test
//	public void testCreateTemporaryChangeRequestsError() throws Exception {
//		when(businessLogic.saveChangeRequest(anyString())).thenReturn(TEST_SESSION_ID);
//		when(businessLogic.createChange(any(), any(), any(), any())).thenThrow(new RuntimeException());
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/algosec-integration/submit-task/create-temporary-change").contentType(MediaType.APPLICATION_JSON).content("{}");
//		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"SESSION_ID\":\"some_session_id\"}")).andReturn();
//	}

	@Test
	public void testGetStatusOfTemporaryChange() throws Exception {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(TEST_SESSION_ID);
		when(businessLogic.getStatusOfTemporaryChange(anyString())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/fetch-task-status/temporary-change/" + TEST_SESSION_ID);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"SESSION_ID\":\"some_session_id\"}")).andReturn();
	}

	@Test
	public void testStartFetchRiskInfo() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/algosec-integration/submit-task/fetch-risk-info/" + TEST_SESSION_ID);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"message\": \"acknowledged\"}")).andReturn();
	}

	@Test
	public void testGetStatusOfFetchRiskInfo() throws Exception {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(TEST_SESSION_ID);
		when(businessLogic.getStatusOfFetchRiskInfo(anyString())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/fetch-task-status/risk-info/" + TEST_SESSION_ID);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"SESSION_ID\":\"some_session_id\"}")).andReturn();
	}

	@Test
	public void testStartFetchChangeNeededInfo() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/algosec-integration/submit-task/fetch-change-needed-info/" + TEST_SESSION_ID);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"message\": \"acknowledged\"}")).andReturn();
	}

	@Test
	public void testGetStatusOfChangeNeededInfo() throws Exception {
		AlgosecTaskStatusResponse response = new AlgosecTaskStatusResponse();
		response.setSessionId(TEST_SESSION_ID);
		when(businessLogic.getStatusOfChangeNeededInfo(anyString())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/fetch-task-status/change-needed-info/" + TEST_SESSION_ID);
		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.content().json("{\"SESSION_ID\":\"some_session_id\"}")).andReturn();
	}
//	@Test
//	public void testCleanupFirewallChanges() throws Exception {
//		List<FirewallChangeEntity> fwList = new ArrayList();
//		FirewallChangeEntity fwChange = new FirewallChangeEntity();
//		fwChange.setId(2);
//		fwChange.setItsmId("001");
//		fwChange.setAlgoSecChangeStatus(IConstant.AlgosecChangeStatus.NOT_APPLICABLE);
//		fwChange.setChangeId("ch001");
//		fwList.add(fwChange);
//
//		List<String> changeIds = new ArrayList();
//		changeIds.add("ch001");
//		Map<String, List<String>> returnApiResponse = new HashMap<>();
//		returnApiResponse.put(IConstant.CHANGE_ID_STATUS_UPDATED, changeIds);
//		when(firewallChangeDAO.getAllTemporaryPolicyChangeRequestsBySessionId(anyString())).thenReturn(fwList);
//		when(session.getAlgoSecSessionId()).thenReturn("0001");
//		when(service.updateChangeStatus(changeIds, session)).thenReturn(returnApiResponse);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/algosec-integration/cleanup").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
//	}
//
//	@Test
//	public void testCleanupFirewallChanges2() throws Exception {
//		List<FirewallChangeEntity> fwList = new ArrayList();
//		FirewallChangeEntity fwChange = new FirewallChangeEntity();
//		fwChange.setId(2);
//		fwChange.setItsmId("001");
//		fwChange.setAlgoSecChangeStatus(IConstant.AlgosecChangeStatus.NOT_APPLICABLE);
//		fwChange.setChangeId("ch001");
//		fwList.add(fwChange);
//		List<String> changeIds = new ArrayList();
//		changeIds.add("ch001");
//		Map<String, List<String>> returnApiResponse = new HashMap<>();
//		returnApiResponse.put(IConstant.CHANGE_ID_STATUS_NOT_UPDATED, changeIds);
//		when(firewallChangeDAO.getAllTemporaryPolicyChangeRequestsBySessionId(anyString())).thenReturn(fwList);
//		when(session.getAlgoSecSessionId()).thenReturn("0001");
//		when(service.updateChangeStatus(changeIds, session)).thenReturn(returnApiResponse);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/algosec-integration/cleanup").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
//	}
//
//	@Test
//	public void testCleanupFirewallChanges3() throws Exception {
//		List<FirewallChangeEntity> fwList = new ArrayList();
//		FirewallChangeEntity fwChange = new FirewallChangeEntity();
//		fwChange.setId(2);
//		fwChange.setItsmId("001");
//		fwChange.setAlgoSecChangeStatus(IConstant.AlgosecChangeStatus.NOT_APPLICABLE);
//		fwChange.setChangeId("ch001");
//		fwList.add(fwChange);
//
//		List<String> changeIds = new ArrayList();
//		changeIds.add("ch001");
//		Map<String, List<String>> returnApiResponse = new HashMap<>();
//		returnApiResponse.put(IConstant.CHANGE_ID_STATUS_NOT_UPDATED, changeIds);
//		returnApiResponse.put(IConstant.CHANGE_ID_STATUS_UPDATED, changeIds);
//		when(firewallChangeDAO.getAllTemporaryPolicyChangeRequestsBySessionId(anyString())).thenReturn(fwList);
//		when(session.getAlgoSecSessionId()).thenReturn("0001");
//		when(service.updateChangeStatus(changeIds, session)).thenReturn(returnApiResponse);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/algosec-integration/cleanup").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
//	}
	
//	@Test
//	public void testRiskReportEndPoint() throws Exception {
//		when(session.getAlgoSecSessionId()).thenReturn("001");
//		when(businessLogic.getAndSaveRiskInfo(any())).thenReturn(TestData.getRiskReportRespData());
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/risk-report?changeId=63").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.RiskReport").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.RiskReport.63").exists())
//				.andReturn();
//
//	}

//	@Test
//	public void testRiskReportEndPointForTwoChangeId() throws Exception {
//		when(session.getAlgoSecSessionId()).thenReturn("001");
//		when(businessLogic.getAndSaveRiskInfo(any())).thenReturn(TestData.getRiskReportRespDataForTwoChangeId());
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/risk-report?changeId=471&changeId=63").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.RiskReport").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.RiskReport.471").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.RiskReport.63").exists())
//				.andReturn();
//
//	}
	
//	@Test
//	public void testLinkITSM() throws Exception {
//		FirewallChangeEntity fwChange = new FirewallChangeEntity();
//		fwChange.setId(2);
//		fwChange.setItsmId("001");
//		fwChange.setStatus("NEW");
//		fwChange.setChangeId("ch001");
//
//		when(firewallChangeDAO.loadFirewallChangeByChangeId(anyString())).thenReturn(fwChange);
//		when(firewallChangeDAO.save(fwChange)).thenReturn(fwChange);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/algosec-integration/LinkITSM").accept(MediaType.APPLICATION_JSON).param("itsmId", "002").param("changeId","Ch001");
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
//	}

//	@Test
//	public void testUpdateFirewallChangeStatus() throws Exception {
//		AlgosecUpdateStatusResponse algosecUpdateStatusResponse = new AlgosecUpdateStatusResponse();
//		when(businessLogic.updateStatusField(any())).thenReturn(algosecUpdateStatusResponse);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/algosec-integration/cleanup");
//		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//	}

//	@Test
//	public void testCreateChange() throws Exception {
//		AlgosecChangeResponse response = new AlgosecChangeResponse();
//		String testData = "{\"devices\": [\"x\", \"y\"], \"data\": {}}";
//		doNothing().when(deviceService).fetchDeviceInfo(any(JsonNode.class));
//		when(businessLogic.createChange(any(JsonNode.class), any(Devices.class), any(IConstant.AlgosecChangeRententionType.class), any(UserSession.class))).thenReturn(response);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/algosec-integration").contentType(MediaType.APPLICATION_JSON).content(testData);
//		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//	}

//	@Test
//	public void testChangeNeededInfoEndPoint() throws Exception {
//		when(session.getAlgoSecSessionId()).thenReturn("001");
//        when(businessLogic.getAndSaveChangeNeededInfo(any())).thenReturn(TestData.getChangeNeededInfoRespData());
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/change-needed-info?changeId=471").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.ChangeNeededInfo").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.ChangeNeededInfo.471").exists())
//				.andReturn();
//	}

//	@Test
//	public void testChangeNeededInfoEndPointForTwoChangeId() throws Exception {
//		when(session.getAlgoSecSessionId()).thenReturn("001");
//		when(businessLogic.getAndSaveChangeNeededInfo(any())).thenReturn(TestData.getChangeNeededInfoRespDataForTwoChangeId());
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/algosec-integration/change-needed-info?changeId=471&changeId=63").accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(requestBuilder)
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.ChangeNeededInfo").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.ChangeNeededInfo.471").exists())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.ChangeNeededInfo.63").exists())
//				.andReturn();
//	}
}
