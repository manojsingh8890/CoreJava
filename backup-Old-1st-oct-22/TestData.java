package com.ibm.sec;

public class TestData {

	public static String getTicketDetailsData() {
		return "{\"status\":\"Success\",\"messages\":[],\"data\":{\"id\":694,\"subChangeRequests\":[695],\"fields\":[{\"name\":\"Owner\",\"values\":[\"admin<XXX@us.ibm.com>\"]},{\"name\":\"Creator\",\"values\":[\"mss-ms<mss-ms@local.local>\"]},{\"name\":\"Devices\",\"values\":[\"atl_msslab_R8040_fw\"]},{\"name\":\"Ticket Template Name\",\"values\":[\"SOC Standard Request Template\"]},{\"name\":\"DAISY\",\"values\":[\"optimized\"]},{\"name\":\"LastUpdated\",\"values\":[\"2022-09-08 08:09:17\"]},{\"name\":\"Requestor\",\"values\":[\"mss-ms<mss-ms@local.local>\"]},{\"name\":\"Form Type\",\"values\":[\"Traffic Change\"]},{\"name\":\"Initial Plan status\",\"values\":[\"Result OK\"]},{\"name\":\"Workflow\",\"values\":[\"SOC Optimized Workflow\"]},{\"name\":\"Subject\",\"values\":[\"Manoj TEST\"]},{\"name\":\"status\",\"values\":[\"open\"]}],\"originalTraffic\":[{\"source\":{\"items\":[{\"value\":\"10.0.0.5\"}]},\"destination\":{\"items\":[{\"value\":\"192.168.0.0/16\"}]},\"service\":{\"items\":[{\"value\":\"tcp/22\"}]},\"application\":{\"items\":[{\"value\":\"any\"}]},\"user\":{\"items\":[{\"value\":\"any\"}]},\"fields\":[{\"name\":\"Requested URL Category\",\"values\":[\"any\"]}],\"action\":\"Allow\"}],\"plannedTraffic\":[{\"source\":{\"items\":[{\"value\":\"10.0.0.5\"}]},\"destination\":{\"items\":[{\"value\":\"192.168.0.0/16\"}]},\"service\":{\"items\":[{\"value\":\"tcp/22\"}]},\"application\":{\"items\":[{\"value\":\"any\"}]},\"user\":{\"items\":[{\"value\":\"any\"}]},\"fields\":[{\"name\":\"Change URL Category\",\"values\":[\"any\"]}],\"action\":\"Allow\"}]}}";
	}

	public static String getChangeNeededData() {
		return "{\n"
				+ "  \"queryUIResult\" : \"https://10.239.0.10/fa/query/results/#/work/ALL_FIREWALLS_query-1662627826112/\",\n"
				+ "  \"queryResult\" : [ {\n"
				+ "    \"queryDescription\" : \"10.0.0.5=>192.168.0.0/16:tcp/22:any:any\",\n"
				+ "    \"fipResult\" : \"Routed\",\n"
				+ "    \"finalResult\" : \"Partially allowed\",\n"
				+ "    \"queryHTMLPath\" : \"https://10.239.0.10/fa/query/results/#/work/ALL_FIREWALLS_query-1662627826112/\",\n"
				+ "    \"devicesInPath\" : [ [ {\n"
				+ "      \"name\" : \"atl_msslab_R8040_fw\",\n"
				+ "      \"displayName\" : \"atl-msslab-R8040_fw\"\n"
				+ "    } ] ],\n"
				+ "    \"queryItem\" : [ {\n"
				+ "      \"isAllowed\" : \"Partially allowed (x2)\",\n"
				+ "      \"deviceName\" : \"atl_msslab_R8040_fw\",\n"
				+ "      \"displayName\" : \"atl-msslab-R8040_fw\",\n"
				+ "      \"rules\" : [ {\n"
				+ "        \"ruleName\" : \"Allow all\",\n"
				+ "        \"service\" : [ \"ssh\", \"https\", \"icmp-proto\", \"http\" ],\n"
				+ "        \"source\" : [ \"Any\" ],\n"
				+ "        \"destination\" : [ \"Any\" ],\n"
				+ "        \"install\" : [ \"Policy Targets\" ],\n"
				+ "        \"action\" : \"Accept\",\n"
				+ "        \"rule_id\" : \"76FD22F2-EFA8-4C81-A617-40201D3F5C4E\"\n"
				+ "      }, {\n"
				+ "        \"service\" : [ \"Any\" ],\n"
				+ "        \"source\" : [ \"Any\" ],\n"
				+ "        \"destination\" : [ \"atl-msslab-R8040_fw\", \"atl-msslab-R8040_mgmt\" ],\n"
				+ "        \"install\" : [ \"Policy Targets\" ],\n"
				+ "        \"action\" : \"Accept\",\n"
				+ "        \"rule_id\" : \"D978125E-0473-495C-ADCF-F1BD262EAB0A\"\n"
				+ "      } ]\n"
				+ "    } ]\n"
				+ "  } ]\n"
				+ "}";
	}

