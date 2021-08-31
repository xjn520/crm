package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getCustomerName(String name);

    Customer getCustomerByOwner(String customerOwner);

    int getTotal();

    List<Customer> getCustomerList(Map<String, Object> map);

    Customer getCustomerById(String customerId);

    int updateCustomer(Customer c);

    int delete(String customerId);

    Customer getCustomer(String id);
}
