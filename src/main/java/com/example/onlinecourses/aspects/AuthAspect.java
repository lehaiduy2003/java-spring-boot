package com.example.onlinecourses.aspects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class AuthAspect {
    private static final Logger logger = Logger.getLogger(AuthAspect.class.getName());

    // log before sign in method
    @Before("@annotation(com.example.onlinecourses.annotations.LogEntryPoint)")
    public void logUserInfo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Executing user sign in method with information: %s", argsString));
        }
    }

    @Around("@annotation(com.example.onlinecourses.annotations.LogExecTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("%s executed in %dms", joinPoint.getSignature(), executionTime));
        }
        return proceed;
    }
}