	public static String getRiskReportData() {

		return "CF.{Risk_API_Details}: {\n"
				+ "       \"RisksURLs\" : \"firewalls/afa-1221/riskCheck_3_fireflow63/risks.html\",\n"
				+ "       \"Risk\" : [\n"
				+ "          {\n"
				+ "             \"profile\" : \"Standard\",\n"
				+ "             \"name\" : \"I01-inbound-any\",\n"
				+ "             \"severity\" : \"high\",\n"
				+ "             \"description\" : \"\\\"Any\\\" service can enter your network\",\n"
				+ "             \"devices\" : \"206.253.229.197,206.253.229.198\",\n"
				+ "             \"code\" : \"I01\"\n"
				+ "          },\n"
				+ "          {\n"
				+ "             \"profile\" : \"Standard\",\n"
				+ "             \"name\" : \"I25-inbound-http\",\n"
				+ "             \"severity\" : \"suspected high\",\n"
				+ "             \"description\" : \"HTTP/HTTPS can enter your network\",\n"
				+ "             \"devices\" : \"206.253.229.197,206.253.229.198\",\n"
				+ "             \"code\" : \"I25\"\n"
				+ "          },\n"
				+ "          {\n"
				+ "             \"profile\" : \"Standard\",\n"
				+ "             \"name\" : \"R08-rule-allsrv\",\n"
				+ "             \"severity\" : \"medium\",\n"
				+ "             \"description\" : \"\\\"Allow Any service\\\" rules\",\n"
				+ "             \"devices\" : \"206.253.229.197,206.253.229.198\",\n"
				+ "             \"code\" : \"R08\"\n"
				+ "          },\n"
				+ "          {\n"
				+ "             \"profile\" : \"Standard\",\n"
				+ "             \"name\" : \"I26-inbound-ftp\",\n"
				+ "             \"severity\" : \"medium\",\n"
				+ "             \"description\" : \"FTP can enter your network\",\n"
				+ "             \"devices\" : \"206.253.229.197,206.253.229.198\",\n"
				+ "             \"code\" : \"I26\"\n"
				+ "          }\n"
				+ "       ],\n"
				+ "       \"Date\" : \"Fri Nov 19 05:42:06 2021\"\n"
				+ "    }\n"
				+ "\n"
				+ "\n"
				+ "CF.{MSS Ticket}: ";
	}

