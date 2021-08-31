package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PageinationVo<Activity> pageList(Map<String, Object> map);


    boolean delete(String[] id);

    Map<String, Object> getUserListandActivity(String id);


    boolean update(Activity a);

    Activity detail(String id);


    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);

    List<Activity> searchActivity(String value);

    List<Activity> searchActivityList(String value);

    Map<String, Object> getActivityCharts();
}
