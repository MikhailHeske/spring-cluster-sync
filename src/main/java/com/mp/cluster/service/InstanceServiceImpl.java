package com.mp.cluster.service;

import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceStatus;
import com.mp.cluster.entity.InstanceType;
import com.mp.cluster.service.entity.InstanceEntityService;
import com.mp.cluster.util.InstanceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.mp.cluster.util.InstanceUtils.isReplica;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceServiceImpl implements InstanceService {

    private final InstanceEntityService entityService;
    private final static String INSTANCE_ID = UUID.randomUUID().toString();

    private InstanceInfo currentInstance;

    @Override
    public InstanceInfo register() {
        Instant now = Instant.now();
        currentInstance = InstanceInfo.
                builder()
                .instanceId(INSTANCE_ID)
                .type(InstanceType.REPLICA)
                .status(InstanceStatus.ACTIVE)
                .lastMonitorTime(now)
                .registerTime(now)
                .build();

        currentInstance = entityService.save(currentInstance);
        return currentInstance;
    }

    @Override
    @Transactional
    public InstanceInfo remove() {
        currentInstance = entityService.getById(currentInstance.getInstanceId());
        currentInstance.setStatus(InstanceStatus.REMOVED);
        currentInstance.setRemovedTime(Instant.now());
        currentInstance = entityService.save(currentInstance);

        return currentInstance;
    }

    @Override
    public void monitor() {
        currentInstance = entityService.getById(currentInstance.getInstanceId());
        currentInstance.setLastMonitorTime(Instant.now());
        currentInstance.setStatus(InstanceStatus.ACTIVE);
        currentInstance = entityService.save(currentInstance);

        List<InstanceInfo> instancesInfos = entityService.findInstancesInfo();
        instancesInfos.sort(Comparator.comparing(InstanceInfo::getInstanceId));
        if (isReplica(currentInstance)) {
            // check master. If master is down check that can this replica be the master
            monitorMaster(instancesInfos, currentInstance);
        }

        monitorNextInstance(instancesInfos, currentInstance);
    }

    protected void monitorMaster(List<InstanceInfo> instancesInfos, InstanceInfo currentInstance) {
        boolean isMasterAlive = instancesInfos.stream().anyMatch(InstanceUtils::isMaster);

        if (!isMasterAlive) {
            boolean becomeMaster = false;
            if (instancesInfos.isEmpty()) {
                log.warn("Instance [{}]: Warning!!! Cluster is empty!!! How is it possible if I work????", currentInstance.getInstanceId());
            } else {
                becomeMaster = instancesInfos.getFirst().getInstanceId().equals(currentInstance.getInstanceId());
            }

            if (becomeMaster) {
                log.info("Instance [{}]: Current instance became master", currentInstance.getInstanceId());
                currentInstance.setType(InstanceType.MASTER);
                try {
                    entityService.save(currentInstance);
                } catch (Exception e) {
                    log.error("Instance [{}]: Cannot persist update to become master", currentInstance.getInstanceId());
                }
            }
        }
    }

    protected void monitorNextInstance(List<InstanceInfo> instanceInfos,
                                       InstanceInfo currentInstance) {
        if (instanceInfos.size() > 1) {
            int monitoredInstanceIndex = -1;
            for (int i = 0; i < instanceInfos.size() && monitoredInstanceIndex < 0; i ++) {
                if (instanceInfos.get(i).getInstanceId().equals(currentInstance.getInstanceId())) {
                    monitoredInstanceIndex = i + 1;
                    if (monitoredInstanceIndex >= instanceInfos.size()) {
                        monitoredInstanceIndex = 0;
                    }
                }
            }

            if (monitoredInstanceIndex >= 0) {
                InstanceInfo monitored = instanceInfos.get(monitoredInstanceIndex);
                if (isDown(monitored)) {
                    log.info("Instance [{}]: Monitored instance {} is lost. Persist it", currentInstance.getInstanceId(), monitored.getInstanceId());
                    monitored.setStatus(InstanceStatus.LOST);
                    monitored.setLastLostTime(Instant.now());
                    entityService.save(monitored);
                }
            } else {
                log.warn("Instance [{}]: Didn't find instance for monitor", currentInstance.getInstanceId());
            }
        }
    }

    @Override
    public InstanceInfo getCurrentInstanceInfo() {
        return currentInstance;
    }

    @Override
    public void clean() {
        Instant removeFrom = Instant.now().minus(1, ChronoUnit.DAYS);
        List<InstanceInfo> oldData = entityService.findByStatusInAndLastMonitorTimeLessThen(Set.of(InstanceStatus.LOST, InstanceStatus.REMOVED), removeFrom);
        log.info("Found {} instances for remove from list", oldData.size());
        if (!oldData.isEmpty()) {
            entityService.deleteByIds(oldData.stream().map(InstanceInfo::getInstanceId).collect(Collectors.toSet()));
        }
    }

    protected boolean isDown(InstanceInfo instanceInfo) {
        Instant monitorTime = Instant.now().minus(30, ChronoUnit.SECONDS);
        return instanceInfo.getLastMonitorTime().isBefore(monitorTime);
    }
}
