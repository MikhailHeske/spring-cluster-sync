package com.mp.cluster.service;

import com.mp.cluster.util.InstanceUtils;
import com.mp.cluster.util.VoidFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ClusterExecutorImpl implements ClusterExecutor {

    private final InstanceService instanceService;

    @Override
    public void executeOnMaster(VoidFunction function) {
        if (InstanceUtils.isMaster(instanceService.getCurrentInstanceInfo())) {
            function.apply();
        }
    }

    @Override
    public void executeOnReplica(VoidFunction function) {
        if (InstanceUtils.isReplica(instanceService.getCurrentInstanceInfo())) {
            function.apply();
        }
    }

    @Override
    public <T> T executeOnMaster(Supplier<T> function) {
        if (InstanceUtils.isMaster(instanceService.getCurrentInstanceInfo())) {
            return function.get();
        }
        return null;
    }

    @Override
    public <T> T executeOnReplica(Supplier<T> function) {
        if (InstanceUtils.isReplica(instanceService.getCurrentInstanceInfo())) {
            return function.get();
        }
        return null;
    }
}