	public static String getTicketDetailsDataForChangeNeededInfo() {

		return "{\"status\":\"Success\",\"messages\":[],\"data\":{\"id\":694,\"subChangeRequests\":[695],\"fields\":[{\"name\":\"Owner\",\"values\":[\"admin<XXX@us.ibm.com>\"]},{\"name\":\"Creator\",\"values\":[\"mss-ms<mss-ms@local.local>\"]},{\"name\":\"Devices\",\"values\":[\"atl_msslab_R8040_fw\"]},{\"name\":\"Ticket Template Name\",\"values\":[\"SOC Standard Request Template\"]},{\"name\":\"DAISY\",\"values\":[\"optimized\"]},{\"name\":\"LastUpdated\",\"values\":[\"2022-09-08 08:09:17\"]},{\"name\":\"Requestor\",\"values\":[\"mss-ms<mss-ms@local.local>\"]},{\"name\":\"Form Type\",\"values\":[\"Traffic Change\"]},{\"name\":\"Initial Plan status\",\"values\":[\"Result OK\"]},{\"name\":\"Workflow\",\"values\":[\"SOC Optimized Workflow\"]},{\"name\":\"Subject\",\"values\":[\"Manoj TEST\"]},{\"name\":\"status\",\"values\":[\"open\"]}],\"originalTraffic\":[{\"source\":{\"items\":[{\"value\":\"10.0.0.5\"}]},\"destination\":{\"items\":[{\"value\":\"192.168.0.0/16\"}]},\"service\":{\"items\":[{\"value\":\"tcp/22\"}]},\"application\":{\"items\":[{\"value\":\"any\"}]},\"user\":{\"items\":[{\"value\":\"any\"}]},\"fields\":[{\"name\":\"Requested URL Category\",\"values\":[\"any\"]}],\"action\":\"Allow\"}],\"plannedTraffic\":[{\"source\":{\"items\":[{\"value\":\"10.0.0.5\"}]},\"destination\":{\"items\":[{\"value\":\"192.168.0.0/16\"}]},\"service\":{\"items\":[{\"value\":\"tcp/22\"}]},\"application\":{\"items\":[{\"value\":\"any\"}]},\"user\":{\"items\":[{\"value\":\"any\"}]},\"fields\":[{\"name\":\"Change URL Category\",\"values\":[\"any\"]}],\"action\":\"Allow\"}]}}";
	}

	public static String getRiskReportRespData() {

		return "{\n"
				+ "    \"RiskReport\": [\n"
				+ "        {\n"
				+ "            \"Risk\": [\n"
				+ "                [\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I01-inbound-any\",\n"
				+ "                        \"severity\": \"high\",\n"
				+ "                        \"description\": \"\\\"Any\\\" service can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I01\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I25-inbound-http\",\n"
				+ "                        \"severity\": \"suspected high\",\n"
				+ "                        \"description\": \"HTTP/HTTPS can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I25\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"R08-rule-allsrv\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"\\\"Allow Any service\\\" rules\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"R08\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I26-inbound-ftp\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"FTP can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I26\"\n"
				+ "                    }\n"
				+ "                ]\n"
				+ "            ],\n"
				+ "            \"Date\": \" Fri Nov 19 05:42:06 2021\"\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}";
	}

	public static String getRiskReportRespDataForTwoChangeId() {

		return	"{\n"
				+ "    \"RiskReport\": [\n"
				+ "        {\n"
				+ "            \"Risk\": [\n"
				+ "                [\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I01-inbound-any\",\n"
				+ "                        \"severity\": \"high\",\n"
				+ "                        \"description\": \"\\\"Any\\\" service can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I01\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I25-inbound-http\",\n"
				+ "                        \"severity\": \"suspected high\",\n"
				+ "                        \"description\": \"HTTP/HTTPS can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I25\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"R08-rule-allsrv\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"\\\"Allow Any service\\\" rules\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"R08\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I26-inbound-ftp\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"FTP can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197,206.253.229.198\",\n"
				+ "                        \"code\": \"I26\"\n"
				+ "                    }\n"
				+ "                ]\n"
				+ "            ],\n"
				+ "            \"Date\": \" Fri Nov 19 05:42:06 2021\"\n"
				+ "        },\n"
				+ "        {\n"
				+ "            \"Risk\": [\n"
				+ "                [\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I01-inbound-any\",\n"
				+ "                        \"severity\": \"high\",\n"
				+ "                        \"description\": \"\\\"Any\\\" service can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197\",\n"
				+ "                        \"code\": \"I01\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I25-inbound-http\",\n"
				+ "                        \"severity\": \"suspected high\",\n"
				+ "                        \"description\": \"HTTP/HTTPS can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197\",\n"
				+ "                        \"code\": \"I25\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"R08-rule-allsrv\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"\\\"Allow Any service\\\" rules\",\n"
				+ "                        \"devices\": \"206.253.229.197\",\n"
				+ "                        \"code\": \"R08\"\n"
				+ "                    },\n"
				+ "                    {\n"
				+ "                        \"profile\": \"Standard\",\n"
				+ "                        \"name\": \"I26-inbound-ftp\",\n"
				+ "                        \"severity\": \"medium\",\n"
				+ "                        \"description\": \"FTP can enter your network\",\n"
				+ "                        \"devices\": \"206.253.229.197\",\n"
				+ "                        \"code\": \"I26\"\n"
				+ "                    }\n"
				+ "                ]\n"
				+ "            ],\n"
				+ "            \"Date\": \" Tue Jul 26 13:04:29 2022\"\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}";
	}

