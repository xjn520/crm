package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) resp;

        String path= request.getServletPath();
        //不应该被拦截的资源，放行
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path))
        {
            filterChain.doFilter(req,resp);
        }else {//其他资源
            HttpSession session= request.getSession();
            User user=(User) session.getAttribute("user");
            if (user!=null)//表示登录过
            {
                filterChain.doFilter(req,resp);
            }else {//表示没有登录过
                //重定向到登录页
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
