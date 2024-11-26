package com.lyflexi.alertxxljob.monitor;

import com.lyflexi.alertxxljob.service.IMessageAlertService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:10
 */

@Component
@Slf4j
public class JobMonitor {
    /**
     * 告警发送阈值
     */
    private final Long  ALERT_THRESHOLD = 5000L;
    /**
     * 监控延迟
     */
    private final Long INITAIL_DELAY = 0L;
    /**
     * 监控间隔
     */
    private final Long INTERVAL = 5000L;
    /**
     * 风险等级调整阈值
     *  eg.>2 ，调整为高危
     */
    private final Integer RISK_THRESHOLD = 2;

    @Autowired
    private IMessageAlertService messageAlertService;
    private final List<JobInfo> taskPool = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    /**
     * 启动监控线程
     */
    @PostConstruct
    public void initMonitor() {
        executor.scheduleWithFixedDelay(new DurationMonitorTask(taskPool,messageAlertService,ALERT_THRESHOLD,RISK_THRESHOLD), INITAIL_DELAY, INTERVAL, TimeUnit.MILLISECONDS);
        log.info("长定时任务监控组件启动...");
    }

    /**
     * 注册任务
     * @param jobInfo
     */
    public void registerTask(JobInfo jobInfo) {
        taskPool.add(jobInfo);
    }



    /**
     * 销毁方法
     */
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        log.info("定时任务监控线程销毁...");
    }

}