package com.mp.cluster.service;

import com.mp.cluster.util.VoidFunction;

import java.util.function.Supplier;

public interface ClusterExecutor {

    public void executeOnMaster(VoidFunction function);

    public void executeOnReplica(VoidFunction function);

    public <T> T executeOnMaster(Supplier<T> function);

    public <T> T executeOnReplica(Supplier<T> function);
}
