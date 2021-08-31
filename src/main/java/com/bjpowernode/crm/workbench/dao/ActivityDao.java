package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
   int save(Activity activity);

   int getTotalByCondition();

   List<Activity> getActivityCondition(Map<String, Object> map);


   int delete(String[] id);

    Activity getById(String id);


    int update(Activity a);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);

    List<Activity> searchActivityList(String value);

    List<Map<String, Object>> getActivityCharts();
}
