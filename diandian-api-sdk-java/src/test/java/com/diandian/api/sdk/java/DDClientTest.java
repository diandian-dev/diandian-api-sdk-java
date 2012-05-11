/**
 * 
 */
package com.diandian.api.sdk.java;

import junit.framework.TestCase;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-9 下午5:56:11
 */
public class DDClientTest extends TestCase {

    public DDClientTest() {

    }

    public DDClientTest(String testName) {
        super(testName);
    }

    public void testDDClient() {

        DDClient dd = new DDClient(DDAPIConstants.APP_KEY, DDAPIConstants.APP_SECRET,
                DDAPIConstants.REDIRECT_URI);
        dd.setDdHttpTools(new HttpsTools());
        dd.initAccessTokenByPassword(DDAPIConstants.USER_NAME, DDAPIConstants.USER_PASSWORD);
        System.out.println(dd.getToken());

    }

    public static void main(String[] args) {
        new DDClientTest().testDDClient();
    }

}
