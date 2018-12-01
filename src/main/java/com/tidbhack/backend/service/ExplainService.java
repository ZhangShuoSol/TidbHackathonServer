package com.tidbhack.backend.service;

import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.dto.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public interface ExplainService {
    Response explain(String sql);

    String getTable(String name);

    //获取表的索引
    String getTableIndexs(String name);

}
