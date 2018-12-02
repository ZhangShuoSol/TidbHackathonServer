package com.tidbhack.backend.service;

import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.dto.TableIndex;

import java.util.List;
import java.util.Map;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public interface ExplainService {
    Response explain(String sql);

    String getTable(String name);

    //获取表的索引
    List<Map<String, Object>> getTableIndexs(String name);
    //添加表的索引
    String addTableIndexs(TableIndex tableIndex);
    //删除表的索引
    String deleteTableIndexs(String table,String columns);
}
