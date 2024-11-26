package com.lyflexi.alertxxljob.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:20
 */

public interface IMessageAlertService {
    public void sendAlert(String body);
}
