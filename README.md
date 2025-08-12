# Spring Cluster Sync

Library for sync instances in cluster. Use Master/Replica approach where one instance is master and other instances are replicas. On master down one replica became master.
Allow to execute some code only on master or only on replica instances.
Support mongoDB and RDS data storages. 

## How to add

1. Add library to maven or gradle
2. Use `@EnableClusterSync` annotation for enabling library
3. Set data storage in properties file `cluster-sync.data-source: rds/mongo`
4. If you use RDS create table 
```
CREATE TABLE IF NOT EXISTS instance_info (
    instance_id VARCHAR(36) NOT NULL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    register_time TIMESTAMP,
    last_monitor_time TIMESTAMP,
    removed_time TIMESTAMP,
    last_lost_time TIMESTAMP,
    version INT
);
```

## How to use

```
@Slf4j
@Service
@RequiredArgsConstructor
public class Demo {
    private final ClusterExecutor clusterExecutor;
    
    public void foo() {
        clusterExecutor.executeOnMaster(() -> log.info("It's master");
    }
    
    public void bar() {
        clusterExecutor.executeOnReplica(() -> log.info("It's replica");
    }
}   
```