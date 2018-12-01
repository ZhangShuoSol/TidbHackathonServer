package com.tidbhack.backend.service;

import com.google.gson.Gson;
import com.tidbhack.backend.datasource.Explain;
import com.tidbhack.backend.datasource.ExplainRowMapper;
import com.tidbhack.backend.domain.*;
import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wenbinsong on 2018/11/30.
 */
@Service
public class ExplainServiceImpl implements ExplainService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Response explain(String sql) {
        System.out.println(sql);
        List<Explain> explainList = jdbcTemplate.query(sql, new ExplainRowMapper());

        ExplainParser explainParser = new ExplainParser();

        List<QueryResult> queryResults = new ArrayList<QueryResult>();
        for (Explain explain: explainList) {
            QueryResult queryResult = new QueryResult();
            queryResult.setId(explain.getId());
            queryResult.setCount(explain.getCount());
            queryResult.setTask(explain.getTask());
            queryResult.setOperatorinfo(explain.getOperator_info());
            queryResult.setOperatorinfo(explain.getExecution_info());
            queryResult.setUuid(explain.getUuid());
            queryResults.add(queryResult);
        }


        for (Explain explain: explainList) {
            explainParser.HandleNode(new Node(explain.getUuid(), explain.getId(), explain.getTask(), explain.getCount(), explain.getOperator_info(), explain.getExecution_info() ));
        }

        Response response = new Response();
        response.setPlan(queryResults);
        response.setNode(explainParser.getRoot());

        ExplainContextInstance explainContextInstance = ExplainContextInstance.getInstance();
        ExplainContext explainContext = new ExplainContext();
        explainContext.setNode(explainParser.getRoot());
        explainContext.setPlan(queryResults);
        String uuid = UUIDGenerator.getUUID32();
        explainContextInstance.addContext(uuid, explainContext);

        response.setUuid(uuid);
        return response;
    }

    @Override
    public String getTable(String name) {

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from " + name + " limit 0");
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        Map<String,String> fieldMap = new HashMap<String,String>();
        for (int i = 1; i <= columnCount; i++) {
            fieldMap.put("ColumnName", metaData.getColumnName(i));
            fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
            fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
            fieldMap.put("CatalogName", metaData.getCatalogName(i));
            fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
            fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
            fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
            fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
            fieldMap.put("SchemaName", metaData.getSchemaName(i));
            fieldMap.put("TableName", metaData.getTableName(i));
            fieldMap.put("SchemaName", metaData.getSchemaName(i));
            list.add(fieldMap);
        }
        Gson gson = new Gson();
        String result = gson.toJson(list);
        return result;
    }
}
