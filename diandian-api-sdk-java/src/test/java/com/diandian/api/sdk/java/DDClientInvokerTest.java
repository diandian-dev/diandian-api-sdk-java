/**
 * 
 */
package com.diandian.api.sdk.java;

import junit.framework.TestCase;

import com.diandian.api.sdk.DefaultJsonParser;
import com.diandian.api.sdk.view.TagView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-10 下午5:05:12
 */
public class DDClientInvokerTest extends TestCase {

    public DDClientInvokerTest() {
    }

    public DDClientInvokerTest(String testName) {
        super(testName);
    }

    public void testGet() {
        TagView tagView = new DDClientInvoker(getClient(), new DefaultJsonParser()).getMyTags();
        System.out.println(tagView.toString());
    }

    public void testPost() {
        new DDClientInvoker(getClient(), new DefaultJsonParser()).watchTag("点点");
    }

    public static DDClient getClient() {
        DDClient dd = new DDClient(DDAPIConstants.APP_KEY, DDAPIConstants.APP_SECRET,
                DDAPIConstants.REDIRECT_URI);
        dd.setDdHttpTools(new HttpsTools());
        dd.initAccessTokenByPassword(DDAPIConstants.USER_NAME, DDAPIConstants.USER_PASSWORD);
        return dd;
    }

    public static void main(String[] args) {
        new DDClientInvokerTest().testPost();
        new DDClientInvokerTest().testGet();
    }

    public static DDClientInvoker getDDClientInvoker() {
        return new DDClientInvoker(getClient(), new DefaultJsonParser());
    }
}
