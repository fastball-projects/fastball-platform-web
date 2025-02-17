package dev.fastball.platform.web.filter;

import dev.fastball.platform.web.feature.business.context.BusinessContext;
import dev.fastball.platform.web.feature.business.context.BusinessContextItem;
import dev.fastball.platform.web.feature.business.context.WebPortalBusinessContextAccessor;
import dev.fastball.platform.web.service.WebPortalBusinessContextService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static dev.fastball.platform.web.WebPlatformConstants.BusinessContext.BUSINESS_CONTEXT_ID_HEADER;
import static dev.fastball.platform.web.WebPlatformConstants.BusinessContext.BUSINESS_CONTEXT_KEY_HEADER;

/**
 * @author Geng Rong
 */
@AllArgsConstructor
public class BusinessContextFilter extends OncePerRequestFilter {

    private final WebPortalBusinessContextService businessContextService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String businessKey = request.getHeader(BUSINESS_CONTEXT_KEY_HEADER);
            String businessId = request.getHeader(BUSINESS_CONTEXT_ID_HEADER);
            if (businessKey != null && businessId != null) {
                WebPortalBusinessContextAccessor<?> businessContextAccessor = businessContextService.getBusinessContext(businessKey);
                BusinessContextItem currentBusinessContext = businessContextAccessor.getBusinessContextById(businessId);
                if (currentBusinessContext != null) {
                    BusinessContext.set(currentBusinessContext);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            BusinessContext.remove();
        }
    }
}
