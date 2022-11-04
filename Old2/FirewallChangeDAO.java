package com.ibm.sec.dao;

import java.util.List;

import com.ibm.sec.model.FirewallChangeEntity;
import com.ibm.sec.model.FirewallChangeRequestEntity;
import com.ibm.sec.model.FirewallChangeSessionEntity;
import com.ibm.sec.model.FirewallChangeSessionErrorsEntity;
import com.ibm.sec.util.IConstant;

public interface FirewallChangeDAO {

	public FirewallChangeRequestEntity saveRequest(String request, String sessionId);

	public FirewallChangeRequestEntity saveFirewallChangeRequest(FirewallChangeRequestEntity entity);

	public FirewallChangeRequestEntity getChangeRequestBySessionId(String sessionId);

	public List<FirewallChangeEntity> getAllTemporaryPolicyChangeRequestsBySessionId(String sessionId);

//	public static final int DEFAULT_CLEANUP_FREQUENCY_HOURS = 24;
//
//	FirewallChangeEntity loadFirewallChangeByChangeId(String changeId);
//
//	FirewallChangeEntity loadFirewallChangeByItsmId(String itsmId);
//
//	boolean delete(FirewallChangeEntity firewallChange);
//
//	FirewallChangeEntity save(FirewallChangeEntity firewallChange);
//
//	FirewallChangeEntity update(FirewallChangeEntity firewallChange);
//
//	FirewallChangeEntity findById(int id);
//
//	Mono<List<FirewallChangeEntity>> findAll();
//
//	List<FirewallChangeEntity> findAllNewRequests();

	List<FirewallChangeEntity> updateStatus(List<FirewallChangeEntity> firewallChanges, IConstant.AlgosecChangeStatus status);

	List<FirewallChangeEntity> saveAll(List<FirewallChangeEntity> firewallChange);

	boolean areTemporaryChangeRequestsComplete(String sessionId);

	boolean areFinalChangeRequestsComplete(String sessionId);

	boolean isRisksFetchComplete(String sessionId);

	boolean isChangeNeededInfoFetchComplete(String sessionId);

	void initiateFirewallChangeSession(String sessionId);

	void markCompleteTemporaryRequests(String sessionId);

	void saveChangeNeededInfo(String changeNeededInfo, String sessionId);

	void saveRiskInfo(String riskInfo, String sessionId);

	FirewallChangeSessionErrorsEntity saveError(String error, IConstant.Functionality functionality, String sessionId);

	List<String> getErrorsBySessionIdAndFunctionality(String sessionId, IConstant.Functionality functionality);

	List<String> getTemporaryPolicyCreateChangesBySessionId(String sessionId);

	String getChangeNeededInfoBySessionId(String sessionId);

	String getRiskInfoBySessionId(String sessionId);

	void markCompleteChangeNeededInfoFetch(String sessionId);

	void markCompleteRiskInfoFetch(String sessionId);
	
	List<FirewallChangeEntity> getTemporaryChangeEntityForPolicyCreateChangesBySessionId(String sessionId);
 }