	public static String getChangeNeededInfoRespData() {

		return	"{\n"
				+ "    \"ChangeNeededInfo\": [\n"
				+ "        {\n"
				+ "            \"source\": \"192.168.10.10\",\n"
				+ "            \"destination\": \"10.10.10.10\",\n"
				+ "            \"service\": \"tcp/443\",\n"
				+ "            \"isChangeNeeded\": false,\n"
				+ "            \"queryItem\": [\n"
				+ "                {\n"
				+ "                    \"message\": \"Allowed (x1)\",\n"
				+ "                    \"deviceName\": \"atl_msslab_R8040_fw\",\n"
				+ "                    \"displayName\": \"atl-msslab-R8040_fw\"\n"
				+ "                }\n"
				+ "            ]\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}";
	}

	public static String getChangeNeededInfoRespDataForPartiallyAllowed() {
		return  "{\n"
				+ "    \"ChangeNeededInfo\": [\n"
				+ "        {\n"
				+ "            \"source\": \"10.0.0.5\",\n"
				+ "            \"destination\": \"192.168.0.0/16\",\n"
				+ "            \"service\": \"tcp/22\",\n"
				+ "            \"isChangeNeeded\": true,\n"
				+ "            \"queryItem\": [\n"
				+ "                {\n"
				+ "                    \"message\": \"Partially allowed (x2)\",\n"
				+ "                    \"deviceName\": \"atl_msslab_R8040_fw\",\n"
				+ "                    \"displayName\": \"atl-msslab-R8040_fw\"\n"
				+ "                }\n"
				+ "            ]\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}";
	}
	public static String getChangeNeededInfoRespDataForTwoChangeId() {

		return	"{\n"
				+ "    \"ChangeNeededInfo\": [\n"
				+ "        {\n"
				+ "            \"source\": \"10.0.0.5\",\n"
				+ "            \"destination\": \"192.168.0.5\",\n"
				+ "            \"service\": \"tcp/22\",\n"
				+ "            \"isChangeNeeded\": true,\n"
				+ "            \"queryItem\": [\n"
				+ "                {\n"
				+ "                    \"message\": \"Blocked (x1)\",\n"
				+ "                    \"deviceName\": \"atl_msslab_R8040_fw\",\n"
				+ "                    \"displayName\": \"atl-msslab-R8040_fw\"\n"
				+ "                }\n"
				+ "            ]\n"
				+ "        },\n"
				+ "        {\n"
				+ "            \"source\": \"10.0.0.10\",\n"
				+ "            \"destination\": \"10.0.0.20\",\n"
				+ "            \"service\": \"tcp/80\",\n"
				+ "            \"isChangeNeeded\": true,\n"
				+ "            \"queryItem\": [\n"
				+ "                {\n"
				+ "                    \"message\": \"Blocked by default device behavior\",\n"
				+ "                    \"deviceName\": \"FG3600VD02\",\n"
				+ "                    \"displayName\": \"FG3600VD02\"\n"
				+ "                },\n"
				+ "                {\n"
				+ "                    \"message\": \"Blocked by default device behavior\",\n"
				+ "                    \"deviceName\": \"FG3600VD03\",\n"
				+ "                    \"displayName\": \"FG3600VD03\"\n"
				+ "                },\n"
				+ "                {\n"
				+ "                    \"message\": \"Allowed\",\n"
				+ "                    \"deviceName\": \"10_239_0_87\",\n"
				+ "                    \"displayName\": \"10.239.0.87\"\n"
				+ "                }\n"
				+ "            ]\n"
				+ "        }\n"
				+ "    ]\n"
				+ "}";
	}

