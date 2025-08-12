package com.mp.cluster.cfg;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = "com.mp.cluster")
@EnableMongoRepositories(basePackages = "com.mp.cluster")
@EntityScan(basePackages = "com.mp.cluster")
@EnableScheduling
@Configuration
public class ClusterConfiguration {
}
