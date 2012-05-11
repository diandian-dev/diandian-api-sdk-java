/**
 * 
 */
package com.diandian.api.sdk.java;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;

import com.diandian.api.sdk.java.service.BlogService;
import com.diandian.api.sdk.model.AudioPostInfo;
import com.diandian.api.sdk.model.BlogDetailInfo;
import com.diandian.api.sdk.model.LinkPostInfo;
import com.diandian.api.sdk.model.PhotoPostInfo;
import com.diandian.api.sdk.model.PostBaseInfo;
import com.diandian.api.sdk.model.TextPostInfo;
import com.diandian.api.sdk.model.VideoPostInfo;
import com.diandian.api.sdk.view.FollowersView;
import com.diandian.api.sdk.view.PostView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-11 上午10:13:05
 */
public class BlogServiceTest extends TestCase {

    private static final String blogCName = "apitest";

    private final BlogService blogService;

    public BlogServiceTest() {
        this.blogService = new BlogService(DDClientInvokerTest.getDDClientInvoker());

    }

    public BlogServiceTest(String testName) {
        super(testName);
        this.blogService = new BlogService(DDClientInvokerTest.getDDClientInvoker());
    }

    public void testGetBlogInfo() {
        BlogDetailInfo blogInfo = blogService.getBlogInfo(blogCName);
        assertTrue(blogInfo != null);

    }

    public void testGetFollowers() {
        FollowersView followers = blogService.getFollowers(blogCName, 5, 0);
        assertTrue(followers != null && followers.getUsers().size() <= 5);
    }

    public void testGetPosts() {
        //测试取做生意post
        PostView postView = blogService.getPost(blogCName, 5, 0, null, null, false, false, null);
        assertTrue(postView != null && postView.getPosts().size() <= 5);

        String tag = "ddtest";
        postView = blogService.getPost(blogCName, 5, 0, tag, null, false, false, null);
        assertTrue(postView != null && postView.getPosts().size() <= 5
                && postView.getPosts().get(0).getTags().contains(tag));

        //测试取各类型的post
        String[] types = { "photo", "link", "text", "audio", "video" };
        for (String type : types) {
            postView = blogService.getPost(blogCName, 5, 0, null, type, false, false, null);
            assertTrue(postView != null
                    && postView.getPosts().get(0).getType().equalsIgnoreCase(type));

            postView = blogService.getPost(blogCName, 5, 0, tag, type, false, false, null);
            assertTrue(postView != null
                    && postView.getPosts().get(0).getType().equalsIgnoreCase(type)
                    && postView.getPosts().get(0).getTags().contains(tag));

        }

        //测试取一条post
        postView = blogService.getPost(blogCName, 0, 0, null, null, false, false, getAnyOnePost());
        assertTrue(postView != null && postView.getPosts().size() == 1);

    }

    public void testPost() throws InterruptedException {
        String[] states = { "published", "draft", "submission" };
        String tag = "ddtest";
        //测试发布音乐
        for (String state : states) {
            blogService
                    .postAudio(
                            blogCName,
                            state,
                            tag,
                            null,
                            "apitest",
                            "zd",
                            "dz",
                            "/Users/zhangdong/workspace/diandian-api-sdk/diandian-api-sdk-java/src/test/java/com/diandian/api/sdk/java/data.mp3");
            //因为系统有防机器程序，所以休息一个再搞
            Thread.sleep(2000);
        }

        //测试发布视频
        for (String state : states) {
            blogService.postVideo(blogCName, state, tag, null, "apitest",
                    "http://v.youku.com/v_show/id_XMzk0MTU2NjEy.html");
            //因为系统有防机器程序，所以休息一个再搞
            Thread.sleep(2000);
        }

        //测试发布图片
        for (String state : states) {
            blogService
                    .postPhoto(
                            blogCName,
                            state,
                            tag,
                            null,
                            "apitest",
                            "/Users/zhangdong/workspace/diandian-api-sdk/diandian-api-sdk-java/src/test/java/com/diandian/api/sdk/java/data.jpg");
            //因为系统有防机器程序，所以休息一个再搞
            Thread.sleep(2000);
        }

        //测试发布文字
        for (String state : states) {
            blogService.postText(blogCName, state, tag, null, "apitest",
                    "<p>good good study,day day above</p>");
            //因为系统有防机器程序，所以休息一个再搞
            Thread.sleep(2000);
        }

        //测试发布链接
        for (String state : states) {
            blogService.postLink(blogCName, state, tag, null, "apitest", "www.diandian.com",
                    "点点网，一个你来了就不想离开的网站");
            //因为系统有防机器程序，所以休息一个再搞
            Thread.sleep(2000);
        }

    }

