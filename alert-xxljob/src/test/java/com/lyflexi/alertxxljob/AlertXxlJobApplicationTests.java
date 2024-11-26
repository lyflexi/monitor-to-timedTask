package com.lyflexi.alertxxljob;

import com.lyflexi.alertxxljob.mock.MockMonitorTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlertXxlJobApplicationTests {
@Autowired
    MockMonitorTask mockMonitorTask;
    @Test
    void contextLoads() throws Exception {
        mockMonitorTask.mockMonitorTask();
        Thread.sleep(10000L);
    }

}
