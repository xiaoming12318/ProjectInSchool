package com.exampl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.time.LocalDateTime;

//data内默认包含getter和setter方法，有参和无参构造方法,还有tostring方法

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String sex;
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;
    private Integer phoneNumber;
    private String address;


}