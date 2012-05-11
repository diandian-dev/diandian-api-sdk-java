/**
 * 
 */
package com.diandian.api.sdk.java.service;

import com.diandian.api.sdk.java.DDAPIConstants;
import com.diandian.api.sdk.java.DDClientInvoker;
import com.diandian.api.sdk.model.BlogDetailInfo;
import com.diandian.api.sdk.view.FollowersView;
import com.diandian.api.sdk.view.PostView;

/**
 * 博客相关的接口的集合。用以用它获取博客信息，
 * 获取头像地址，获取某博客下的内容，发布，
 * 编辑文章等功能。
 * 
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-8 下午5:19:16
 */
public class BlogService {

    private final DDClientInvoker ddClientInvoker;

    public BlogService(DDClientInvoker ddClientInvoker) {
        this.ddClientInvoker = ddClientInvoker;
    }

    /**
     * @return the ddClientInvoker
     */
    public DDClientInvoker getDdClientInvoker() {
        return ddClientInvoker;
    }

    /**
     * 获取某一博客的信息
     * 
     * @param blogCName
     * @return
     */
    public BlogDetailInfo getBlogInfo(String blogCName) {
        return this.ddClientInvoker.getBlogInfo(blogCName);
    }

    /**
     * 获取此博客的关注都信息
     * 
     * @param blogCName
     * @param limit
     * @param offset
     * @return
     */
    public FollowersView getFollowers(String blogCName, int limit, int offset) {
        return this.ddClientInvoker.getFollowers(blogCName, limit, offset);
    }

    /**
     * 获取某博客下的文章
     * 
     * @param blogCName
     * @param limit
     * @param offset
     * @param tag
     * @param type 文章类型.如:photo,text,video,like,audio。空表示所有类型
     * @param reblogInfo
     * @param notesInfo
     * @param postId 文章Id.有此id.前面的type,tag,limit,offset都将失效。
     * @return
     */
    public PostView getPost(String blogCName, int limit, int offset, String tag, String type,
            boolean reblogInfo, boolean notesInfo, String postId) {
        return this.ddClientInvoker.getPosts(blogCName, type, limit, offset, tag, reblogInfo,
                notesInfo, postId);
    }

    /**
     * 发布文字
     * 
     * @param blogCName
     * @param state 发布状态。如:draft,queue,published
     * @param tag 文章标签。多个标签用“,”分割
     * @param slug 自定义url
     * @param title 文字标题
     * @param body 文字内容
     */
    public void postText(String blogCName, String state, String tag, String slug, String title,
            String body) {
        this.ddClientInvoker.postText(blogCName, state, tag, slug, title, body);

    }

    /**
     * 发布链接
     * 
     * @param blogCName
     * @param state 发布状态。如:draft,queue,published。默认为published
     * @param tag 文章标签。多个标签用“,”分割
     * @param slug 自定义url
     * @param title 链接标题
     * @param url 链接地址
     * @param description 链接描述
     */
    public void postLink(String blogCName, String state, String tag, String slug, String title,
            String url, String description) {
        this.ddClientInvoker.postLink(blogCName, state, tag, slug, title, url, description);
    }

    /**
     * 发布图片
     * 
     * @param blogCName
     * @param state 发布状态。如:draft,queue,published。默认为published
     * @param tag 文章标签。多个标签用“,”分割
     * @param slug 自定义url
     * @param caption 图片描述
     * @param filePath 图片全路径
     */
    public void postPhoto(String blogCName, String state, String tag, String slug, String caption,
            String filePath) {
        this.ddClientInvoker.postPhoto(blogCName, state, tag, slug, caption, filePath);
    }

    /**
     * 发布音乐
     * 
     * @param blogCName
     * @param state 发布状态。如:draft,queue,published。默认为published
     * @param tag 文章标签。多个标签用“,”分割
     * @param slug 自定义url *
     * @param caption 音乐描述
     * @param musicName 音乐名
     * @param musicSinger 歌手
     * @param filePath 音乐全路径
     */
    public void postAudio(String blogCName, String state, String tag, String slug, String caption,
            String musicName, String musicSinger, String filePath) {
        this.ddClientInvoker.postAudio(blogCName, state, tag, slug, caption, filePath, musicName,
                musicSinger);
    }

    /**
     * 发布视频
     * 
     * @param blogCName
     * @param state 发布状态。如:draft,queue,published。默认为published
     * @param tag 文章标签。多个标签用“,”分割
     * @param slug 自定义url
     * @param caption 视频描述
     * @param sourceUrl 视频源地址
     */
    public void postVideo(String blogCName, String state, String tag, String slug, String caption,
            String sourceUrl) {
        this.ddClientInvoker.postVideo(blogCName, state, tag, slug, caption, sourceUrl);
    }

    /**
     * 编辑文章
     * 
     * @param blogCName
     * @param id 文章ID
     * @param type 文章类型
     * @param state 文章发布状态
     * @param tag 文章标签,多个标签用“,”分开
     * @param slug 自定义url.
     * 
     *        不同类型以后的参数不一，只需传入对应类型的参数即可。
     * @param title
     * @param body
     * @param description
     * @param url
     * @param caption
     * @param musicName
     * @param musicSinger
     * @param filePath
     * @param sourceUrl
     */
    public void editPost(String blogCName, String id, String type, String state, String tag,
            String slug, String title, String body, String description, String url, String caption,
            String musicName, String musicSinger, String filePath, String sourceUrl) {

        if (DDAPIConstants.POST_AUDIO.equalsIgnoreCase(type)) {
            this.ddClientInvoker.editAudio(blogCName, state, tag, slug, caption, filePath,
                    musicName, musicSinger, id);
        } else if (DDAPIConstants.POST_LINK.equalsIgnoreCase(type)) {
            this.ddClientInvoker.editLink(blogCName, state, tag, slug, title, url, description, id);

        } else if (DDAPIConstants.POST_PHOTO.equalsIgnoreCase(type)) {
            this.ddClientInvoker.editPhoto(blogCName, state, tag, slug, caption, filePath, id);

        } else if (DDAPIConstants.POST_TEXT.equalsIgnoreCase(type)) {

            this.ddClientInvoker.editText(blogCName, state, tag, slug, title, body, id);
        } else if (DDAPIConstants.POST_VIDEO.equalsIgnoreCase(type)) {
            this.ddClientInvoker.editVideo(blogCName, state, tag, slug, caption, sourceUrl, id);
        }
    }

    /**
     * 装载文章
     * 
     * @param blogCName
     * @param id 文章ID
     * @param tag 标签
     * @param comment 评论
     */
    public void reblog(String blogCName, String id, String tag, String comment) {
        this.ddClientInvoker.reblogPost(blogCName, id, tag, comment);
    }

    /**
     * 删除文章
     * 
     * @param blogCName
     * @param id
     */
    public void delete(String blogCName, String id) {
        this.ddClientInvoker.deletePost(blogCName, id);
    }

    /**
     * 获取草稿
     * 
     * @param blogCName
     * @return
     */
    public PostView getDraft(String blogCName) {
        return this.ddClientInvoker.getDraft(blogCName);
    }

    /**
     * 获取投稿
     * 
     * @param blogCName
     * @return
     */
    public PostView getSubmission(String blogCName) {
        return this.ddClientInvoker.getSubmission(blogCName);
    }

    /**
     * 获取定时发布队列
     * 
     * @param blogCName
     * @return
     */
    public PostView getQueue(String blogCName) {
        return this.ddClientInvoker.getQueue(blogCName);
    }
}
