package com.mp.cluster.service;

import com.mp.cluster.entity.InstanceInfo;

public interface InstanceService {

    InstanceInfo register();

    InstanceInfo remove();

    void monitor();

    InstanceInfo getCurrentInstanceInfo();

    void clean();
}
