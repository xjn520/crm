package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<String> getCustomerName(String name);

    boolean save(Customer customer);

    Map<String, Object> CustomerList(Map<String, Object> map);

    Customer update(String customerId);

    boolean updateCustomer(Customer c);

    boolean delete(String customerId);

    Customer detail(String id);

    boolean saveRemark(CustomerRemark cr);

    List<CustomerRemark> showRemarkList(String customerId);

    boolean updateRemark(CustomerRemark cr);

    boolean deleteRemark(String remarkId);
}
