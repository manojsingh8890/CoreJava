package com.ibm.sec.service;

import com.ibm.sec.model.UserSession;
import com.ibm.sec.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * A single point of contact for calling APIs.
 */
@Service
@Slf4j
public class ApplicationService {

    @Autowired
    private RestUtil restUtil;
    @Autowired
    private ServicesConfig servicesConfig;

    /**
     * Calls device enablement service and returns response
     *
     * @param deviceIds
     * @return
     */
    public Mono<String> getDeviceEnablement(UserSession session, String deviceIds) {
        return restUtil.makeGetCallToMSSAPIs(session, servicesConfig.getDeviceEnablementValuesUrl(), String.class, deviceIds);

    }

    /**
     * Input devices containing host name are fetched in this method
     *
     * @param params
     * @return
     */
    public Mono<String> getDevicesWithHostName(UserSession session, String params) {
        return restUtil.makeGetCallToMSSAPIs(session, servicesConfig.getDeviceUrlWithHostname(), String.class, params);
    }


}