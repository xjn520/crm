package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if ("/workbench/activity/getUserList.dao".equals(path))
        {
            getUserList(request,response);
        }else if ("/workbench/activity/save.dao".equals(path))
        {
                save(request,response);
        }else if ("/workbench/activity/pageList.dao".equals(path))
        {
            pageList(request,response);
        }else if ("/workbench/activity/delete.dao".equals(path))
        {
            delete(request,response);
        }else if ("/workbench/activity/getUserListandActivity.dao".equals(path))
        {
            getUserListandActivity(request,response);
        }else if ("/workbench/activity/update.dao".equals(path))
        {
            update(request,response);
        }else if ("/workbench/activity/detail.dao".equals(path))
        {
            detail(request,response);
        }else if ("/workbench/activity/getRemarkListByAid.dao".equals(path))
        {
            getRemarkListByAid(request,response);
        }else if ("/workbench/activity/deleteRemark.dao".equals(path))
        {
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.dao".equals(path))
        {
            saveRemark(request,response);
        }else if ("/workbench/activity/updateRemark.dao".equals(path))
        {
            updateRemark(request,response);
        }else if ("/workbench/activity/getActivityCharts.dao".equals(path))
        {
            getActivityCharts(request,response);
        }
    }

    private void getActivityCharts(HttpServletRequest request, HttpServletResponse response) {
        //获得市场活动的统计图数据
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=as.getActivityCharts();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        String editTime= DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="1";
        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.updateRemark(ar);

        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        //
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String noteContent=request.getParameter("noteContent");
        String activityId=request.getParameter("activityId");
        String id=UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.saveRemark(ar);

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",flag);
        map.put("ar",ar);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String activityId=request.getParameter("activityId");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> list=as.getRemarkListByAid(activityId);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity=as.detail(id);
        request.setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");

        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.update(a);
        //{"success":"flag"}
       PrintJson.printJsonFlag(response,flag);

    }

    private void getUserListandActivity(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=activityService.getUserListandActivity(id);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String id[]=request.getParameterValues("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.delete(id);
        //{"success":"flag"}
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount=(pageNo-1)*pageSize;
        //把上面这些数据打包到Map，交给service层
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PageinationVo<Activity> vo =activityService.pageList(map);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,vo);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService=(UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list=userService.getUserList();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService activityService=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.save(a);
        //PrintJson.printJsonObj(response,flag); //{"success":flag}
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
