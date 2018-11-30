package com.tidbhack.backend.service;

import com.tidbhack.backend.datasource.Explain;
import com.tidbhack.backend.datasource.ExplainRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
@Service
public class ExplainServiceImpl implements ExplainService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String explain(String sql) {
        List<Explain> explainList = jdbcTemplate.query(sql, new ExplainRowMapper());

        for (Explain explain: explainList) {
            System.out.println(explain.getId());
            System.out.println(explain.getCount());
            System.out.println(explain.getTask());
            System.out.println(explain.getOperator_info());
            System.out.println(explain.getExecution_info());
        }

        return "";
    }
}
