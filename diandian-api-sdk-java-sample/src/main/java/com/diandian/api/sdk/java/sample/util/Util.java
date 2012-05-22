/**
 * 
 */
package com.diandian.api.sdk.java.sample.util;

import org.apache.commons.lang3.StringUtils;

import com.diandian.api.sdk.Token;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-16 下午6:40:14
 */
public class Util {

    /**
     * 将token转为String.用于保存用户登陆信息
     * 
     * <h1>正常情况下肯定不能这样做哦。需要自己实现一个</h1>
     * 
     * @param token
     * @return
     */
    public static String token2String(Token token) {
        if (token == null) {
            return null;
        }
        String info = token.getAccessToken();
        info += "&" + token.getRefreshToken();
        info += "&" + token.getExpiresIn();
        info += "&" + token.getLastUpdateTime();
        info += "&" + token.getTokenType();
        info += "&" + StringUtils.join(token.getScope(), ",");
        return info;
    }

    public static Token string2Token(String info) {
        if (StringUtils.isEmpty(info)) {
            return null;
        }
        String[] tokenArr = info.split("&");
        Token token = new Token();
        token.setAccessToken(tokenArr[0]);
        token.setRefreshToken(tokenArr[1]);
        token.setExpiresIn(Long.valueOf(tokenArr[2]));
        token.setLastUpdateTime(Long.valueOf(tokenArr[3]));
        token.setTokenType(tokenArr[4]);
        token.setScope(tokenArr[5].split(","));
        return token;

    }
}
