package com.lyflexi.alertxxljob.monitor;

import com.lyflexi.alertxxljob.service.IMessageAlertService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:11
 */

@Slf4j
public class DurationMonitorTask implements Runnable {
    private final List<JobInfo> taskPool;
    private final IMessageAlertService messageAlertService;
    /**
     * 告警阈值
     */
    private Long ALERT_THRESHOLD;
    /**
     * 风险等级阈值
     */
    private Integer RISK_THRESHOLD;

    public DurationMonitorTask(List<JobInfo> taskPool, IMessageAlertService messageAlertService, Long alertThreshold, Integer riskThreshold) {
        this.taskPool = taskPool;
        this.messageAlertService = messageAlertService;
        this.ALERT_THRESHOLD = alertThreshold;
        this.RISK_THRESHOLD = riskThreshold;
    }

    /**
     * 禁止抛异常！
     */
    @Override
    public void run() {
        Long duration = 0L;
        for (JobInfo jobInfo : taskPool) {
            if (jobInfo.isFinish()){
                log.info("定时任务：{}:{}，执行完成耗时：{}ms.", jobInfo.getTaskId(), jobInfo.getFactoryCode(), jobInfo.getDuration());
                removeTask(jobInfo);
            }
            duration = jobInfo.calDuration();
            log.info("定时任务：{}:{}，正在执行,已耗时：{}ms.", jobInfo.getTaskId(), jobInfo.getFactoryCode(), duration);
            if (duration > ALERT_THRESHOLD) {
                log.warn("warn...存在长耗时的定时任务：{}:{}，正在执行,已耗时：{}ms，请@开发排查！", jobInfo.getTaskId(), jobInfo.getFactoryCode(), duration);
                try {
                    sendAlert(jobInfo);
                    jobInfo.sendCounter();
                } catch (Exception e) {
                    log.error("长任务监控告警出现异常：{}", e.getCause().toString());
                }
            }
        }

    }

    /**
     * 任务完成
     * 移除任务
     *
     * @param jobInfo
     */
    private void removeTask(JobInfo jobInfo) {
        Iterator<JobInfo> iterator = taskPool.iterator();
        while (iterator.hasNext()) {
            JobInfo next = iterator.next();
            if (StringUtils.equals(next.getTaskId(), jobInfo.getTaskId())) {
                taskPool.remove(jobInfo);
            }
        }
    }

    /**
     * 发送告警
     *
     * @param jobInfo
     */
    private void sendAlert(JobInfo jobInfo) {
        String factoryCode = jobInfo.getFactoryCode();
        this.setContext(factoryCode);
        AlertEnums.Level riskLevel = evaluateRisk(jobInfo);
        try {
            this.messageAlertService.sendAlert(jobInfo.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.clearContext();
        }
        log.warn("warn...定时任务执行时间过长告警发送成功");
    }

    /**
     * 评估当前任务的风险等级
     * @param jobInfo
     */
    private AlertEnums.Level evaluateRisk(JobInfo jobInfo) {
        AtomicInteger sendCount = jobInfo.getSendCount();
        if (sendCount.get()>=RISK_THRESHOLD){
            return AlertEnums.Level.HIGH;
        }
        return AlertEnums.Level.MID;
    }

    /**
     * 设置上下文
     *
     * @param factoryCode
     */
    public void setContext(String factoryCode) {

    }

    /**
     * 清空当前任务上线文：
     * 1-用户上下文
     * 2-切换数据源
     */
    public void clearContext() {

    }
}


