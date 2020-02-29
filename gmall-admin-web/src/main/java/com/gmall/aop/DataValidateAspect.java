package com.gmall.aop;

import com.gmall.to.CommonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-2-29 18:04:20
 */
@Component
@Aspect
public class DataValidateAspect {

    /* *
    * ..表示当前包下面的任意层子包，第一个*为返回值，*(..)任意参数的方法，*Controller表示以Controller结尾的任意类
    *
    * */

    @Around("execution(* com.gmall..*Controller.*(..))")
    public Object dataValidate(ProceedingJoinPoint point) {
        //前置通知
        Object proceed = null;
        try {
            //对参数进行校验，如果通过那么才执行目标方法
            Object[] args = point.getArgs();
            for (Object arg : args) {
                if(arg instanceof BindingResult){
                    int errorCount = ((BindingResult) arg).getErrorCount();
                    if(errorCount>0){
                        //如果校验出错，那么就返回出错的信息
                        return new CommonResult().validateFailed((BindingResult)arg);
                    }
                }
            }
            //目标方法
            proceed = point.proceed(args);

        } catch (Throwable throwable) {
            //异常通知
            throwable.printStackTrace();
        } finally {
            //后置通知
        }
        return proceed;
    }
}
