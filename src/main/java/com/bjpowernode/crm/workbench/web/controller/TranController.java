package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.vo.TranListVo;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import com.bjpowernode.crm.workbench.service.impl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if ("/workbench/transaction/add.dao".equals(path))
        {
            add(request,response);
        }else if ("/workbench/transaction/searchActivity.dao".equals(path))
        {
            searchActivity(request,response);
        }else if ("/workbench/transaction/searchContacts.dao".equals(path))
        {
            searchContacts(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path))
        {
            getCustomerName(request,response);
        }else if ("/workbench/transaction/save.do".equals(path))
        {
            save(request,response);
        }else if ("/workbench/transaction/pageList.do".equals(path))
        {
            pageList(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path))
        {
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path))
        {
            getHistoryListByTranId(request,response);
        }else if ("/workbench/transaction/edit.do".equals(path))
        {
            edit(request,response);
        }else if ("/workbench/transaction/searchActivityList.do".equals(path))
        {
            searchActivityList(request,response);
        }else if ("/workbench/transaction/update.do".equals(path))
        {
            update(request,response);
        }else if ("/workbench/transaction/delete.do".equals(path))
        {
            delete(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path))
        {
            changeStage(request,response);
        }else if ("/workbench/transaction/getCharts.do".equals(path))
        {
            getCharts(request,response);
        }
    }


    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        //取得交易阶段的数量统计图表的数据
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map=ts.getCharts();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String stage=request.getParameter("stage");
        String money=request.getParameter("money");
        String expectedDate=request.getParameter("expectedDate");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();

        Tran t=new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditTime(editTime);
        t.setEditBy(editBy);
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.changeStage(t);
        //处理可能性
        Map<String,String> pMap=(Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("t",t);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String tranId=request.getParameter("tranId");
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.delete(tranId);
        PrintJson.printJsonFlag(response,flag);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("进入了修改方法");
        String tranId=request.getParameter("tranId");
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source=request.getParameter("source");
        String activityId=request.getParameter("activityId");

        String contactsName=request.getParameter("contactsName");

        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        Tran  t=new Tran();
        t.setId(tranId);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setDescription(description);
        t.setNextContactTime(nextContactTime);
        t.setContactSummary(contactSummary);
        t.setActivityId(activityId);
        t.setEditTime(DateTimeUtil.getSysTime());
        t.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        System.out.println(contactsName);
        System.out.println(customerName);
        boolean flag=ts.update(t,customerName,contactsName);
        if (flag)
        {
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void searchActivityList(HttpServletRequest request, HttpServletResponse response) {
        String value=request.getParameter("searchActivity");
        ActivityService as=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.searchActivityList(value);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }


    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String tranId=request.getParameter("tranId");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();

        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map=ts.edit(tranId);

        request.setAttribute("uList",uList);
        request.setAttribute("map",map);
        request.getRequestDispatcher("/workbench/transaction/edit.jsp").forward(request,response);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取得相应的交易历史列表");
        String tranId=request.getParameter("tranId");
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> list=ts.getHistoryListByTranId(tranId);
        //阶段和可能性对应值
        Map<String,String> pMap=(Map<String, String>) this.getServletContext().getAttribute("pMap");
        //将交易历史列表遍历
        for (TranHistory tranHistory:list)
        {
            String stage=tranHistory.getStage();
            String possibility=pMap.get(stage);
            tranHistory.setPossibility(possibility);

        }
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String id=request.getParameter("id");
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t=ts.detail(id);
        //处理一下页面的可能性那个值
        /*
        * 阶段：存放在t里面
        * 阶段和可能性之间对应的值 在pMap里面
        *
        * */
        String stage=t.getStage();
        Map<String,String> pMap=(Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility=pMap.get(stage);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");
        String owner=request.getParameter("");
        String name=request.getParameter("");
        String stage=request.getParameter("");
        String type=request.getParameter("");
        String source=request.getParameter("");
        String customerName=request.getParameter("");
        String contactName=request.getParameter("");

        int pageNo=Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("customerName",customerName);
        map.put("contactName",contactName);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        PageinationVo<Tran> vo=ts.pageList(map);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,vo);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source=request.getParameter("source");
        String activityId=request.getParameter("activityId");
        String contactsId=request.getParameter("contactsId");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");

        Tran t=new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.save(t,customerName);
        if (flag)
        {
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name=request.getParameter("name");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList=cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);
    }

    private void searchContacts(HttpServletRequest request, HttpServletResponse response) {
        String aname=request.getParameter("aname");
        ContactsService cs=(ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> list=cs.searchContacts(aname);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }

    private void searchActivity(HttpServletRequest request, HttpServletResponse response) {
        String value=request.getParameter("searchActivity");
        ActivityService as=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.searchActivity(value);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("进入了此方法");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
