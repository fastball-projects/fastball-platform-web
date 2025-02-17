package dev.fastball.platform.web.service.support;

import dev.fastball.platform.web.feature.business.context.WebPortalBusinessContextAccessor;
import dev.fastball.platform.web.service.WebPortalBusinessContextService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultWebPortalBusinessContextService implements WebPortalBusinessContextService, BeanPostProcessor {
    private final Map<String, WebPortalBusinessContextAccessor<?>> accessorKeyMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends WebPortalBusinessContextAccessor>, WebPortalBusinessContextAccessor<?>> accessorClassMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebPortalBusinessContextAccessor) {
            WebPortalBusinessContextAccessor<?> businessContextAccessor = (WebPortalBusinessContextAccessor<?>) bean;
            accessorKeyMap.put(businessContextAccessor.contextKey(), businessContextAccessor);
            accessorClassMap.put(businessContextAccessor.getClass(), businessContextAccessor);
        }
        return bean;
    }

    @Override
    public WebPortalBusinessContextAccessor<?> getBusinessContext(String contextKey) {
        return accessorKeyMap.get(contextKey);
    }

    @Override
    public WebPortalBusinessContextAccessor<?> getBusinessContext(Class<? extends WebPortalBusinessContextAccessor<?>> contextClass) {
        return accessorClassMap.get(contextClass);
    }
}
