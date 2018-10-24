package com.itmayi.service.api.impl;

import com.itmayi.api.service.TestApiService;
import com.itmayi.base.BaseApiService;
import com.itmayi.base.ResponseBase;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService {
    @Override
    public Map<String, Object> test(Integer id, String name) {
        Map<String,Object> map = new HashMap<>();
        map.put("rtnCode","200");
        map.put("rtnMsg","success");
        map.put("data",new String[]{"123","321",id+"",name});
        return map;
    }

    @Override
    public ResponseBase testResponseBase() {
        return setResultSuccess();
    }

    @Override
    public ResponseBase testRedis(String key,String value) {
        System.out.println(1);
        baseRedisService.setString(key,value);
        return setResultSuccess();
    }
}
