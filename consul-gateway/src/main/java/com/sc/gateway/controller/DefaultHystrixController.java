package com.sc.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认降级处理
 */
@RestController
public class DefaultHystrixController {

    @RequestMapping("/defaultfallback")
    public Map<String,String> defaultfallback(){
        System.out.println("goes fallback now(defaultfallback)...");
        Map<String,String> map = new HashMap<>();
        map.put("resultCode","fail");
        map.put("resultMessage","service into fallback exception!");
        map.put("resultObj","null");
        return map;
    }
    
    
    @ResponseBody
    @RequestMapping("/fallback")
    public Object fallback() {
         return "goes into fallback(/fallback)";
    }
    
}
