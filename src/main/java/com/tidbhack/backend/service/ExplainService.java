package com.tidbhack.backend.service;

import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.dto.Response;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public interface ExplainService {
    Response explain(String sql);

    String getTable(String name);
}
