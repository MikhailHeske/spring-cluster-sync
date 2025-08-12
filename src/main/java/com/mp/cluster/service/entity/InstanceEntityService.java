package com.mp.cluster.service.entity;

import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface InstanceEntityService {

    InstanceInfo save(InstanceInfo instanceInfo);

    List<InstanceInfo> findInstancesInfo();

    InstanceInfo getById(String id);

    List<InstanceInfo> findByStatusInAndLastMonitorTimeLessThen(Set<InstanceStatus> statuses, Instant time);

    void deleteByIds(Set<String> ids);
}
