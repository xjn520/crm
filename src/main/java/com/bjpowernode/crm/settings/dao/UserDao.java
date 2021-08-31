package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String,String> map) throws LoginException;
    List<User> getUserList();

}
