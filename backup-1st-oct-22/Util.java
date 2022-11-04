package com.ibm.sec.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous utility class
 */
@Component
@Slf4j
public class Util {

    @Value("#{'${network_protocol_list}'}.split(',')")
    private List<String> validProtocolNames;

    private final String REGEX_MATCH_IP_ADDRESSES = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}";
    private final String REGEX_MATCH_CIDR = "\\d{1,2}";
    private final Pattern IP_ADDRESS_PATTERN = Pattern.compile(REGEX_MATCH_IP_ADDRESSES);
    private final Pattern CIDR_PATTERN = Pattern.compile(REGEX_MATCH_CIDR);

    private final String REGEX_RISK_REPORT_PATTERN_JSONARRAY = "\"Risk\" : \\[[^*]+\\]";
    private final String REGEX_RISK_REPORT_PATTERN_FOR_JSONOBJECT = "\"Risk\" : \\{[^*]+\\},";
    private final Pattern RISK_REPORT_PATTERN_JSONARRAY = Pattern.compile(REGEX_RISK_REPORT_PATTERN_JSONARRAY);
    private final Pattern RISK_REPORT_PATTERN_JSONOBJECT = Pattern.compile(REGEX_RISK_REPORT_PATTERN_FOR_JSONOBJECT);
    
    private final String REGEX_RISK_ANALYSIS_DATE_PATTERN = "\"Date\" :[^*?]+\"";
    private final Pattern RISK_ANALYSIS_DATE_PATTERN = Pattern.compile(REGEX_RISK_ANALYSIS_DATE_PATTERN);

    public boolean isIpValid(String ipAddress) {
        if(StringUtils.isEmpty(ipAddress)) {
            return false;
        }
        String[] ipAddressParts = ipAddress.split("/");
        if(ipAddressParts.length > 2) {
            return false;
        }
        String ipAddressPart1 = ipAddressParts[0];
        boolean firstPartValid = IConstant.ANY.equalsIgnoreCase(ipAddressPart1) || IP_ADDRESS_PATTERN.matcher(ipAddressPart1).matches();
        if(ipAddressParts.length > 1) {
            return firstPartValid && CIDR_PATTERN.matcher(ipAddressParts[1]).matches();
        } else {
            return firstPartValid;
        }
    }

    public String listToCommaSeparated(List<String> values) {
        StringBuilder valuesCommaSeparated = new StringBuilder();
        values.forEach(value -> valuesCommaSeparated.append(value).append(','));
        if(valuesCommaSeparated.length() > 0) {
            valuesCommaSeparated.deleteCharAt(valuesCommaSeparated.length()-1);
        }
        return valuesCommaSeparated.toString();
    }

    public List<String> jsonArrayToList(ArrayNode arrayNode) {
        List<String> devicesNames = new ArrayList<>();
        for (JsonNode jsonNode : arrayNode) {
            devicesNames.add(jsonNode.asText());
        }
        return devicesNames;
    }

    public boolean isProtocolNameValid(String protocolName) {
        return validProtocolNames.contains(protocolName.toLowerCase());
    }

    public boolean isPortNumberValid(String port) {
        if(IConstant.ANY.equalsIgnoreCase(port)) {
            return true;
        }
        int portNumber = 0;
        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            log.error(String.format("Port %s is not an integer value", port));
            return false;
        }
        return portNumber >= IConstant.MIN_PORT_NUMBER && portNumber <= IConstant.MAX_PORT_NUMBER;
    }

    /**
     * Regex Matcher for string contents
     *
     *
     * @param content
     * @param pattern
     * @return
     */
    private String findMatch(String content, Pattern pattern) {
        String matchedResult = "";
        if(content != null) {
            // Now create matcher object.
            Matcher m = pattern.matcher(content);
            if (m.find()) {
                matchedResult =  m.group(0);
            }
        }
        return  matchedResult;
    }

    public String riskReportRegexMatch(String inputString) {
        String riskData = findMatch(inputString, RISK_REPORT_PATTERN_JSONARRAY);
        if("".equals(riskData)) {
        	riskData = findMatch(inputString, RISK_REPORT_PATTERN_JSONOBJECT);
        	if (riskData != "" && riskData.endsWith(",")) {
        		riskData = riskData.substring(0, riskData.length() - 1);
        	}
        }
        return riskData;
    }
    
    public String riskAnalysisDateRegexMatch(String inputString) {
        return findMatch(inputString, RISK_ANALYSIS_DATE_PATTERN);
    }

    /**
     * Returns a unqiue alphanumeric text that can be used as any unqiue identifier.
     * @return
     */
    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
