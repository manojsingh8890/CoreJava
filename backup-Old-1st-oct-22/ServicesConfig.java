package com.ibm.sec.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * A configuration for calling services.
 */
@Component
@ConfigurationProperties(prefix = "app.service.url")
@Getter
@Setter
@NoArgsConstructor
public class ServicesConfig {
    private String algosecFireflowAuthenticationUrl;
    private String algosecFireflowChangeRequestUrl;
    private String algosecFireflowRiskReportUrl;
    private String algosecDeviceMappingUrl;
    private String deviceEnablementValuesUrl;
    private String deviceUrlWithHostname;
    private String algosecFindNetworkObjectsByDeviceName;
    private String algosecFindServiceObjectsByRemedyDeviceId;
    private String algosecTrafficSimulationUrl;
    private String algosecRulesByDeviceNameUrl;
    private String algosecFireflowRuleRemovalRequestUrl;
    private String algosecFireflowObjectChangeRequestUrl;
}
