package com.springboot.lamp.controller;

import com.springboot.lamp.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/post-api")
public class PostController {

    @PostMapping("/domain")
    public String postExample(){
        return "hello";
    }

    @PostMapping("/member")
    public String postMember(@RequestBody Map<String, String> map){
        StringBuilder sb = new StringBuilder();

        map.entrySet().forEach(mp ->{
            sb.append(mp.getKey() +  " " + mp.getValue());
        });
        return sb.toString();
    }

    @PostMapping("/member2")
    public String postmember2(@RequestBody MemberDto memberDto){
        return memberDto.toString();
    }

    @ResponseBody
    @PostMapping("/member3")
    public ResponseEntity<MemberDto> postmember3(@RequestBody MemberDto memberDto){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(memberDto);
    }
}
