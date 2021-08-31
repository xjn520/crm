package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if ("/workbench/clue/getUserList.dao".equals(path))
        {
            getUserList(request,response);
        }else if ("/workbench/clue/pageList.dao".equals(path))
        {
            pageList(request,response);
        }else if ("/workbench/clue/save.dao".equals(path))
        {
            save(request,response);
        }else if ("/workbench/clue/detail.dao".equals(path))
        {
            detail(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.dao".equals(path))
        {
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbund.dao".equals(path))
        {
            unbund(request,response);
        }else if ("/workbench/clue/getActivityListByNameNotByClueId.dao".equals(path))
        {
            getActivityListByNameNotByClueId(request,response);
        }else if ("/workbench/clue/bund.dao".equals(path))
        {
            bund(request,response);
        }else if ("/workbench/clue/getActivityListByName.dao".equals(path))
        {
            getActivityListByName(request,response);
        }
        else if ("/workbench/clue/convert.dao".equals(path))
        {
            convert(request,response);
        }else if ("/workbench/clue/update.dao".equals(path))
        {
            update(request,response);
        }else if ("/workbench/clue/updateList.dao".equals(path))
        {
            updateList(request,response);
        }else if ("/workbench/clue/deleteList.dao".equals(path))
        {
            deleteList(request,response);
        }else if ("/workbench/clue/getClueRemarkList.dao".equals(path))
        {
            getClueRemarkList(request,response);
        }
        else if ("/workbench/clue/saveClueRemark.dao".equals(path))
        {
            saveClueRemark(request,response);
        }else if ("/workbench/clue/updateClueRemark.dao".equals(path))
        {
            updateClueRemark(request,response);
        }else if ("/workbench/clue/deleteClueRemark.dao".equals(path))
        {
            deleteClueRemark(request,response);
        }else if ("/workbench/clue/getCharts.dao".equals(path))
        {
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object> map=cs.getCharts();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void deleteClueRemark(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.deleteClueRemark(id);
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateClueRemark(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");

        ClueRemark cr=new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.updateClueRemark(cr);
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveClueRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId=request.getParameter("clueId");
        String noteContent=request.getParameter("noteContent");
        String id=UUIDUtil.getUUID();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();

        ClueRemark cr=new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setCreateTime(createTime);
        cr.setCreateBy(createBy);
        cr.setClueId(clueId);

        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag=cs.saveClueRemark(cr);//{"success":"flag"}
        String json="{\"success\":\"flag\"}";
        PrintWriter out= response.getWriter();
        out.print(json);
        out.close();
    }

    private void getClueRemarkList(HttpServletRequest request, HttpServletResponse response) {
        String clueId=request.getParameter("clueId");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> list=cs.getClueRemarkList(clueId);
        String user=((User)request.getSession().getAttribute("user")).getName();
        Map<String,Object> map=new HashMap<>();
        map.put("list",list);
        map.put("user",user);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void deleteList(HttpServletRequest request, HttpServletResponse response) {
        String clueId=request.getParameter("clueId");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.deleteList(clueId);
        //{"success":"flag"}
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateList(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String appellation=request.getParameter("appellation");
        String fullname=request.getParameter("name");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");
        String state=request.getParameter("state");
        String source=request.getParameter("source");

        Clue c=new Clue();

        c.setId(id);
        c.setOwner(owner);
        c.setCompany(company);
        c.setAppellation(appellation);
        c.setFullname(fullname);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        c.setState(state);
        c.setSource(source);
        c.setEditTime(DateTimeUtil.getSysTime());
        c.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.updateList(c);
        //{"success":"flag"}
        String json="{\"success\":\"flag\"}";
        try {
            PrintWriter out= response.getWriter();
            out.print(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object> map=cs.update(id);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String clueId=request.getParameter("clueId");
        //接收是否需要创建交易的标记
        String flag=request.getParameter("flag");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        Tran t=null;
        if ("a".equals(flag))
        {
            t=new Tran();
            //接收交易表单中的数据
            String money=request.getParameter("money");
            String name=request.getParameter("name");
            String expectedDate=request.getParameter("expectedDate");
            String stage=request.getParameter("stage");
            String activityId=request.getParameter("activityId");
            String id=UUIDUtil.getUUID();
            String createTime=DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1=cs.convert(clueId,t,createBy);
        if (flag1)
        {
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname=request.getParameter("aname");
        ActivityService as=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.getActivityListByName(aname);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId=request.getParameter("clueId");
        String activityIds[]=request.getParameterValues("activityId");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.bund(clueId,activityIds);
        System.out.println(flag);
        PrintWriter out= response.getWriter();
        out.print(flag);
        out.close();

    }

    private void getActivityListByNameNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        String aname=request.getParameter("aname");
        String clueId=request.getParameter("clueId");
        Map<String,String> map=new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        ActivityService as=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.getActivityListByNameNotByClueId(map);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id查询关联的市场活动列表");
        String clueId=request.getParameter("clueId");
        ActivityService as=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.getActivityListByClueId(clueId);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=cs.detail(id);
        System.out.println(clue);
        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id= UUIDUtil.getUUID();
        String fullname=request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");

        Clue clue=new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.save(clue);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");
        String name=request.getParameter("name");
        String company=request.getParameter("company");
        String phone=request.getParameter("phone");
        String source=request.getParameter("source");
        String owner=request.getParameter("owner");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        int pageNo=Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        ClueService cs=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PageinationVo<Clue> vo =cs.pageList(map);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,vo);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService=(UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list=userService.getUserList();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }
}
