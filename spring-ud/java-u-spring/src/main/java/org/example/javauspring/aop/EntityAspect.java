package org.example.javauspring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EntityAspect {

    private static final Logger log = LoggerFactory.getLogger(EntityAspect.class);

    @Pointcut("execution(public Integer org.example.javauspring.service.*Service.findById(Integer))")   // pattern
//    @Pointcut("execution(public * findById)")
    public void anyFindByIdServiceMethod() {

    }

    @Before("anyFindByIdServiceMethod() && args(id)")
//    @Before("anyFindByIdServiceMethod()")
    public void addLogging(JoinPoint joinPoint, Object id) {
        log.info("invoked findById method");
    }

    @Around("anyFindByIdServiceMethod() && args(id)")
    public Object addLoggingAround(ProceedingJoinPoint joinPoint, Object id) throws Throwable {
        log.info("invoked findById method around id {}",id);
        Object result = joinPoint.proceed(); // если обработать, то не увидим exception
        log.info("invoked findById method around result{}",result);
        return result;
    }

    // bean - check bean name
    @Pointcut("bean(*Service)")
    public void isService() {

    }

    // args - check method type
    // *  - any param type
    // @args - check annotation on the param type
    @Pointcut("args(org.springframework.ui.Model, ..)") // от 0 до ..по параметрам
    public void hasModelParam() {

    }

    // @within - check annotation on the class level (по аннотации над классом)
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isEntityPointcut() {

    }

    // @annotation - check annotation on method level
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)") // (по аннотации метода)
    public void hasGetMapping() {

    }

    // within - check class type name
    @Pointcut("within(org.example.javauspring.service.*Service)") // постфикс
    public void isEntityServicePointcut() {

    }

    // this - check AOP proxy class type
    // target - check target object class type
    @Pointcut("this(org.springframework.data.repository.Repository)")
//    @Pointcut("target(org.springframework.data.repository.Repository)")
    public void isEntityRepositoryPointcut() {

    }
}
