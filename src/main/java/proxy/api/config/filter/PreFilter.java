package proxy.api.config.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.session.SessionRepository;
import proxy.api.config.interceptor.RestTemplateLoggingInterceptor;

public class PreFilter extends ZuulFilter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PreFilter.class);

    @Autowired
    private SessionRepository repository;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_FORWARD_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        log.info(request.getAuthType() + " :: " + request.getSession().getId() + " :: " + request.getCookies().toString());
        return null;
    }
}