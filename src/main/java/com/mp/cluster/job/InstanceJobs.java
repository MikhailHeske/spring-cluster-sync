package com.mp.cluster.job;

import com.mp.cluster.service.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstanceJobs {

    private final InstanceService instanceService;

    @Scheduled(initialDelay = 1000L, fixedDelay = 1000L)
    public void monitorJob() {
        instanceService.monitor();
    }

    @Scheduled(initialDelay = 1000L, fixedDelay = 60 * 60 * 1000L)
    public void cleanup() {
        instanceService.clean();
    }
}
