package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int deleteClue(String clueId);

    List<ClueRemark> getClueRemarkList(String clueId);

    int saveClueRemark(ClueRemark cr);

    int updateClueRemark(ClueRemark cr);

    int deleteClueRemark(String id);
}
