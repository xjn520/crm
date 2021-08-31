package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    List<CustomerRemark> showRemarkList(String customerId);

    int updateRemark(CustomerRemark cr);

    int deleteRemark(String remarkId);
}
