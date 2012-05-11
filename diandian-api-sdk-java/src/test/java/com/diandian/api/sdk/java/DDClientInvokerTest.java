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

        DDClientInvoker.init(getClient(), new DefaultJsonParser());
        TagView tagView = DDClientInvoker.getInstance().getMyTags();

        System.out.println(tagView.toString());

    }

    public void testPost() {
        DDClientInvoker.init(getClient(), new DefaultJsonParser());
        DDClientInvoker.getInstance().watchTag("点点");

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
        return DDClientInvoker.init(getClient(), new DefaultJsonParser());
    }
}
