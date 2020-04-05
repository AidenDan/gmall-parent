package com.gmall.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.to.CommonResult;
import com.gmall.ums.entity.Member;
import com.gmall.ums.entity.MemberLevel;
import com.gmall.ums.service.MemberLevelService;
import com.gmall.ums.service.MemberService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Aiden
 * @version 1.0
 * @description 处理会员级别信息的controller
 * @date 2020-3-2 13:28:46
 */
@CrossOrigin
@RestController
public class MemberLevelController {
    @Reference
    MemberLevelService memberLevelService;

    @GetMapping("/memberLevel/list")
    public Object getAllMemberLevel(){
        List<MemberLevel> list = memberLevelService.list();
        return new CommonResult().success(list);
    }
}
