/**
 * 
 */
package com.diandian.api.sdk.java;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.diandian.api.sdk.DefaultHttpTools;
import com.diandian.api.sdk.Token;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-10 下午2:44:15
 */
public class HttpsTools extends DefaultHttpTools {

    /**
     * 默认构造函数，初始化上下文
     */
    public HttpsTools() {
        init();
    }

    /* (non-Javadoc)
     * @see com.diandian.api.sdk.DDHttpTools#openUrl(java.lang.String, java.lang.String, java.util.Map, com.diandian.api.sdk.Token)
     */
    @Override
    public String openUrl(String url, String method, Map<String, String> params, Token token) {
        return super.openUrl(url, method, params, token);
    }

    /* (non-Javadoc)
     * @see com.diandian.api.sdk.DDHttpTools#uploadFile(java.lang.String, java.util.Map, java.lang.String, java.lang.String, byte[], com.diandian.api.sdk.Token)
     */
    @Override
    public String uploadFile(String reqUrl, Map<String, String> parameters, String fileParamName,
            String filename, byte[] data, Token token) {
        return super.uploadFile(reqUrl, parameters, fileParamName, filename, data, token);
    }

    /**
     * 这是一个X509TrustManager的空的实现，仅仅做为一个示例。
     * 他会使验证证书失效。在生产环境中，务必自己实现一套。
     */
    private void init() {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        } };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
