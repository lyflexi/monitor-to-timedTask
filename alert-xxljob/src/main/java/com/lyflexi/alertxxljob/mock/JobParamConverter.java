package com.lyflexi.alertxxljob.mock;

import com.alibaba.fastjson2.JSON;
import org.springframework.util.Assert;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:14
 */
public class JobParamConverter {

    public static <T extends JobFactoryParam> T parseAndCheckJobParam(String paramJson, Class<T> clazz) {
        T param = JSON.parseObject(paramJson, clazz);
        Assert.notNull(param, "jobParam参数为不能为空！");
        param.assertParam();
        return param;
    }
}
