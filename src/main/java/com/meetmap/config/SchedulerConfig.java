package com.meetmap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        // You can configure a thread pool size and thread name prefix for the TaskScheduler
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // Number of concurrent threads for scheduling tasks
        scheduler.setThreadNamePrefix("ScheduledTask-");  // Name prefix for scheduler threads
        scheduler.initialize();
        return scheduler;
    }
}
