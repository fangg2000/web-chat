package com.fangg.config.sharingshpere;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class DataSourceAspect {
	private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("@annotation(com.fangg.config.sharingshpere.DataSourceAnnotation)")
    public void dsPointCut() {
    	logger.info("进入拦截--数据源切换方法");
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSourceAnnotation dataSource = method.getAnnotation(DataSourceAnnotation.class);
        
        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }
        
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            //DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}
