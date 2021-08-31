package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> sList=customerDao.getCustomerName(name);
        return sList;
    }

    @Override
    public boolean save(Customer customer) {
        boolean flag=true;
        int count= customerDao.save(customer);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> CustomerList(Map<String, Object> map) {
        int total=customerDao.getTotal();
        List<Customer> dataList=customerDao.getCustomerList(map);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("dataList",dataList);
        return map1;
    }

    @Override
    public Customer update(String customerId) {
        Customer c=customerDao.getCustomerById(customerId);
        return c;
    }

    @Override
    public boolean updateCustomer(Customer c) {
        boolean flag=true;
        int count=customerDao.updateCustomer(c);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean delete(String customerId) {
        boolean flag=true;
        int count=customerDao.delete(customerId);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Customer detail(String id) {
        Customer c=customerDao.getCustomer(id);
        return c;
    }

    @Override
    public boolean saveRemark(CustomerRemark cr) {
        boolean flag=true;
        int count=customerRemarkDao.save(cr);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public List<CustomerRemark> showRemarkList(String customerId) {
        List<CustomerRemark> list=customerRemarkDao.showRemarkList(customerId);
        return list;
    }

    @Override
    public boolean updateRemark(CustomerRemark cr) {
        boolean flag=true;
        int count=customerRemarkDao.updateRemark(cr);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean deleteRemark(String remarkId) {
        boolean flag=true;
        int count=customerRemarkDao.deleteRemark(remarkId);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }


}
