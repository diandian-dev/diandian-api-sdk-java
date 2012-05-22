/**
 * 
 */
package com.diandian.api.sdk.java;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.diandian.api.sdk.DDHttpTools;
import com.diandian.api.sdk.Token;
import com.diandian.api.sdk.exception.DDAPIException;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-7 下午4:46:10
 */
public class DDClient {

    private Token token = null;

    private String appKey;

    private String appSecret;

    private String userName = null;

    private String userPassword = null;

    private final String URL_AUTHORIZE = DDAPIConstants.HOST + "oauth/authorize";

    private final String URL_OAUTH_TOKEN = DDAPIConstants.HOST + "oauth/token";

    private String REDIRECT_URI;

    /**
     * 默认权限
     */
    private final String[] DEFAULT_PERMISSIONS = { "read", "write" };

    /**
     * HttpTools主要负责Http相关的操作。
     */
    private DDHttpTools ddHttpTools;

    /**
     * 
     * @param appKey
     * @param appSecret
     * @param redirectUri
     */
    public DDClient(String appKey, String appSecret, String redirectUri) {
        if (StringUtils.isEmpty(appKey)) {
            throw new DDAPIException(DDAPIConstants.INVALID_CLIENT_KEY, "invalid appKey", null);
        }
        if (StringUtils.isEmpty(appSecret)) {
            throw new DDAPIException(DDAPIConstants.INVALID_CLIENT_SECRET, "invalid appSecret",
                    null);
        }
        if (StringUtils.isEmpty(redirectUri)) {
            throw new DDAPIException(DDAPIConstants.INVALID_CLIENT_REDIRECT_URI,
                    "invalid redirectUri", null);
        }
        this.setAppKey(appKey);
        this.setAppSecret(appSecret);
        this.setRedirectUri(redirectUri);
    }

    /**
     * 获取code认证方式的url。
     * 
     * @return
     */
    public String getOauthUrl() {
        return URL_AUTHORIZE + "?client_id=" + appKey + "&response_type=code&scope="
                + StringUtils.join(DEFAULT_PERMISSIONS, ",");
    }

    /**
     * 默认的code认证方式。
     * 
     * @param code
     * @param listener
     */
    public void initAccessTokenByCode(String code) {
        DDParameters param = new DDParameters();
        param.add("client_id", this.getAppKey());
        param.add("client_secret", this.getAppSecret());
        param.add("grant_type", "authorization_code");
        param.add("code", code);
        param.add("redirect_uri", REDIRECT_URI);
        this.initAccessToken(param);
    }

    /**
     * 默认的password认证方式。
     * 
     * @param userName
     * @param userPassword
     * @param listener
     */

    public void initAccessTokenByPassword(String userName, String userPassword) {
        DDParameters param = new DDParameters();
        param.add("client_id", this.getAppKey());
        param.add("client_secret", this.getAppSecret());
        param.add("grant_type", "password");
        param.add("username", userName);
        param.add("password", userPassword);
        String scope = StringUtils.join(DEFAULT_PERMISSIONS, ",");
        param.add("scope", scope);
        this.initAccessToken(param);
    }

    /**
     * refreshToken
     * token过期后使用
     * 
     * @param listener
     */
    public void refreshToken() {
        if (token == null) {
            throw new DDAPIException(401, "invalid token", null);
        }
        DDParameters param = new DDParameters();
        param.add("client_id", this.getAppKey());
        param.add("client_secret", this.getAppSecret());
        param.add("grant_type", "refresh_token");
        param.add("refresh_token", token.getRefreshToken());
        param.add("scope", StringUtils.join(DEFAULT_PERMISSIONS, ","));
        this.initAccessToken(param);

    }

    private synchronized void initAccessToken(DDParameters param) {
        try {
            String url = URL_OAUTH_TOKEN + "?" + DDHttpTools.encodeUrl(param.mParameters);
            String result = this.doGet(url, null, null);
            JSONObject json = new JSONObject(result);
            if (token == null) {
                token = new Token();
            }
            token.setAccessToken(json.getString("access_token"));
            token.setRefreshToken(json.getString("refresh_token"));
            token.setExpiresIn(json.getLong("expires_in"));
            token.setScope(json.getString("scope").split(","));
            token.setTokenType(json.getString("token_type"));
            token.setLastUpdateTime(System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DDAPIException e) {
            throw e;
        }
    }

    /**
     * 判断当前session是否可用。
     * 过期和null皆为不可用
     * 
     * @return
     */
    public boolean isSessionValid() {
        if (token != null) {
            return (!StringUtils.isEmpty(token.getAccessToken()) && (System.currentTimeMillis() < token
                    .getExpiresIn() * 1000 + token.getLastUpdateTime()));
        }
        return false;
    }

    public String doGet(String url, Token token, DDParameters ddParameters) {
        return ddHttpTools.openUrl(url, "GET", ddParameters == null ? null
                : ddParameters.mParameters, token);
    }

    public String doPost(String url, Token token, DDParameters ddParameters) {
        return ddHttpTools.openUrl(url, "POST", ddParameters == null ? null
                : ddParameters.mParameters, token);
    }

    public String doUpload(String reqUrl, DDParameters parameters, String fileParamName,
            String filename, byte[] data, Token token) {
        try {
            return ddHttpTools.uploadFile(reqUrl, parameters.mParameters, fileParamName, filename,
                    data, token);

        } catch (Exception e) {
            throw new DDAPIException(DDAPIConstants.UPLOAD_FAILED, e.getMessage(), e);
        }
    }

    /**
     * @return the ddhttpTools
     */
    public DDHttpTools getDdHttpTools() {
        return ddHttpTools;
    }

    /**
     * @param ddhttpTools the ddhttpTools to set
     */
    public void setDdHttpTools(DDHttpTools ddHttpTools) {
        this.ddHttpTools = ddHttpTools;
    }

    /**
     * 获取有效的token.
     * 如果token无效，则
     * throw new DDAPIException(DDAPIConstants.INVALID_TOKEN,
     * "invalid token", null);
     * 
     * @return
     */
    public Token getToken() {
        if (this.isSessionValid()) {
            return token;
        } else {
            throw new DDAPIException(DDAPIConstants.INVALID_TOKEN, "invalid token", null);
        }
    }

    /**
     * 获取token.
     * 结果可能为空，可能过期，因为不能直接使用
     * 
     * @return
     */
    public Token getTokenWithOutCheck() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRedirectUri() {
        return this.REDIRECT_URI;
    }

    public void setRedirectUri(String redirectUri) {
        this.REDIRECT_URI = redirectUri;
    }

}
