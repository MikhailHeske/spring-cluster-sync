
package com.mp.cluster.service.entity;

import com.mp.cluster.cfg.ConditionalOnMongo;
import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;
import com.mp.cluster.repository.InstanceInfoMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@ConditionalOnMongo
public class InstanceEntityServiceMongoImpl implements InstanceEntityService {

    private final InstanceInfoMongoRepository repository;

    @Override
    @Transactional
    public InstanceInfo save(InstanceInfo instanceInfo) {
        return repository.save(instanceInfo);
    }

    @Override
    public List<InstanceInfo> findInstancesInfo() {
        return repository.findByStatusIn(Set.of(InstanceStatus.ACTIVE));
    }

    @Override
    public InstanceInfo getById(String id) {
        return repository.findById(id).get();
    }

    public List<InstanceInfo> findByStatusInAndLastMonitorTimeLessThen(Set<InstanceStatus> statuses, Instant time) {
        return repository.findByStatusInAndLastMonitorTimeLessThan(statuses, time);
    }

    @Override
    public void deleteByIds(Set<String> ids) {
        repository.deleteAllById(ids);
    }
}
