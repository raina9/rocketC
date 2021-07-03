package com.weblearnex.app.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.weblearnex.app.exception.CustomException;
import com.weblearnex.app.security.JwtTokenProvider;
import com.weblearnex.app.setup.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UrlPathHelper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;

public class PreFilter extends ZuulFilter {

    //private static Logger log = LoggerFactory.getLogger(PreFilter.class);
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    Gson gson = new Gson();
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String remoteHost = ctx.getRequest().getRemoteHost();
        String requestURL = ctx.getRequest().getRequestURL().toString();
        if (!requestURL.contains("proxyurl")) {
            // log.info("remoteHost {} requestURL {}", new Object[]{remoteHost, requestURL});
            String originatingRequestUri = this.urlPathHelper.getOriginatingRequestUri(ctx.getRequest());
            final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
            //log.info("URI {} original URI {}", new Object[]{requestURI, originatingRequestUri});
            String protocol = requestURL.substring(0, requestURL.indexOf("//") + 2);
            String urlWithoutProtocol = requestURL.substring(requestURL.indexOf("//") + 2);
            String[] split = urlWithoutProtocol.substring(0, urlWithoutProtocol.indexOf("/")).split("\\.");
            String subPath = split[0];
            //final String newURL = protocol + "." + split[1] + "." + split[2];
            //Here the main thing is to create a HttpServletRequestWrapper and override the request coming from the actual request
            HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(ctx.getRequest()) {
                public String getRequestURI() {
                    if (requestURI != null && !requestURI.equals("/")) {
                        if (subPath.length() <= 0) {
                            return "/" + subPath + requestURI;
                        } else {
                            return requestURI;
                        }
                    }
                    if (subPath.length() <= 0) {
                        return "/" + subPath;
                    } else {
                        return "/";
                    }
                }
            };

            //String token = jwtTokenProvider.resolveToken(request);
            //UserDetails userDetails = jwtTokenProvider.getUserDetails(token);


           /* HttpSession session = request.getSession();
            User user = (User) session.getAttribute(userDetails.getUsername());
            try {
                String userString = gson.toJson(user);
                ctx.addZuulRequestHeader("user", userString);

            } catch (Exception e) {
                new CustomException("Error ", HttpStatus.INTERNAL_SERVER_ERROR);
            }*/
            ctx.setRequest(httpServletRequestWrapper);
        }
        System.out.println(
                "Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
        return null;
    }


}
