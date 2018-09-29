package app.logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class MethodLoggerAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Before("execution(* app..*(..))")
    public void before(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.info(String.format("Method '%s.%s' started.", className, methodName));
    }


    @After("execution(* app..*(..))")
    public void after(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.info(String.format("Method '%s.%s' finished.", className, methodName));
    }



}
