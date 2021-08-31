package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PageinationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ClueService {
    PageinationVo<Clue> pageList(Map<String, Object> map);

    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityIds);


    boolean convert(String clueId, Tran t, String createBy);

    Map<String, Object> update(String id);

    boolean updateList(Clue c);

    boolean deleteList(String clueId);

    List<ClueRemark> getClueRemarkList(String clueId);

    boolean saveClueRemark(ClueRemark cr);

    boolean updateClueRemark(ClueRemark cr);

    boolean deleteClueRemark(String id);

    Map<String, Object> getCharts();
}
