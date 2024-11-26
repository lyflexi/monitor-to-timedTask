package com.lyflexi.alertxxljob.mock;

import com.lyflexi.alertxxljob.monitor.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:05
 */
@Component
@Slf4j
public class MockMonitorTask extends AbstractJobTask {

    private final static String CALLER_NAME_MOCK_MONITOR_TASK = "模拟任务";


    @Scheduled(cron = "0/1 0 * * * ?")
    public void mockMonitorTask () throws Exception{
//        String jobParam = XxlJobHelper.getJobParam();
        String jobParam = "{\"factoryCode\":\"X165\",\"key1\":\"value1\",\"key2\":\"value2\"}";
        log.info(">>>>>>>>{}，当前参数：{}", CALLER_NAME_MOCK_MONITOR_TASK,jobParam);
        JobInfo jobInfo = null;
        Long dataCount = 0L;
        try {
            // 1.设置上下文
            jobInfo = super.setContext("中文任务", jobParam);
            Thread.sleep(8000);
            // 2.设置处理的数据条数
            dataCount = 1000L;
            jobInfo.setDataCount(dataCount);
        } finally {
            //3.清空上下文
            super.clearContext(jobInfo);
        }
    }
}