    public void testEdit() {
        PostView postView = blogService.getPost(blogCName, 0, 0, null, null, false, false,
                getAnyOnePost());
        if (postView != null) {
            postView.getPosts().get(0).getTags().add("ddddtest");
            PostBaseInfo p = postView.getPosts().get(0);
            if (p instanceof PhotoPostInfo) {
                editPhoto((PhotoPostInfo) p);
            } else if (p instanceof VideoPostInfo) {
                editVideo((VideoPostInfo) p);
            } else if (p instanceof AudioPostInfo) {
                editAudio((AudioPostInfo) p);
            } else if (p instanceof TextPostInfo) {
                editText((TextPostInfo) p);
            } else if (p instanceof LinkPostInfo) {
                editLink((LinkPostInfo) p);
            }
        } else {
            assertTrue(false);
        }

    }

    private void editPhoto(PhotoPostInfo photoPost) {

        blogService
                .editPost(
                        blogCName,
                        photoPost.getPostId(),
                        photoPost.getType(),
                        "published",
                        StringUtils.join(photoPost.getTags(), ","),
                        null,
                        null,
                        null,
                        null,
                        null,
                        photoPost.getCaption(),
                        null,
                        null,
                        "/Users/zhangdong/workspace/diandian-api-sdk/diandian-api-sdk-java/src/test/java/com/diandian/api/sdk/java/data.jpg",
                        null);
    }

    private void editAudio(AudioPostInfo audioPost) {

        blogService
                .editPost(
                        blogCName,
                        audioPost.getPostId(),
                        audioPost.getType(),
                        "published",
                        StringUtils.join(audioPost.getTags(), ","),
                        null,
                        null,
                        null,
                        null,
                        null,
                        audioPost.getCaption(),
                        null,
                        null,
                        "/Users/zhangdong/workspace/diandian-api-sdk/diandian-api-sdk-java/src/test/java/com/diandian/api/sdk/java/data.mp3",
                        null);

    }

    private void editVideo(VideoPostInfo videoPost) {

        blogService.editPost(blogCName, videoPost.getPostId(), videoPost.getType(), "published",
                StringUtils.join(videoPost.getTags(), ","), null, null, null, null, null,
                videoPost.getCaption(), null, null, null,
                "http://v.youku.com/v_show/id_XMzk0MTU2NjEy.html");

    }

    private void editText(TextPostInfo testPost) {
        blogService.editPost(blogCName, testPost.getPostId(), testPost.getType(), "published",
                StringUtils.join(testPost.getTags(), ","), null, testPost.getBody(),
                testPost.getTitle(), null, null, null, null, null, null, null);

    }

    private void editLink(LinkPostInfo linkPost) {
        blogService.editPost(blogCName, linkPost.getPostId(), linkPost.getType(), "published",
                StringUtils.join(linkPost.getTags(), ","), null, linkPost.getTitle(), null,
                linkPost.getDescription(), linkPost.getUrl(), null, null, null, null, null);

    }

    private String getAnyOnePost() {
        //测试取做生意post
        PostView postView = blogService.getPost(blogCName, 5, 0, null, null, false, false, null);
        return postView.getPosts().get(0).getPostId();

    }

    public void testReblog() {
        blogService.reblog(blogCName, getAnyOnePost(), "ddtest", "good good study,day day above");
        assertTrue(true);
    }

    public void testDelete() {
        blogService.delete(blogCName, getAnyOnePost());
    }

    public void testDraft() {

        PostView dbv = blogService.getDraft(blogCName);
        assertTrue(dbv != null);

    }

    public void testQueue() {

        PostView dbv = blogService.getQueue(blogCName);
        assertTrue(dbv != null);
    }

    public void testSubmission() {
        PostView dbv = blogService.getSubmission(blogCName);
        assertTrue(dbv != null);
    }

    public static void main(String args[]) {

    }

}
