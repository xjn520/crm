package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if ("/workbench/customer/getUserList.dao".equals(path))
        {
            getUserList(request,response);
        }else if ("/workbench/customer/save.dao".equals(path))
        {
            save(request,response);
        }else if ("/workbench/customer/CustomerList.dao".equals(path))
        {
            CustomerList(request,response);
        }else if ("/workbench/customer/update.dao".equals(path))
        {
            update(request,response);
        }else if ("/workbench/customer/updateCustomer.dao".equals(path))
        {
            updateCustomer(request,response);
        }else if ("/workbench/customer/delete.dao".equals(path))
        {
            delete(request,response);
        }else if ("/workbench/customer/detail.dao".equals(path))
        {
            detail(request,response);
        }else if ("/workbench/customer/saveRemark.dao".equals(path))
        {
            saveRemark(request,response);
        }else if ("/workbench/customer/showRemark.dao".equals(path))
        {
            showRemark(request,response);
        }else if ("/workbench/customer/updateRemark.dao".equals(path))
        {
            updateRemark(request,response);
        }else if ("/workbench/customer/deleteRemark.dao".equals(path))
        {
            deleteRemark(request,response);
        }else if ("/workbench/customer/showTran.dao".equals(path))
        {
            showTran(request,response);
        }else if ("/workbench/customer/deleteTran.dao".equals(path))
        {
            deleteTran(request,response);
        }
    }

    private void deleteTran(HttpServletRequest request, HttpServletResponse response) {
        String tranId=request.getParameter("tranId");
        TranService ts=(TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.deleteTran(tranId);
        PrintJson.printJsonFlag(response,flag);
    }

    private void showTran(HttpServletRequest request, HttpServletResponse response) {
        TranService tr=(TranService) ServiceFactory.getService(new TranServiceImpl());
        ServletContext application =request.getServletContext();
        Map<String,Object> pMap=(Map<String,Object>)application.getAttribute("pMap");

        List<Tran> list=tr.showTran();
        for (Tran tran:list)
        {
            String possibility= (String) pMap.get(tran.getStage());
            tran.setPossibility(possibility);
        }
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String remarkId=request.getParameter("remarkId");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag=cs.deleteRemark(remarkId);
        PrintJson.printJsonFlag(response,flag);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String remarkId=request.getParameter("remarkId");
        String noteContent=request.getParameter("noteContent");
        CustomerRemark cr=new CustomerRemark();
        cr.setId(remarkId);
        cr.setNoteContent(noteContent);

        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag=cs.updateRemark(cr);
        PrintJson.printJsonFlag(response,flag);
    }

    private void showRemark(HttpServletRequest request, HttpServletResponse response) {
        String customerId=request.getParameter("customerId");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<CustomerRemark> list=cs.showRemarkList(customerId);
        String user=((User)request.getSession().getAttribute("user")).getName();
        Map<String,Object> map=new HashMap<>();
        map.put("list",list);
        map.put("user",user);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String customerId=request.getParameter("customerId");
        String noteContent=request.getParameter("noteContent");

        CustomerRemark cr=new CustomerRemark();
        cr.setId(UUIDUtil.getUUID());
        cr.setNoteContent(noteContent);
        cr.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        cr.setCreateTime(DateTimeUtil.getSysTime());
        cr.setEditFlag("0");
        cr.setCustomerId(customerId);

        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag=cs.saveRemark(cr);
        PrintJson.printJsonFlag(response,flag);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
        String id=request.getParameter("id");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        Customer c=cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request,response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String customerId=request.getParameter("customerId");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag=cs.delete(customerId);
        PrintJson.printJsonFlag(response,flag);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String website=request.getParameter("website");
        String phone=request.getParameter("phone");
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String description=request.getParameter("description");
        String address=request.getParameter("address");
        Customer c=new Customer();
        c.setId(id);
        c.setOwner(owner);
        c.setName(name);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setEditBy(editBy);
        c.setEditTime(editTime);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setDescription(description);
        c.setAddress(address);

        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag=cs.updateCustomer(c);
        PrintJson.printJsonFlag(response,flag);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String customerId=request.getParameter("customerId");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        Customer c=cs.update(customerId);

        UserService us=(UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();

        Map<String,Object> map=new HashMap<>();
        map.put("uList",uList);
        map.put("c",c);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map);
    }

    private void CustomerList(HttpServletRequest request, HttpServletResponse response) {
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");

        int pageNo=Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
        int skipCount=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        Map<String,Object> map1=cs.CustomerList(map);
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,map1);
    }


    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String website=request.getParameter("website");
        String phone=request.getParameter("phone");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String description=request.getParameter("description");
        String address=request.getParameter("address");

        Customer customer=new Customer();
        customer.setId(id);
        customer.setOwner(owner);
        customer.setName(name);
        customer.setWebsite(website);
        customer.setPhone(phone);
        customer.setCreateBy(createBy);
        customer.setCreateTime(createTime);
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag=cs.save(customer);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list=us.getUserList();
        response.setContentType("text/html;charset=utf-8");
        PrintJson.printJsonObj(response,list);
    }
}
