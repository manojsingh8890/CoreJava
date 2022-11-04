package com.ibm.sec.dao;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.sec.model.FirewallChangeRequestEntity;
import com.ibm.sec.model.FirewallChangeSessionEntity;
import com.ibm.sec.model.FirewallChangeSessionErrorsEntity;
import com.ibm.sec.repository.FirewallChangeRequestRepository;
import com.ibm.sec.repository.FirewallChangeRepository;
import com.ibm.sec.repository.FirewallChangeSessionErrorsRepository;
import com.ibm.sec.repository.FirewallChangeSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.sec.model.FirewallChangeEntity;
import com.ibm.sec.util.IConstant;

import lombok.extern.slf4j.Slf4j;


@Service("FirewallChangeService")
@Slf4j
@Transactional
public class FirewallChangeServiceDAOImpl implements FirewallChangeDAO {

	@Autowired
	private FirewallChangeRequestRepository firewallChangeRequestRepository;

	@Autowired
	private FirewallChangeRepository firewallChangeRepository;

	@Autowired
	private FirewallChangeSessionRepository firewallChangeSessionRepository;

	@Autowired
	private FirewallChangeSessionErrorsRepository firewallChangeSessionErrorsRepository;

	@Override
	public FirewallChangeRequestEntity saveRequest(String request, String sessionId) {
		FirewallChangeRequestEntity entity = new FirewallChangeRequestEntity();
		entity.setChangeRequest(request);
		entity.setSessionId(sessionId);
		return firewallChangeRequestRepository.save(entity);
	}

	@Override
	public FirewallChangeRequestEntity saveFirewallChangeRequest(FirewallChangeRequestEntity entity) {
		return firewallChangeRequestRepository.save(entity);
	}

	@Override
	public FirewallChangeRequestEntity getChangeRequestBySessionId(String sessionId) {
		return firewallChangeRequestRepository.findBySessionId(sessionId);
	}

	@Override
	public List<FirewallChangeEntity> getAllTemporaryPolicyChangeRequestsBySessionId(String sessionId) {
		return firewallChangeRepository.findBySessionIdAndAlgoSecChangeRetentionTypeAndChangeTypeIn(sessionId, IConstant.AlgosecChangeRententionType.TEMPORARY, Arrays.asList(IConstant.AlgosecChangeType.POLICY_CREATE, IConstant.AlgosecChangeType.POLICY_DELETE));
	}

//	@Value("${cleanup_threshold_hours}")
//	private int cleanupFrequencyHours;
//
//	@Override
//	public FirewallChangeEntity save(FirewallChangeEntity firewallChange) {
//		return firewallRepository.save(firewallChange);
//	}
//
//	@Override
//	public FirewallChangeEntity update(FirewallChangeEntity firewallChange) {
//		return firewallRepository.save(firewallChange);
//	}
//
//	@Override
//	public FirewallChangeEntity loadFirewallChangeByChangeId(String changeId) {
//		FirewallChangeEntity fwChange = firewallRepository.findByChangeId(changeId);
//		return fwChange;
//	}
//
//	@Override
//	public FirewallChangeEntity loadFirewallChangeByItsmId(String itsmId) {
//		FirewallChangeEntity fwChange = firewallRepository.findByItsmId(itsmId);
//		return fwChange;
//	}
//
//	@Override
//	public boolean delete(FirewallChangeEntity firewallChange) {
//		FirewallChangeEntity fwChange = findById(firewallChange.getId());
//		if(fwChange != null && fwChange.getId()>0) {
//			 firewallRepository.deleteById(fwChange.getId());
//			 return true;
//		}else {
//			 return false;
//		}
//	}
//
//	@Override
//	public FirewallChangeEntity findById(int id) {
//		FirewallChangeEntity firewallChange = this.firewallRepository.findById(id).orElseGet(null);
//		return firewallChange;
//	}
//
//	@Override
//	public Mono<List<FirewallChangeEntity>> findAll() {
//		return Mono.just(this.firewallRepository.findAll());
//	}
//
	@Override
	public List<FirewallChangeEntity> saveAll(List<FirewallChangeEntity> firewallChange){
		return firewallChangeRepository.saveAll(firewallChange);
	}

	@Override
	public boolean areTemporaryChangeRequestsComplete(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		return firewallChangeSessionEntity.isTemporaryRequestsComplete();
	}

	@Override
	public boolean areFinalChangeRequestsComplete(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		return firewallChangeSessionEntity.isFinalRequestsComplete();
	}

	@Override
	public boolean isRisksFetchComplete(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		return firewallChangeSessionEntity.isRiskFetchComplete();
	}

	@Override
	public boolean isChangeNeededInfoFetchComplete(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		return firewallChangeSessionEntity.isChangeNeededInfoFetchComplete();
	}

