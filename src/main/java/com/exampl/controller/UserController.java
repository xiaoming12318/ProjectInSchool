package com.exampl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exampl.common.R;
import com.exampl.common.SendSms;
import com.exampl.constant.RedisConstantMessage;
import com.exampl.entity.User;
import com.exampl.service.UserService;
import com.exampl.util.ValidateCodeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JedisPool jedis;



    /*
    * 注册
    * */
    @PostMapping("/register")
    public R<String> register(@RequestBody User user,String phoneNumber,String code) throws ExecutionException, InterruptedException {
        //1.从redis通过手机号取出验证码
        String redisCode = jedis.getResource().get(RedisConstantMessage.SENDTYPE_REGISTER + phoneNumber);

        //3.其次还要判断验证码是否过期
        if(redisCode==null){
            return R.error("验证码过期，请重新获取");
        }
        log.info("user消息:{}", user);
        //获取密码方便md5加密
        String password = user.getPassword();
        //因为数据库有这个状态，但是用户编辑不到，所以默认就给他是1
        if (user.getStatus() == null) user.setStatus(1);
        //设置md5加密。
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        //2.把前端传过来的验证码和redis中的验证码做比对
        if (!redisCode.equals(code)){
            return R.error("验证码错误，请重新输入");
        }

        //处理异常的类在，GlobalExceptionHandler中
        //代表有数据继续执行,
        userService.save(user);
        return R.success("保存成功");
    }


    /*
    * 登录
    * */
    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request) {
        log.info("user：{}", user);

        //没数据登录失败
        if (user == null) return R.error("登录失败");

        //密码
        if (user.getPassword() == null) return R.error("密码不能为空");


        //查一条对应的数据select * from user where username=?
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User userOne = userService.getOne(queryWrapper);
        log.info("得到的数据：{}", userOne);
        //没有查到数据
        if (userOne == null) {
            return R.error("查无此用户请先注册");
        }

        //密码做加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        //密码不对登录失败
        if (!userOne.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //将id存入session'
        Long id = userOne.getId();
        request.getSession().setAttribute("user", id);

        return R.success(userOne);
    }

    /*
    * 修改个人信息
    * 记得指定泛型
    * */
    @PutMapping
    public R<String> update(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper();
        //select * from user where id=?
        queryWrapper.eq(User::getId,user.getId());
        userService.update(user,queryWrapper);
        return R.success("修改成功");
    }




}
