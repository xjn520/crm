package com.bjpowernode.crm.workbench.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactsController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path= request.getServletPath();
        if ("/workbench/contacts/xxx.dao".equals(path))
        {
            //xxx(request,response);
        }else if ("/workbench/contacts/xxx.dao".equals(path))
        {
            //xxx(request,response);
        }
    }
}
