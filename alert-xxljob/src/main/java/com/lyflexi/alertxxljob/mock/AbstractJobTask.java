package com.lyflexi.alertxxljob.mock;

import com.lyflexi.alertxxljob.monitor.JobInfo;
import com.lyflexi.alertxxljob.monitor.JobMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:07
 */
@Component
public abstract class AbstractJobTask {

    private static final Logger log = LoggerFactory.getLogger(AbstractJobTask.class);


    @Autowired
    private JobMonitor jobMonitor;
    /**
     * 设置当前任务上线文：
     *      1-用户上下文
     *      2-切换数据源
     *      3-启动监控
     * @param callerJobName
     * @param jobParam
     */
    protected JobInfo setContext (String callerJobName,String jobParam) {
        JobInfo jobInfo = monitorRegister(callerJobName, jobParam);
        return jobInfo;
    }
    /**
     * 清空当前任务上线文：
     *      1-用户上下文
     *      2-切换数据源
     *      3-结束监控
     */
    protected void clearContext (JobInfo jobInfo) {
        finishMonitor(jobInfo);
    }

    /**
     * 注册任务于监控对象池
     * @param callerJobName
     * @param jobParam
     * @return 任务id【xxljob名称+工厂编码】
     */
    private JobInfo monitorRegister (String callerJobName,String jobParam) {
        JobFactoryParam param = JobParamConverter.parseAndCheckJobParam(jobParam, JobFactoryParam.class);
        String factoryCode = param.getFactoryCode();
        String taskId = UUID.randomUUID().toString();
        taskId = new StringJoiner(":").add(taskId).add(callerJobName).add(factoryCode).toString() ;
        JobInfo jobInfo = new JobInfo(taskId,jobParam,factoryCode,callerJobName);
        jobMonitor.registerTask(jobInfo);
        return jobInfo;
    }

    /**
     * 结束任务
     * @param jobInfo
     * @return
     */
    private void finishMonitor (JobInfo jobInfo) {
        if (Objects.nonNull(jobInfo)){
            jobInfo.turnToFinish();
        }
    }

}