package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Override
    public PageinationVo<Clue> pageList(Map<String, Object> map) {
        int total=clueDao.getTotalByCondition();

        List<Clue> dataList=clueDao.getClueCondition(map);
        PageinationVo<Clue> vo=new PageinationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean save(Clue clue) {
        boolean flag=true;
        int count=clueDao.save(clue);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue=clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag=true;
        int count=clueActivityRelationDao.unbund(id);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {
        boolean flag=true;
        for (String actIds:activityIds)
        {
            //取得的每一个activityId和clueId做关联
            ClueActivityRelation car=new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(actIds);
            car.setClueId(clueId);
            //添加关联操作
            int count=clueActivityRelationDao.bund(car);
            if (count!=1)
            {
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime= DateTimeUtil.getSysTime();
        boolean flag=true;
        //1.根据线索ID获取线索对象（线索当中封装了线索对象）
        Clue c=clueDao.getById(clueId);
        //2.通过线索对象提取客户信息，当该用户不存在的时候，新建客户（根据公司的名字精确匹配，判断该用户是否存在）
        String company=c.getCompany();
        Customer customer =customerDao.getCustomerByName(company);
        if (customer==null)//为空说明以前没有这个客户，需要新建一个
        {
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(c.getAddress());
            customer.setWebsite(c.getWebsite());
            customer.setPhone(c.getPhone());
            customer.setOwner(c.getOwner());
            customer.setNextContactTime(c.getNextContactTime());
            customer.setName(company);
            customer.setDescription(c.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(c.getContactSummary());
            //添加客户
            int count1=customerDao.save(customer);
            if (count1!=1)
            {
                flag=false;
            }
        }
        //3.通过线索对象提取联系人信息，保存联系人
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(c.getSource());
        contacts.setOwner(c.getOwner());
        contacts.setNextContactTime(c.getNextContactTime());
        contacts.setMphone(c.getMphone());
        contacts.setJob(c.getJob());
        contacts.setFullname(c.getFullname());
        contacts.setEmail(c.getEmail());
        contacts.setDescription(c.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(c.getContactSummary());
        contacts.setAppellation(c.getAppellation());
        contacts.setAddress(c.getAddress());
        //添加联系人
        int count2=contactsDao.save(contacts);
        if (count2!=1)
        {
            flag=false;
        }

        //4.线索备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList=clueRemarkDao.getListByClueId(clueId);
        //取出每一条线索的备注
        for (ClueRemark clueRemark:clueRemarkList) {
            String noteContent=clueRemark.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            //添加客户备注
            int count3=customerRemarkDao.save(customerRemark);
            if (count3!=1)
            {
                flag=false;
            }
            //创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            //添加联系人备注
            int count4=contactsRemarkDao.save(contactsRemark);
            if (count4!=1)
            {
                flag=false;
            }
        }
        //5."线索和市场活动"的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联列表
        List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.getListByClueId(clueId);
        //遍历每一条与市场活动关联的关联关系记录
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList)
        {
            //取出每一条的活动id activityId
            String activityId=clueActivityRelation.getActivityId();
            //创建 联系人与市场活动关联关系对象 让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            //添加联系人与市场活动关系
            int count5=contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1)
            {
                flag=false;
            }
        }

        //6.如果有创建交易需求，创建一条交易
        if (t!=null)
        {
            //t在controller封装好了信息：id,money,name,expectedDate,stage,activityId,createTime,createBy
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(contacts.getId());

            int count6=tranDao.save(t);
            if (count6!=1)
            {
                flag=false;
            }
         //7.如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th=new TranHistory();
                th.setId(UUIDUtil.getUUID());
                th.setCreateBy(createBy);
                th.setCreateTime(createTime);
                th.setMoney(t.getMoney());
                th.setStage(t.getStage());
                th.setExpectedDate(t.getExpectedDate());
                th.setTranId(t.getId());
                //添加交易历史
            int count7=tranHistoryDao.save(th);
            if (count7!=1)
            {
                flag=false;
            }
        }
        //8.删除线索备注
        for (ClueRemark clueRemark:clueRemarkList)//第四步调用过dao层，直接用这个集合
        {
            int count8=clueRemarkDao.delete(clueRemark);
            if (count8!=1)
            {
                flag=false;
            }
        }

        //9.删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList)//第五步调用过dao层，直接用这个集合
        {
            int count9=clueActivityRelationDao.delete(clueActivityRelation);
            if (count9!=1)
            {
                flag=false;
            }
        }

        //10.删除线索
        int count10=clueDao.delete(clueId);
        if (count10!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> update(String id) {
        //获得用户表
        List<User> uList=userDao.getUserList();
        //获得线索
        Clue c=clueDao.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("uList",uList);
        map.put("c",c);
        return map;
    }

    @Override
    public boolean updateList(Clue c) {
        boolean flag=true;
        int count=clueDao.updateList(c);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean deleteList(String clueId) {
        boolean flag;
        int count=clueDao.deleteClue(clueId);
        int count1=clueActivityRelationDao.deleteClue(clueId);
        int count2=clueRemarkDao.deleteClue(clueId);
        if (count==1 & count1==1 & count2==1)
        {
            flag=true;
        }else {
            flag=false;
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getClueRemarkList(String clueId) {
        List<ClueRemark> list=clueRemarkDao.getClueRemarkList(clueId);
        return list;
    }

    @Override
    public boolean saveClueRemark(ClueRemark cr) {
        boolean flag=true;
        int count=clueRemarkDao.saveClueRemark(cr);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean updateClueRemark(ClueRemark cr) {
        boolean flag=true;
        int count=clueRemarkDao.updateClueRemark(cr);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean deleteClueRemark(String id) {
        boolean flag=true;
        int count=clueRemarkDao.deleteClueRemark(id);
        if (count!=1)
        {
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        List<Map<String, Object>> dataList=clueDao.getCharts();
        Map<String, Object> map=new HashMap<>();
        map.put("dataList",dataList);
        return map;
    }

}
