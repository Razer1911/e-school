package org.jeecg.modules.demo.signin.rule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignInCodeRule implements IFillRuleHandler {
    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        //生成时间戳
        String timestamp = DateUtil.format(new Date(), "yyyyMMddHHmm");

        RedisAtomicLong entityIdCounter = new RedisAtomicLong("Q" + timestamp, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long increment = entityIdCounter.getAndIncrement();
        if (increment == 0) {
            //初始设置过期时间
            entityIdCounter.expire(5, TimeUnit.MINUTES);
        }

        return StrFormatter.format("D{}{}", timestamp, NumberUtil.decimalFormat("0000", increment));
    }

}
