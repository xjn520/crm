package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.vo.TranListVo;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    PageinationVo<Tran> pageList(Map<String, Object> map);

    Map<String, Object> edit(String tranId);

    boolean update(Tran t, String customerName, String contactsName);

    boolean delete(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();

    List<Tran> showTran();

    boolean deleteTran(String tranId);
}
