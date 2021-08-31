package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    List<Clue> getClueCondition(Map<String, Object> map);

    int getTotalByCondition();

    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);

    int updateList(Clue c);

    int deleteClue(String clueId);

    List<Map<String, Object>> getCharts();
}
