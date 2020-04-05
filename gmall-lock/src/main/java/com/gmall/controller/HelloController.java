package com.gmall.controller;

import com.gmall.service.HelloService;
import com.gmall.service.RedissonLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-4 12:32:43
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @Autowired
    RedissonLockService redissonLockService;

    @GetMapping("/hello")
    public String hello(){
        helloService.incrDistribute();
        return "ok";
    }

    @GetMapping("/hello2")
    public String hello2(){
        helloService.useRedisssonForLock();
        return "ok";
    }

    //Redisson分布式读写锁测试
    @GetMapping("/hello3")
    public String hello3(){
        return redissonLockService.read();
    }

    @GetMapping("/hello4")
    public String hello4(){
        return redissonLockService.write();
    }

    //闭锁测试
    @GetMapping("/go")
    public Boolean gogogo(){
        return redissonLockService.gogogo();
    }


    @GetMapping("/suomen")
    public String suomen() throws InterruptedException {
        Boolean suomen = redissonLockService.suomen();
        return suomen?"锁门了":"门没锁";
    }

  //信号量测试 ，释放信号量，会被感知
    @GetMapping("/rc")
    public Boolean release() throws InterruptedException {
        return redissonLockService.rc();
    }

     //感知并捕获被释放的信号量
    @GetMapping("/tc")
    public Boolean park() throws InterruptedException {
        return redissonLockService.tc();
    }
}





























