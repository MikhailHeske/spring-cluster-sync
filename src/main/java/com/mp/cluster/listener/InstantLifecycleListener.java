package com.mp.cluster.listener;

import com.mp.cluster.service.InstanceService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstantLifecycleListener {

    private final InstanceService instanceService;

    @EventListener
    public void onApplicationStartup(ApplicationReadyEvent event) {
        instanceService.register();
    }

    @PreDestroy // Let's try this
    public void onApplicationShutdown() {
        instanceService.remove();
    }
}
