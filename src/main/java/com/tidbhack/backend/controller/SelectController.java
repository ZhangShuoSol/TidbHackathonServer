package com.tidbhack.backend.controller;

import com.tidbhack.backend.domain.Node;
import com.tidbhack.backend.dto.Response;
import com.tidbhack.backend.dto.Statement;
import com.tidbhack.backend.service.ExplainService;
import com.tidbhack.backend.utils.FormatSqlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wenbinsong on 2018/11/30.
 */

@RestController
@Api(value = "/select", description = "Select 语句执行接口")
public class SelectController {

    @Autowired
    private ExplainService explainService;

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ApiOperation(value = "select 语句执行接口", notes = "select 语句执行接口")
    public Response select(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement

    ) {

        Response res = explainService.explain("EXPLAIN ANALYZE " + statement.getSql());

        return res;
    }

    @RequestMapping(value = "/formatsql", method = RequestMethod.POST)
    @ApiOperation(value = "获取格式化后的SQL", notes = "获取格式化后的SQL")

    public String formatSql(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement
    ) {
        return FormatSqlUtils.getFormatSql(statement.getSql());
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    @ApiOperation(value = "查找表接口", notes = "查找表接口")

    public String getTable(
            @ApiParam(value = "表名", required = true)
            @RequestParam String tablename
    ) {
        String result = explainService.getTable(tablename);
        return result;
    }
}
