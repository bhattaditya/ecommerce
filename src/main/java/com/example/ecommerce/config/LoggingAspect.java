package com.example.ecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.example.ecommerce.service..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        String className =
                joinPoint.getTarget().getClass().getSimpleName();

        String methodName =
                joinPoint.getSignature().getName();

        log.info("Entering {}.{}()",
                className,
                methodName);

        try {

            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - start;

            log.info(
                    "Exiting {}.{}() - took {} ms",
                    className,
                    methodName,
                    executionTime
            );

            return result;

        } catch (Exception ex) {

            log.error(
                    "Exception in {}.{}(): {}",
                    className,
                    methodName,
                    ex.getMessage()
            );

            throw ex;
        }
    }
}