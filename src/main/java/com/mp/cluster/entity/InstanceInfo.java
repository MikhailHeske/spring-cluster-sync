package com.mp.cluster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Document
@Table(name = "instance_info")
public class InstanceInfo {
    @Id
    @jakarta.persistence.Id
    @Column(name = "instance_id")
    private String instanceId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InstanceStatus status;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InstanceType type;
    @Column(name = "register_time")
    private Instant registerTime;
    @Column(name = "last_monitor_time")
    private Instant lastMonitorTime;
    @Column(name = "removed_time")
    private Instant removedTime;
    @Column(name = "last_lost_time")
    private Instant lastLostTime;
    @Version
    @Column(name = "version")
    private long version;
}
