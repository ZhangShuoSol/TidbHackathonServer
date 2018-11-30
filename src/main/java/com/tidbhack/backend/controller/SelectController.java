package com.tidbhack.backend.controller;

import com.tidbhack.backend.dto.Statement;
import com.tidbhack.backend.service.ExplainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String select(
            @ApiParam(value = "执行语句", required = true)
            @RequestBody Statement statement

    ) {

        String result = explainService.explain(statement.getSql());
        return result;
    }
}
