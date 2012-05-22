/**
 * 
 */
package com.diandian.api.sdk.java.sample.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.diandian.api.sdk.Token;
import com.diandian.api.sdk.java.DDClientInvoker;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-17 下午5:17:28
 */
public class RequestThreadUtils {

    public static final String INVOKER = "invoker";

    public static void setDDClientInvoker(DDClientInvoker ddClientInvoker) {
        setAttribute(INVOKER, ddClientInvoker);
    }

    public static DDClientInvoker getDDClientInvoker() {
        return (DDClientInvoker) getAttribute(INVOKER);
    }

    /**
     * save attribute to request scope;
     * 
     * @param attr
     * @param value
     */
    public static void setAttribute(String attr, Object value) {
        RequestAttributes requestAttrs = RequestContextHolder.currentRequestAttributes();
        requestAttrs.setAttribute(attr, value, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * get attribute from request scope;
     * 
     * @param attr
     * @return
     */
    public static Object getAttribute(String attr) {
        RequestAttributes requestAttrs = RequestContextHolder.currentRequestAttributes();
        return requestAttrs.getAttribute(attr, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * remove attribute from request scope;
     * 
     * @param attr
     */
    public static void removeAttribute(String attr) {
        RequestAttributes requestAttrs = RequestContextHolder.currentRequestAttributes();
        requestAttrs.removeAttribute(attr, RequestAttributes.SCOPE_REQUEST);
    }

    public static void saveToken(Token token, HttpServletResponse response) {
        String tokenStr = Util.token2String(token);
        Cookie cookie = new Cookie("access_token", tokenStr);
        response.addCookie(cookie);
    }

}