	@Override
	public void initiateFirewallChangeSession(String sessionId) {
		FirewallChangeSessionEntity entity = new FirewallChangeSessionEntity();
		entity.setSessionId(sessionId);
		firewallChangeSessionRepository.save(entity);
	}

	@Override
	public void markCompleteTemporaryRequests(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		firewallChangeSessionEntity.setTemporaryRequestsComplete(true);
		firewallChangeSessionRepository.save(firewallChangeSessionEntity);
	}

	@Override
	public void saveChangeNeededInfo(String changeNeededInfo, String sessionId) {
		FirewallChangeSessionEntity entity = firewallChangeSessionRepository.findBySessionId(sessionId);
		entity.setChangeNeededInfo(changeNeededInfo);
		firewallChangeSessionRepository.save(entity);
	}

	@Override
	public void saveRiskInfo(String riskInfo, String sessionId) {
		FirewallChangeSessionEntity entity = firewallChangeSessionRepository.findBySessionId(sessionId);
		entity.setRiskInfo(riskInfo);
		firewallChangeSessionRepository.save(entity);
	}

	@Override
	public FirewallChangeSessionErrorsEntity saveError(String error, IConstant.Functionality functionality, String sessionId) {
		FirewallChangeSessionErrorsEntity entity = new FirewallChangeSessionErrorsEntity();
		entity.setError(error);
		entity.setFunctionality(functionality);
		entity.setSessionId(sessionId);
		firewallChangeSessionErrorsRepository.save(entity);
		return entity;
	}

	@Override
	public List<String> getErrorsBySessionIdAndFunctionality(String sessionId, IConstant.Functionality functionality) {
		List<FirewallChangeSessionErrorsEntity> entities = firewallChangeSessionErrorsRepository.findBySessionIdAndFunctionality(sessionId, functionality);
		return entities.stream().map(FirewallChangeSessionErrorsEntity::getError).collect(Collectors.toList());
	}

	@Override
	public List<String> getTemporaryPolicyCreateChangesBySessionId(String sessionId) {
		List<FirewallChangeEntity> entities = firewallChangeRepository.findBySessionIdAndAlgoSecChangeRetentionTypeAndChangeTypeIn(sessionId, IConstant.AlgosecChangeRententionType.TEMPORARY, Arrays.asList(IConstant.AlgosecChangeType.POLICY_CREATE));
		return entities.stream().map(FirewallChangeEntity::getChangeId).collect(Collectors.toList());
	}

	@Override
	public String getChangeNeededInfoBySessionId(String sessionId) {
		FirewallChangeSessionEntity entity = firewallChangeSessionRepository.findBySessionId(sessionId);
		if(entity != null) {
			return entity.getChangeNeededInfo();
		} else {
			return null;
		}
	}

	@Override
	public String getRiskInfoBySessionId(String sessionId) {
		FirewallChangeSessionEntity entity = firewallChangeSessionRepository.findBySessionId(sessionId);
		if(entity != null) {
			return entity.getRiskInfo();
		} else {
			return null;
		}
	}

	@Override
	public void markCompleteChangeNeededInfoFetch(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		firewallChangeSessionEntity.setChangeNeededInfoFetchComplete(true);
		firewallChangeSessionRepository.save(firewallChangeSessionEntity);
	}

	@Override
	public void markCompleteRiskInfoFetch(String sessionId) {
		FirewallChangeSessionEntity firewallChangeSessionEntity = firewallChangeSessionRepository.findBySessionId(sessionId);
		firewallChangeSessionEntity.setRiskFetchComplete(true);
		firewallChangeSessionRepository.save(firewallChangeSessionEntity);
	}

	//
//	@Override
//	public List<FirewallChangeEntity> findAllNewRequests() {
//		return this.firewallRepository.findAllLastDateNewFirewallChange(getCleanupFrequencyHours());
//	}
//
	@Override
	public List<FirewallChangeEntity> updateStatus(List<FirewallChangeEntity> firewallChanges, IConstant.AlgosecChangeStatus status) {
		List<FirewallChangeEntity> updatedList= firewallChanges.stream().map(f -> {
			f.setAlgoSecChangeStatus(status);
			return f;
			}).collect(Collectors.toList());
		return saveAll(updatedList);
	}
//
//	public int getCleanupFrequencyHours() {
//		return cleanupFrequencyHours == 0 ? DEFAULT_CLEANUP_FREQUENCY_HOURS : cleanupFrequencyHours;
//	}

	@Override
	public List<FirewallChangeEntity> getTemporaryChangeEntityForPolicyCreateChangesBySessionId(String sessionId) {
		return firewallChangeRepository.findBySessionIdAndAlgoSecChangeRetentionTypeAndChangeTypeIn(sessionId, IConstant.AlgosecChangeRententionType.TEMPORARY, Arrays.asList(IConstant.AlgosecChangeType.POLICY_CREATE));
	}
}
