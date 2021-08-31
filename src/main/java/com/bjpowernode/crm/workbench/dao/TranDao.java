package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.vo.TranListVo;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<Tran> pageList(Map<String, Object> map);

    Tran detail(String id);

    int pageListTotal();

    Tran edit(String tranId);

    int update(Tran t);

    int delete(String tranId);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();

    List<Tran> showTran();

    int deleteTran(String tranId);
}
