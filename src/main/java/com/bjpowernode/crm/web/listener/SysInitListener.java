package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("上下文域对象创建了");
        ServletContext application =servletContextEvent.getServletContext();
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
        * 向业务层要7个list
        *业务层应该是这样来保存数据的
        * map.put("appellationList",dvList1)   dvList里面放的是DicValue的值
        * map.put("clueStateList",dvList2)
        * map.put("stageList",dvList3)
        * ...
        * ...
        * */
        Map<String, List<DicValue>> map=ds.getAll();
        Set<String> set=map.keySet(); //该方法是取出map集合里面的键名
        for (String key:set)
        {
            application.setAttribute(key,map.get(key));
        }
        System.out.println("数据字典处理结束");

        //数据字典处理结束后，处理Stage2Possibility.properties这个文件
        /*
        处理Stage2Possibility.properties这个文件步骤
        1、解析该文件，将文件中的键值对关系处理成Java中的键值对方式（map）
                    阶段    可能性值
                Map<String,String>
        */
        //解析properties文件
        Map<String,String> pMap=new HashMap<>();

        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=rb.getKeys();
        while (e.hasMoreElements())
        {
            //阶段
            String key=e.nextElement();
            //可能性
            String value=rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
