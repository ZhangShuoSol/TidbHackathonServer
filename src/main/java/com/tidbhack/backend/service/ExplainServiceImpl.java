package com.tidbhack.backend.service;

import com.google.gson.Gson;
import com.tidbhack.backend.datasource.Explain;
import com.tidbhack.backend.datasource.ExplainRowMapper;
import com.tidbhack.backend.domain.ExplainParser;
import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.domain.QueryResult;
import com.tidbhack.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
@Service
public class ExplainServiceImpl implements ExplainService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Response explain(String sql) {
        List<Explain> explainList = jdbcTemplate.query(sql, new ExplainRowMapper());

        ExplainParser explainParser = new ExplainParser();

        List<QueryResult> queryResults = new ArrayList<QueryResult>();
        for (Explain explain: explainList) {
            System.out.println(explain.getId());
            System.out.println(explain.getCount());
            System.out.println(explain.getTask());
            System.out.println(explain.getOperator_info());
            System.out.println(explain.getExecution_info());
            QueryResult queryResult = new QueryResult();
            queryResult.setId(explain.getId());
            queryResult.setCount(explain.getCount());
            queryResult.setTask(explain.getTask());
            queryResult.setOperatorinfo(explain.getOperator_info());
            queryResult.setOperatorinfo(explain.getExecution_info());
            queryResults.add(queryResult);
        }


        for (Explain explain: explainList) {
            explainParser.HandleNode(new Node(explain.getId(), explain.getTask(), explain.getCount(), explain.getOperator_info(), explain.getExecution_info() ));
        }

        Response response = new Response();
        response.setPlan(queryResults);
        response.setNode(explainParser.getRoot());

        return response;
    }
}
