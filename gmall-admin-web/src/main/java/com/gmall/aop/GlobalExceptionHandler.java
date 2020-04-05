package com.gmall.aop;

import com.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Aiden
 * @version 1.0
 * @description  springMvc的统一异常处理，只要有异常发生就会来到这个处理类，不同的方法就会处理不同的异常  @ControllerAdvice增强的控制器
 * @date 2020-3-1 14:25:14
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    //专门处理数学异常
    @ExceptionHandler(value = {ArithmeticException.class})
    public Object handlerException(Exception exception){
        //打印异常信息
        log.error("感知系统抛出的异常，信息：{}", (Object) exception.getStackTrace());
        return new CommonResult().validateFailed("数学异常");
    }

    //专门处理空指针异常
    @ExceptionHandler(value = {NullPointerException.class})
    public Object handlerException2(Exception exception){
        //打印异常信息
        log.error("感知系统抛出的异常，信息：{}", (Object) exception.getStackTrace());
        return new CommonResult().validateFailed("空指针异常");
    }
}
