package com.springboot.lamp.controller;

import com.springboot.lamp.dto.MemberDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/get-api")
public class HelloController {
private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
    @GetMapping("/variable1/{variable}")
    public String getVariable1(@PathVariable String variable){
        LOGGER.info("varialble : {}", variable);
        return variable;
    }
    @GetMapping("/variable2/{variable}")
    public String getVariable2(@PathVariable("variable") String var){
        return var;
    }

    @ApiOperation(value="get method example", notes="@RequestParam 을 활용한 GET Method")
    @GetMapping("/request1")
    public String getRequest1(
           @ApiParam(value="이름", required = true) @RequestParam String name,
           @ApiParam(value="이메일", required = true)  @RequestParam String email,
           @ApiParam(value="organization ", required = true) @RequestParam String organization
            ){

        return name+ " " + email+ " " + organization;
    }
    @GetMapping("/request2")
    public String getRequest2(@RequestParam Map<String, String> param){
        StringBuilder sb = new StringBuilder();

        param.entrySet().forEach(map ->{
            sb.append(map.getKey() + " " + map.getValue());
        });
        return sb.toString();
    }

    @GetMapping("/request3")
    public String getRequest3(MemberDto memberDto){
        return memberDto.toString();
    }
}
