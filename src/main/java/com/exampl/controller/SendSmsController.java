package com.exampl.controller;

import com.exampl.common.R;
import com.exampl.common.SendSms;
import com.exampl.constant.RedisConstantMessage;
import com.exampl.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/sendSms")
@Slf4j
public class SendSmsController {

    @Autowired
    private SendSms sendSms;

    @Autowired
    private JedisPool jedisPool;


    @PostMapping("/{phoneNumber}")
    public R<String> sendMsgForRegis(@PathVariable String phoneNumber){
        //随机生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //发送消息
        try {
            sendSms.sendSms(phoneNumber,SendSms.RegisterCode,code);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return R.error("执行异常");
        } catch (InterruptedException e) {
            return R.error("中断异常");
        }
        log.info(code.toString());
        //将短信存入redis设置60秒过期时间
        jedisPool.getResource().setex(RedisConstantMessage.SENDTYPE_REGISTER+phoneNumber,60,code.toString());

        return R.success("发送验证码成功");
    }
}
