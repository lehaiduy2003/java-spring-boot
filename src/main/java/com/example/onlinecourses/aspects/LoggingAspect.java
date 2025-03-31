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
public class LoggingAspect {
    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    // log before execute method
    @Before("@annotation(com.example.onlinecourses.annotations.LogEntryPoint)")
    public void logEntryPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Executing method: %s", joinPoint.getSignature()));
        }
        if(args.length > 0 && logger.isLoggable(Level.FINE)) {
            logger.info(String.format("Method arguments: %s", argsString));
        }
    }

    // log result of method
    @After("@annotation(com.example.onlinecourses.annotations.LogExecResult)")
    public void logExecResult(JoinPoint joinPoint) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Method executed: %s", joinPoint.getSignature()));
        }
    }

    // calculate execution time of method and log result or error
    @Around("@annotation(com.example.onlinecourses.annotations.LogExecTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("%s executing succeed in %dms", joinPoint.getSignature(), executionTime));
            }
            if(logger.isLoggable(Level.FINE)) {
                logger.fine(String.format("Method result: %s", result));
            }
        } catch (Throwable throwable) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("Method %s threw an exception: %s", joinPoint.getSignature(), throwable.getMessage()));
            }
            throw throwable;
        }
        return result;
    }

}
