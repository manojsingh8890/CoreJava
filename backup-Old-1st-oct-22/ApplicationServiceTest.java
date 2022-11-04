package com.ibm.sec.service;

import com.ibm.sec.model.UserSession;
import com.ibm.sec.util.RestUtil;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService service;
    @MockBean
    private RestUtil restUtil;
    //@MockBean
    private UserSession session;

    @Test
    public void testServices() {
        when(restUtil.makeGetCallToMSSAPIs(any(),  anyString(), any(Class.class), anyString())).thenReturn(Mono.just("ABC"));
        assertEquals("ABC", service.getDeviceEnablement(session, "").block());
        assertEquals("ABC", service.getDevicesWithHostName(session, "").block());
    }

    @Test
    public void testgetDeviceEnablement(){
        UserSession s = new UserSession();
        s.setSessionId("001");
        when(restUtil.makeGetCallToMSSAPIs(any(),  anyString(), any(Class.class), anyString())).thenReturn(Mono.just("ABC"));
        assertEquals("ABC", service.getDeviceEnablement(s, "").block());
    }
    @Test
    public void testgetDevicesWithHostName(){
        UserSession s = new UserSession();
        s.setSessionId("001");
        when(restUtil.makeGetCallToMSSAPIs(any(),  anyString(), any(Class.class), anyString())).thenReturn(Mono.just("ABC"));
        assertEquals("ABC", service.getDeviceEnablement(s, "").block());
    }
}