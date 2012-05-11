/**
 * 
 */
package com.diandian.api.sdk.java.service;

import com.diandian.api.sdk.java.DDClientInvoker;
import com.diandian.api.sdk.model.UserDetailInfo;
import com.diandian.api.sdk.view.DashboardView;
import com.diandian.api.sdk.view.FollowingView;
import com.diandian.api.sdk.view.LikesPostView;

/**
 * 用户相关接口的集合，可以用它获取用户信息，
 * 我的喜欢，我的关注，关注，喜欢等
 * 
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-8 下午5:19:30
 */
public class UserService {

    private final DDClientInvoker ddClientInvoker;

    public UserService(DDClientInvoker ddClientInvoker) {
        this.ddClientInvoker = ddClientInvoker;

    }

    /**
     * @return the ddClientInvoker
     */
    public DDClientInvoker getDdClientInvoker() {
        return ddClientInvoker;
    }

    /**
     * 获取首页动态
     * 
     * @param type 类型。如photo,text,link,audio,video，为空表示全部类型
     * @param limit
     * @param offset
     * @param sinceId
     * @param reblogInfo
     * @param notesInfo
     * @return
     */
    public DashboardView getHome(String type, int limit, int offset, String sinceId,
            boolean reblogInfo, boolean notesInfo) {
        return this.ddClientInvoker.getHome(type, limit, offset, sinceId, reblogInfo, notesInfo);
    }

    /**
     * 获取用户信息
     * 
     * @return 该用户的全部信息，如关注数，博客等
     */
    public UserDetailInfo getUserInfo() {
        return this.ddClientInvoker.getUserInfo();
    }

    /**
     * 获取我喜欢的post
     * 
     * @param limit
     * @param offset
     * @return
     */
    public LikesPostView getMyLikes(int limit, int offset) {
        return this.ddClientInvoker.getLikes(limit, offset);

    }

    /**
     * 获取的关注的博客
     * 
     * @param limit
     * @param offset
     * @return
     */
    public FollowingView getMyFollowing(int limit, int offset) {
        return this.ddClientInvoker.getFollowing(limit, offset);
    }

    public void like(String id) {
        this.ddClientInvoker.like(id);
    }

    public void unLike(String id) {
        this.ddClientInvoker.unLike(id);
    }

    public void follow(String blogCName) {
        this.ddClientInvoker.follow(blogCName);
    }

    public void unFollow(String blogCName) {
        this.ddClientInvoker.unFollow(blogCName);
    }

}
