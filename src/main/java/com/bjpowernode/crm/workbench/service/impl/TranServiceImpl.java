package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.vo.TranListVo;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    @Override
    public boolean save(Tran t, String customerName) {
        /*
        *添加交易业务
        *参数t里面没有customerId,先处理客户相关的需求
        *1、判断customerName，根据客户名称在客户表，进行精确查询
        * 如果有这个客户，取出这个客户的id，封装到t对象中
        * 如果没有这个客户，则在客户表新建一条客户信息，然后取出客户的id，封装到t中
        *
        * 2、t信息齐全，执行添加交易操作
        * 3、添加完毕后，创建一条交易历史
        * */
        boolean flag=true;
        Customer customer =customerDao.getCustomerByName(customerName);
        if (customer==null)
        {
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(t.getCreateBy());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            //添加客户
            int count1=customerDao.save(customer);
            if (count1!=1)
            {
                flag=false;
            }
        }
        //不管怎样，以上处理表示已经有客户了，客户的id就有了
        t.setCustomerId(customer.getId());

        //添加交替
        int count2=tranDao.save(t);
        if (count2!=1)
        {
            flag=false;
        }
        //添加交易历史
        TranHistory th=new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        th.setTranId(t.getId());
        int count3=tranHistoryDao.save(th);
        if (count3!=1)
        {
            flag=false;
        }

        return flag;
    }

    @Override
    public PageinationVo<Tran> pageList(Map<String, Object> map) {
        int total=tranDao.pageListTotal();
        List<Tran> list=tranDao.pageList(map);
        PageinationVo<Tran> vo=new PageinationVo<>();
        vo.setTotal(total);
        vo.setDataList(list);
        return vo;
    }

    @Override
    public Map<String, Object> edit(String tranId) {
        Map<String,Object> map=new HashMap<>();
        Tran t=tranDao.edit(tranId);

        String id=t.getId();
        String owner=t.getOwner();
         String customerOwner=t.getOwner();
         String money=t.getMoney();
         String name=t.getName();
         String expectedDate=t.getExpectedDate();
         String type=t.getType();
         String description=t.getDescription();
         String contactSummary=t.getContactSummary();

         Customer cus=customerDao.getCustomerByOwner(customerOwner);
         String customerName=cus.getName();
         map.put("id",id);
        map.put("owner",owner);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("type",type);
        map.put("description",description);
        map.put("contactSummary",contactSummary);
        map.put("customerName",customerName);
        return map;
    }

    @Override
    public boolean update(Tran t, String customerName, String contactsName) {
        boolean flag=true;
        //通过客户名找到客户的id
        Customer customer=customerDao.getCustomerByName(customerName);
        String customerId=customer.getId();
        //通过联系人名找到联系人的id
        Contacts contacts =contactsDao.getContactsByName(contactsName);
        String contactsId=contacts.getId();
        t.setCustomerId(customerId);
        t.setContactsId(contactsId);
        int count=tranDao.update(t);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean delete(String tranId) {
        boolean flag=true;
        int count=tranDao.delete(tranId);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        //改变交易阶段
        int count1=tranDao.changeStage(t);
        if (count1!=1)
        {
            flag=false;
        }
        //交易阶段改变后要生成一条交易历史
        TranHistory th=new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        int count2=tranHistoryDao.save(th);
        if (count2!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        //取得total
        int total=tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList=tranDao.getCharts();
        //将total和dataList放入到map中
        Map<String,Object> map=new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        //返回map
        return map;
    }

    @Override
    public List<Tran> showTran() {
        List<Tran> list=tranDao.showTran();
        return list;
    }

    @Override
    public boolean deleteTran(String tranId) {
        boolean flag=true;
        int count=tranDao.deleteTran(tranId);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t=tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> list=tranHistoryDao.getHistoryListByTranId(tranId);
        return list;
    }
}
