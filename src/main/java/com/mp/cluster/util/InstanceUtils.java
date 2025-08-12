package com.mp.cluster.util;

import com.mp.cluster.entity.InstanceInfo;
import com.mp.cluster.entity.InstanceType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InstanceUtils {

    public static boolean isReplica(InstanceInfo instanceInfo) {
        return instanceInfo.getType() == InstanceType.REPLICA;
    }

    public static boolean isMaster(InstanceInfo instanceInfo) {
        return instanceInfo.getType() == InstanceType.MASTER;
    }
}
