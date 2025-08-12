package com.mp.cluster.service.entity;

import com.mp.cluster.cfg.ConditionalOnRds;
import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;
import com.mp.cluster.repository.InstanceInfoRdsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@ConditionalOnRds
public class InstanceEntityServiceRdsImpl implements InstanceEntityService {

    private final InstanceInfoRdsRepository repository;

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
        return repository.getById(id);
    }

    public List<InstanceInfo> findByStatusInAndLastMonitorTimeLessThen(Set<InstanceStatus> statuses, Instant time) {
        return repository.findByStatusInAndLastMonitorTimeLessThan(statuses, time);
    }

    @Override
    public void deleteByIds(Set<String> ids) {
        repository.deleteAllById(ids);
    }
}
