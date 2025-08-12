package com.mp.cluster.repository;

import com.mp.cluster.cfg.ConditionalOnRds;
import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@ConditionalOnRds
public interface InstanceInfoRdsRepository extends JpaRepository<InstanceInfo, String> {

    List<InstanceInfo> findByStatusIn(Set<InstanceStatus> statuses);

    List<InstanceInfo> findByStatusInAndLastMonitorTimeLessThan(Set<InstanceStatus> statuses, Instant time);
}