	public static String getAlgosecApiResponseForRiskReport() {
		return "CF.{Risk_API_Details}: {\n"
				+ "       \"RisksURLs\" : \"firewalls/afa-1221/riskCheck_3_fireflow63/risks.html\",\n"
				+ "       \"Risk\" : [\n"
				+ "          {\n"
				+ "             \"profile\" : \"test\",\n"
				+ "             \"name\" : \"test\",\n"
				+ "             \"severity\" : \"low\"\n"
				+ "          }\n"
				+ "       ]\n"
				+ " \"Date\" : \"Tue Jul 26 13:04:29 2022\""
				+ "}\n"
				+ "CF.{MSS Ticket}: \n";
	}

	public static String ruleRemovalInputJson(){
		return "{\n" +
				"  \"Policy Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"192.168.12.1\",\n" +
				"      \"destination_address\": \"255.255.255.12\",\n" +
				"      \"service\": \"TCP_443\",\n" +
				"      \"application\": \"SNow,.IBM.box.com\",\n" +
				"      \"advance_security\": \"Apply Substandard_Policy\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"To allow traffic from one network to another on port 443\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"Inbound\",\n" +
				"      \"destination_zone\": \"Outbound\",\n" +
				"      \"source_address\": \"H_10.1.1.1\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"TCP_443,TCP_80\",\n" +
				"      \"application\": \"DancedanceRevolution.org\",\n" +
				"      \"advance_security\": \"Dance_Fever_Protection_Suite\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Protect Internal host from Boogie Fever\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"3\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"LAN4\",\n" +
				"      \"destination_zone\": \"WAN\",\n" +
				"      \"source_address\": \"N_11.1.1.0\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Customer Access\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"4\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"Paid_host\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"application\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Access for Paid host\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"5\",\n" +
				"      \"action\": \"Delete\",\n" +
				"      \"rule_id\": \"CF9B1CA6-E509-4AF3-949F-927A342B2232\",\n" +
				"      \"source_address\": \"10.0.0.0/16\",\n" +
				"      \"destination_address\": \"123.45.67.90/32\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"advance_security\": \"N/A\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Remove Audit Scanners after Audit\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
	}
	public static String getInvalidPortTrafficRequestJson(){
		return "{\n" +
				"  \"Policy Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"192.168.12.1\",\n" +
				"      \"destination_address\": \"255.255.255.12\",\n" +
				"      \"service\": \"TCP_443\",\n" +
				"      \"application\": \"SNow,.IBM.box.com\",\n" +
				"      \"advance_security\": \"Apply Substandard_Policy\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"To allow traffic from one network to another on port 443\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"Inbound\",\n" +
				"      \"destination_zone\": \"Outbound\",\n" +
				"      \"source_address\": \"H_10.1.1.1\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"TCP_INVALID\",\n" +
				"      \"application\": \"DancedanceRevolution.org\",\n" +
				"      \"advance_security\": \"Dance_Fever_Protection_Suite\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Protect Internal host from Boogie Fever\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"3\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"LAN4\",\n" +
				"      \"destination_zone\": \"WAN\",\n" +
				"      \"source_address\": \"N_11.1.1.0\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Customer Access\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"4\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"Paid_host\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"PORT\",\n" +
				"      \"application\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Access for Paid host\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"Object Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.1\",\n" +
				"      \"action_items\": \"10.1.1.1\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.1\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.2\",\n" +
				"      \"action_items\": \"10.1.1.2\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.2\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

	}
	public static String getInvaliProtocolTrafficRequestJson(){
		return "{\n" +
				"  \"Policy Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"192.168.12.1\",\n" +
				"      \"destination_address\": \"255.255.255.12\",\n" +
				"      \"service\": \"tcp_80\",\n" +
				"      \"application\": \"SNow,.IBM.box.com\",\n" +
				"      \"advance_security\": \"Apply Substandard_Policy\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"To allow traffic from one network to another on port 443\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"Inbound\",\n" +
				"      \"destination_zone\": \"Outbound\",\n" +
				"      \"source_address\": \"H_10.1.1.1\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"TEST2_40\",\n" +
				"      \"application\": \"DancedanceRevolution.org\",\n" +
				"      \"advance_security\": \"Dance_Fever_Protection_Suite\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Protect Internal host from Boogie Fever\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"3\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"LAN4\",\n" +
				"      \"destination_zone\": \"WAN\",\n" +
				"      \"source_address\": \"N_11.1.1.0\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Customer Access\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"Object Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.1\",\n" +
				"      \"action_items\": \"10.1.1.1\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.1\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.2\",\n" +
				"      \"action_items\": \"10.1.1.2\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.2\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
	}

