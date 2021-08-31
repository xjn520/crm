package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag=true;
        int count=activityRemarkDao.updateRemark(ar);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> list=activityDao.getActivityListByClueId(clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityListByNameNotByClueId(Map<String, String> map) {
        List<Activity> list=activityDao.getActivityListByNameNotByClueId(map);
        return list;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> list=activityDao.getActivityListByName(aname);
        return list;
    }

    @Override
    public List<Activity> searchActivity(String value) {
        List<Activity> list=activityDao.getActivityListByName(value);
        return list;
    }

    @Override
    public List<Activity> searchActivityList(String value) {
        List<Activity> list=activityDao.searchActivityList(value);
        return list;
    }

    @Override
    public Map<String, Object> getActivityCharts() {
        List<Map<String,Object>> dataList=activityDao.getActivityCharts();
        Map<String,Object> map=new HashMap<>();
        map.put("dataList",dataList);
        return map;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> list=activityRemarkDao.getRemarkListByAid(activityId);
        return list;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag=true;
        int count=activityRemarkDao.deleteRemark(id);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag=true;
        int count=activityRemarkDao.saveRemark(ar);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }


    @Override
    public boolean update(Activity a) {
        boolean flag=true;
        int count=activityDao.update(a);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity=activityDao.detail(id);
        return activity;
    }

    @Override
    public boolean save(Activity activity) {
        boolean flag=true;
        int count=activityDao.save(activity);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public PageinationVo<Activity> pageList(Map<String, Object> map) {
        int total=activityDao.getTotalByCondition();//获得总条数

        //取得dataList
        List<Activity> dataList=activityDao.getActivityCondition(map);
        PageinationVo<Activity> vo=new PageinationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //将total和dataList封装到vo

        return vo;
    }

    @Override
    public boolean delete(String[] id) {
        boolean flag=true;

        //查询出需要删除的备注的数量
        int count1=activityRemarkDao.getCountByAid(id);
        //删除备注，返回受影响的条数（实际删除的数量）
        int count2=activityRemarkDao.deleteByAid(id);
        if (count1!=count2)
        {
            flag=false;
        }
        //删除市场活动
        int count3=activityDao.delete(id);
        if (count3!= id.length)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListandActivity(String id) {
        //获得用户（user表）
        List<User> uList=userDao.getUserList();
        //获得活动（activity表）
        Activity a=activityDao.getById(id);

        //返回ulist和activity
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("a",a);
        return map;
    }
}
