/**
 * 
 */
package com.diandian.api.sdk.java.sample.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.diandian.api.sdk.DefaultJsonParser;
import com.diandian.api.sdk.Token;
import com.diandian.api.sdk.java.DDClient;
import com.diandian.api.sdk.java.DDClientInvoker;
import com.diandian.api.sdk.java.HttpsTools;
import com.diandian.api.sdk.java.sample.util.RequestThreadUtils;
import com.diandian.api.sdk.java.sample.util.Util;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-14 下午4:01:20
 */
public class UserFilter implements Filter {

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (this.needLogin((HttpServletRequest) request)) {
            if (RequestThreadUtils.getDDClientInvoker() == null) {

                DDClient ddClient = new DDClient("appKey",
                        "appSecret", "http://127.0.0.1");
                ddClient.setDdHttpTools(new HttpsTools());
                Token token = Util.string2Token(getCookie("access_token",
                        (HttpServletRequest) request));
                if (token == null) {
                    ((HttpServletResponse) response).sendRedirect("/oauthcode");
                    return;
                }
                ddClient.setToken(token);
                //如果token已过期，刚refresh
                if (!ddClient.isSessionValid()) {
                    ddClient.refreshToken();
                    RequestThreadUtils.saveToken(ddClient.getToken(),
                            (HttpServletResponse) response);
                }
                RequestThreadUtils.setDDClientInvoker(DDClientInvoker.init(ddClient,
                        new DefaultJsonParser()));
            }

        }
        chain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public String getCookie(String key, HttpServletRequest request) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        HttpServletRequest hsRequest = request;
        Cookie[] cookies = hsRequest.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public boolean needLogin(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (StringUtils.isEmpty(url) || url.contains("oauthcode") || url.equals("/")) {
            return false;
        }
        return true;
    }
}
