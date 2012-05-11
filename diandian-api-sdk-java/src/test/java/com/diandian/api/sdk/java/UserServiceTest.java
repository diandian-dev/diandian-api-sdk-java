/**
 * 
 */
package com.diandian.api.sdk.java;

import junit.framework.TestCase;

import com.diandian.api.sdk.java.service.BlogService;
import com.diandian.api.sdk.java.service.UserService;
import com.diandian.api.sdk.model.UserDetailInfo;
import com.diandian.api.sdk.view.DashboardView;
import com.diandian.api.sdk.view.FollowingView;
import com.diandian.api.sdk.view.LikesPostView;
import com.diandian.api.sdk.view.PostView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-11 下午2:07:41
 */
public class UserServiceTest extends TestCase {

    private final UserService userService;

    private final BlogService blogService;

    public UserServiceTest() {
        this.userService = new UserService(DDClientInvokerTest.getDDClientInvoker());
        this.blogService = new BlogService(DDClientInvokerTest.getDDClientInvoker());
    }

    public UserServiceTest(String testName) {
        super(testName);
        this.userService = new UserService(DDClientInvokerTest.getDDClientInvoker());
        this.blogService = new BlogService(DDClientInvokerTest.getDDClientInvoker());
    }

    public void testGetUserInfo() {
        UserDetailInfo userInfo = this.userService.getUserInfo();
        assertTrue(userInfo != null && userInfo.getBlogs().size() >= 1);
    }

    public void testGetHome() {
        DashboardView dbv = this.userService.getHome(null, 5, 0, null, false, false);
        assertTrue(dbv != null && dbv.getPosts().size() <= 5);

        String[] types = { "photo", "link", "text", "audio", "video" };

        for (String type : types) {
            dbv = this.userService.getHome(type, 5, 0, null, false, false);
            assertTrue(dbv != null);
        }
    }

    public void testMyLikes() {
        LikesPostView lpv = this.userService.getMyLikes(5, 0);
        assertTrue(lpv != null && lpv.getLikedPost().size() <= 5);
    }

    public void testMyFollowing() {
        FollowingView fv = this.userService.getMyFollowing(5, 0);
        assertTrue(fv != null && fv.getBlogs().size() <= 5);
    }

    public void testLike() {
        this.userService.like(getAnyOnePost());
    }

    public void testUnLike() {
        this.userService.unLike(getOnePostFromMyLikes());
    }

    public void testFollow() {
        this.userService.follow("zhangdong.diandian.com");
    }

    public void testUnFollow() {
        this.userService.unFollow("zhangdong.diandian.com");
    }

    private String getAnyOnePost() {
        //测试取任意post
        PostView postView = blogService.getPost("apitest", 5, 0, null, null, false, false, null);
        return postView.getPosts().get(0).getPostId();

    }

    private String getOnePostFromMyLikes() {
        LikesPostView lpv = this.userService.getMyLikes(5, 0);
        return lpv.getLikedPost().get(0).getPostId();
    }

}
