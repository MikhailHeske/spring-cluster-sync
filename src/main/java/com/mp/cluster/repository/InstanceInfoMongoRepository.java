package com.mp.cluster.repository;

import com.mp.cluster.cfg.ConditionalOnMongo;
import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@ConditionalOnMongo
public interface InstanceInfoMongoRepository extends MongoRepository<InstanceInfo, String> {

    List<InstanceInfo> findByStatusIn(Set<InstanceStatus> statuses);

    List<InstanceInfo> findByStatusInAndLastMonitorTimeLessThan(Set<InstanceStatus> statuses, Instant time);
}
