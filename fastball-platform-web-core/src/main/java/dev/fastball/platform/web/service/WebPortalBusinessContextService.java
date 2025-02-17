package dev.fastball.platform.web.service;

import dev.fastball.platform.web.feature.business.context.WebPortalBusinessContextAccessor;

public interface WebPortalBusinessContextService {

    WebPortalBusinessContextAccessor<?> getBusinessContext(String contextKey);

    WebPortalBusinessContextAccessor<?> getBusinessContext(Class<? extends WebPortalBusinessContextAccessor<?>> contextClass);
}
