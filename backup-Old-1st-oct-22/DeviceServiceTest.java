package com.ibm.sec.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ibm.sec.error.BusinessLogicException;
import com.ibm.sec.error.ErrorConfig;
import com.ibm.sec.model.Claims;
import com.ibm.sec.model.Devices;
import com.ibm.sec.model.UserSession;
import com.ibm.sec.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeviceServiceTest {
    @Autowired
    private DeviceService deviceService;
    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private AlgosecService algosecService;
    @Autowired
    private ErrorConfig errorConfig;
    @Autowired
    private JsonUtil jsonUtil;

    private UserSession session;

    @BeforeEach
    public void createUserSession() {
        session = new UserSession();
        session.setSessionId("abc");
        session.setAuthHeaderValue("def");
        session.setAlgoSecFaSessionId("ghi");
        session.setAlgoSecSessionId("jkl");
        session.setAlgoSecPhpSessionId("mno");
        session.setThreadUuid("pqr");
        Claims claims = new Claims();
        claims.setCustomerId("123");
        claims.setUserName("stu");
        session.setClaims(claims);
    }

    @Test
    public void testInitSucces() throws JsonProcessingException {
        when(applicationService.getDevicesWithHostName(any(), eq("x,y"))).thenReturn(Mono.just("[\n" +
                "{\"id\": \"a\", \"algoSecUniqueName\": \"algosec1\", \"hostName\": \"device1\"},\n" +
                "{\"id\": \"b\", \"algoSecUniqueName\": \"algosec2\", \"hostName\": \"device2\"}\n" +
                "]"));
        when(applicationService.getDeviceEnablement(any(), eq("a,b"))).thenReturn(Mono.just("[\n" +
                "\"p\", \"q\"\n" +
                "]"));
        String data = "{\"Policy Update\": [{\"firewall_policy\": \"x\"}, {\"firewall_policy\": \"y\"}]}";
        JsonNode dataNode = jsonUtil.parse(data);
        Devices devices = deviceService.fetchDeviceInfo(dataNode, session);
        assertEquals(Arrays.asList("x", "y"), devices.getRemedyDeviceNames());
        assertEquals(Arrays.asList("algosec2", "algosec1"), new ArrayList<>(devices.getRemedyDeviceNameAlgosecNameMap().values()));
    }

    @Test
    public void testInitFailureNoDeviceFound() throws JsonProcessingException {
        when(applicationService.getDevicesWithHostName(any(), eq("x,y"))).thenReturn(Mono.just("[]"));
        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            String data = "{\"Policy Update\": [{\"firewall_policy\": \"x\"}, {\"firewall_policy\": \"y\"}]}";
            JsonNode dataNode = jsonUtil.parse(data);
            deviceService.fetchDeviceInfo(dataNode, session);
        });
        assertEquals(errorConfig.getCode().getBadRequest(), thrown.getCode());
        assertEquals(errorConfig.getMessage().getNoDeviceFound(), thrown.getMessage());
        assertEquals(errorConfig.getHttpStatus().getBadRequest(), thrown.getHttpStatus());
    }

    @Test
    public void testInitFailureIncorrectDeviceCount() {
        when(applicationService.getDevicesWithHostName(any(), eq("x,y"))).thenReturn(Mono.just("[\n" +
                "{\"id\": \"a\"}\n" +
                "]"));
        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            String data = "{\"Policy Update\": [{\"firewall_policy\": \"x\"}, {\"firewall_policy\": \"y\"}]}";
            JsonNode dataNode = jsonUtil.parse(data);
            deviceService.fetchDeviceInfo(dataNode, session);
        });
        assertEquals(errorConfig.getCode().getBadRequest(), thrown.getCode());
        assertEquals(errorConfig.getMessage().getIncorrectDeviceCount(), thrown.getMessage());
        assertEquals(errorConfig.getHttpStatus().getBadRequest(), thrown.getHttpStatus());
    }

    @Test
    public void testInitFailureNoAlgosecEnabledDeviceFound() {
        when(applicationService.getDevicesWithHostName(any(), eq("x,y"))).thenReturn(Mono.just("[\n" +
                "{\"id\": \"a\"},\n" +
                "{\"id\": \"b\"}\n" +
                "]"));
        when(applicationService.getDeviceEnablement(any(), eq("a,b"))).thenReturn(Mono.just("[]"));
        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            String data = "{\"Object Update\": [{\"firewall_policy\": \"x\"}, {\"firewall_policy\": \"y\"}]}";
            JsonNode dataNode = jsonUtil.parse(data);
            deviceService.fetchDeviceInfo(dataNode, session);
        });
        assertEquals(errorConfig.getCode().getBadRequest(), thrown.getCode());
        assertEquals(errorConfig.getMessage().getNoAlgosecEnabledDeviceFound(), thrown.getMessage());
        assertEquals(errorConfig.getHttpStatus().getBadRequest(), thrown.getHttpStatus());
    }
}