	public static String getInvalidServiceTrafficRequestJson(){
		return "{\n" +
				"  \"Policy Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"192.168.12.1\",\n" +
				"      \"destination_address\": \"255.255.255.12\",\n" +
				"      \"service\": \"tcp_80\",\n" +
				"      \"application\": \"SNow,.IBM.box.com\",\n" +
				"      \"advance_security\": \"Apply Substandard_Policy\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"To allow traffic from one network to another on port 443\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"Inbound\",\n" +
				"      \"destination_zone\": \"Outbound\",\n" +
				"      \"source_address\": \"H_10.1.1.1\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"tcp/\",\n" +
				"      \"application\": \"DancedanceRevolution.org\",\n" +
				"      \"advance_security\": \"Dance_Fever_Protection_Suite\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Protect Internal host from Boogie Fever\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"3\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"LAN4\",\n" +
				"      \"destination_zone\": \"WAN\",\n" +
				"      \"source_address\": \"N_11.1.1.0\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Customer Access\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"Object Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.1\",\n" +
				"      \"action_items\": \"10.1.1.1\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.1\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.2\",\n" +
				"      \"action_items\": \"10.1.1.2\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.2\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
	}

	public static String getNonExistentServiceObjectTrafficRequestJson(){
		return "{\n" +
				"  \"Policy Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_address\": \"192.168.12.1\",\n" +
				"      \"destination_address\": \"255.255.255.12\",\n" +
				"      \"service\": \"tcp_80\",\n" +
				"      \"application\": \"SNow,.IBM.box.com\",\n" +
				"      \"advance_security\": \"Apply Substandard_Policy\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"To allow traffic from one network to another on port 443\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"Inbound\",\n" +
				"      \"destination_zone\": \"Outbound\",\n" +
				"      \"source_address\": \"H_10.1.1.1\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"service_object_does_not_exist_in_algosec\",\n" +
				"      \"application\": \"DancedanceRevolution.org\",\n" +
				"      \"advance_security\": \"Dance_Fever_Protection_Suite\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Deny\",\n" +
				"      \"comments\": \"Protect Internal host from Boogie Fever\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"3\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"source_zone\": \"LAN4\",\n" +
				"      \"destination_zone\": \"WAN\",\n" +
				"      \"source_address\": \"N_11.1.1.0\",\n" +
				"      \"destination_address\": \"ANY\",\n" +
				"      \"service\": \"ANY\",\n" +
				"      \"acl_action_{accept,_deny}\": \"Accept\",\n" +
				"      \"comments\": \"Customer Access\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"Object Update\": [\n" +
				"    {\n" +
				"      \"request_item\": \"1\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.1\",\n" +
				"      \"action_items\": \"10.1.1.1\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.1\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"request_item\": \"2\",\n" +
				"      \"action\": \"Create\",\n" +
				"      \"object_type\": \"Host\",\n" +
				"      \"object_name\": \"H_10.1.1.2\",\n" +
				"      \"action_items\": \"10.1.1.2\",\n" +
				"      \"comments\": \"Create host with IP 10.1.1.2\",\n" +
				"      \"firewall_policy\": \"atl_msslab_R8040_fw\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
	}
}