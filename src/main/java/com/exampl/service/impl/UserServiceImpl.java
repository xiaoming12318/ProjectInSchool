package com.exampl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exampl.entity.User;
import com.exampl.mapper.UserMapper;
import com.exampl.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
