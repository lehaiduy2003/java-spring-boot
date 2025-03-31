package com.example.onlinecourses.aspects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect extends BaseAspect {

    public LoggingAspect() {
        super(Logger.getLogger(LoggingAspect.class.getName()));
    }

    // log before execute method
    @Before("@annotation(com.example.onlinecourses.annotations.LogEntryPoint)")
    public void logEntryPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        String message = String.format("Method %s called with arguments: %s", joinPoint.getSignature(), argsString);
        super.logMsg(message, Level.INFO);
    }

    // log result of method
    @After("@annotation(com.example.onlinecourses.annotations.LogExecResult)")
    public void logExecResult(JoinPoint joinPoint) {
        super.logMsg(String.format("Method %s executed", joinPoint.getSignature()), Level.INFO);
    }

    // calculate execution time of method and log result or error
    @Around("@annotation(com.example.onlinecourses.annotations.LogExecTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            super.logMsg(String.format("Method %s executed in %d ms", joinPoint.getSignature(), executionTime), Level.INFO);
            super.logMsg(String.format("Method %s returned: %s", joinPoint.getSignature(), result), Level.INFO);
        } catch (Throwable throwable) {
            super.logEx(joinPoint, throwable);
            throw throwable;
        }
        return result;
    }

}
