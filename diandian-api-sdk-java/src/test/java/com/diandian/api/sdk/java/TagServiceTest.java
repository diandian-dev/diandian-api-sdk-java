/**
 * 
 */
package com.diandian.api.sdk.java;

import junit.framework.TestCase;

import com.diandian.api.sdk.java.service.TagService;
import com.diandian.api.sdk.view.DashboardView;
import com.diandian.api.sdk.view.TagView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-11 下午2:30:18
 */
public class TagServiceTest extends TestCase {

    private final TagService tagService;

    private final String tag = "点点";

    public TagServiceTest() {
        this.tagService = new TagService(DDClientInvokerTest.getDDClientInvoker());
    }

    public TagServiceTest(String testName) {
        super(testName);
        this.tagService = new TagService(DDClientInvokerTest.getDDClientInvoker());
    }

    public void testTagPost() {
        DashboardView dbv = this.tagService.getTagPost(tag);
        assertTrue(dbv != null && dbv.getPosts().get(0).getTags().contains(tag));
    }

    public void testWatchTag() {
        this.tagService.watchTag(tag);

    }

    public void testUnwatchTag() {
        this.tagService.unwatchTag(tag);
    }

    public void testMyTags() {
        TagView tagView = this.tagService.getMyTags();
        assertTrue(tagView != null && tagView.getTags().size() >= 0);
    }
}
