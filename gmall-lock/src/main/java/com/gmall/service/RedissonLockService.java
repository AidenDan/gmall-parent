package com.gmall.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-5 15:51:52
 */
@Slf4j
@Service
public class RedissonLockService {
    private String a = "默认值";

    @Autowired
    RedissonClient redissonClient;

    /* *
    * 读用读锁 读锁为共享锁
    * 写用写锁 ，写锁为独占锁(排它锁)，当获取到写锁时，其他线程就不会获取到锁，会处于阻塞状态
    * 读写锁不能同时用在同一个业务中
    * */

    //读操作
    public String read() {
        //获取读写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("hello");
        //获取读锁
        RLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readLock.unlock();
        log.info("读操作的值：{}，线程：{}", a, Thread.currentThread().getName());
        return a;
    }

    //写操作
    public String write()  {
        //获取读写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("hello");
        //获取读锁
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        a= UUID.randomUUID().toString();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
        log.info("写操作的值：{}，线程：{}", a, Thread.currentThread().getName());
        return a;
    }

    public Boolean tc() throws InterruptedException {

        RSemaphore semaphore = redissonClient.getSemaphore("tcc");
        semaphore.acquire();
        return true;
    }

    public Boolean rc() {
        RSemaphore semaphore = redissonClient.getSemaphore("tcc");
        semaphore.release();
        return true;
    }


    public Boolean gogogo() {
        RCountDownLatch downLatch = redissonClient.getCountDownLatch("num");

        downLatch.countDown();
        System.out.println("溜了....");
        return true;
    }

    public Boolean suomen() throws InterruptedException {
        RCountDownLatch downLatch = redissonClient.getCountDownLatch("num");
        downLatch.trySetCount(10);
        downLatch.await();//等大家都走完...
        System.out.println("我要锁门....");
        return true;
    }
}



















