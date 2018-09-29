package app.rest.authorisation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class UserAccessAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* app.rest.LogbookHttpListener.*(..))")
    public void beforeHttpMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info(String.format("User access lookup goes here... '%s'.", methodName));
    }

}
