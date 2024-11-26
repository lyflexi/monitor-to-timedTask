package com.lyflexi.alertxxljob.monitor;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:10
 */

@Data
public class JobInfo {

    /**
     * 定时任务名称：
     * XXLJOB任务名称+工厂编码
     */
    private String taskId;
    /**
     * 被监控任务名称
     */
    private String callerJobName;
    /**
     * 执行参数
     */
    private String execParams;
    /**
     * 处理数据条数
     */
    private Long dataCount;
    /**
     * 执行开售时间
     */
    private LocalDateTime startTime;
    /**
     * 执行耗时：毫秒
     */
    private Long duration;
    /**
     * 任务执行状态
     * 0: 正在执行中, new出来就是0
     * 1： 完成
     */
    private Integer state;
    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 告警发送次数
     */
    private AtomicInteger sendCount;


    public JobInfo(String taskId, String execParams, String factoryCode, String callerJobName) {
        this.taskId = taskId;
        this.execParams = execParams;
        this.startTime = LocalDateTime.now();
        this.factoryCode = factoryCode;
        this.dataCount = 0L;
        this.callerJobName = callerJobName;
        sendCount = new AtomicInteger(0);
        turnToExecute();
    }

    /**
     * 计算当前耗时
     * @return
     */
    public long calDuration() {
        if (Objects.nonNull(startTime)) {
            this.duration = Duration.between(startTime, LocalDateTime.now()).toMillis();
        }
        return duration;
    }

    /**
     * 置为执行中
     */
    public void turnToExecute(){
        this.state = 0;
    }
    /**
     * 置为本轮完成
     */
    public void turnToFinish(){
        this.state = 1;
    }
    /**
     * 是否完成
     */
    public Boolean isFinish(){
        return this.state==1;
    }

    /**
     * 发送次数累加
     * @return
     */
    public Integer sendCounter(){
        return this.sendCount.addAndGet(1);
    }
}

