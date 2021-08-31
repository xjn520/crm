package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        //过滤POST请求中文乱码的问题
        req.setCharacterEncoding("UTF-8");
        //过滤响应流响应乱码
        resp.setContentType("text/html;charset=utf-8");
        //将请求放行
        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
