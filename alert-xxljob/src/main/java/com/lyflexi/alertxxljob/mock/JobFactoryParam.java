package com.lyflexi.alertxxljob.mock;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:14
 */
@Data
public class JobFactoryParam {

    private String factoryCode;

    public void assertParam () {
        Assert.notNull(this.factoryCode, "工厂编码为不能为空！");
    }
}