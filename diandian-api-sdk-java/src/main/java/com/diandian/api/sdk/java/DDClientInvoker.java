/**
 * 
 */
package com.diandian.api.sdk.java;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.diandian.api.sdk.DDJSONParser;
import com.diandian.api.sdk.Token;
import com.diandian.api.sdk.exception.DDAPIException;
import com.diandian.api.sdk.model.BlogDetailInfo;
import com.diandian.api.sdk.model.UserDetailInfo;
import com.diandian.api.sdk.util.BaseUtil;
import com.diandian.api.sdk.view.DashboardView;
import com.diandian.api.sdk.view.FollowersView;
import com.diandian.api.sdk.view.FollowingView;
import com.diandian.api.sdk.view.LikesPostView;
import com.diandian.api.sdk.view.PostView;
import com.diandian.api.sdk.view.TagView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-8 下午6:03:03
 */
public class DDClientInvoker {

    private final DDClient ddClient;

    private final DDJSONParser parser;

    private final DDJSON2BeanFactory json2BeanFactory;

    private static DDClientInvoker instance;

    private static final String GET_HOME_URL = DDAPIConstants.HOST + "v1/user/home";

    private static final String GET_FOLLOWERS_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/followers";

    private static final String GET_BLOG_INFO_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/info";

    private static final String GET_ALL_TYPE_POST_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/posts";

    private static final String GET_ONE_TYPE_POST_FORMAT = GET_ALL_TYPE_POST_FORMAT + "/%s";

    private static final String POST_POST_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/post";

    private static final String EDIT_POST_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/post/edit";

    private static final String REBLOG_POST_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/post/reblog";

    private static final String DELETE_POST_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/post/delete";

    private static final String GET_QUEUE_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/queue";

    private static final String GET_DRAFT_FORMAT = DDAPIConstants.HOST + "v1/blog/%s/draft";

    private static final String GET_SUBMISSION_FORMAT = DDAPIConstants.HOST
            + "v1/blog/%s/submission";

    private static final String GET_USER_INFO_URL = DDAPIConstants.HOST + "v1/user/info";

    private static final String GET_LIKES_URL = DDAPIConstants.HOST + "v1/user/likes";

    private static final String POST_LIKE_URL = DDAPIConstants.HOST + "v1/user/like";

    private static final String DELETE_LIKE_URL = DDAPIConstants.HOST + "v1/user/unlike";

    private static final String GET_FOLLOWING_URL = DDAPIConstants.HOST + "v1/user/following";

    private static final String POST_FOLLOW_URL = DDAPIConstants.HOST + "v1/user/follow";

    private static final String DELETE_FOLLOW_URL = DDAPIConstants.HOST + "v1/user/unfollow";

    private static final String GET_TAG_POSTS_FORMAT = DDAPIConstants.HOST + "v1/tag/posts/%s";

    private static final String POST_WATCH_FORMAT = DDAPIConstants.HOST + "v1/tag/watch/%s";

    private static final String DELETE_WATCH_FORMAT = DDAPIConstants.HOST + "v1/tag/unwatch/%s";

    private static final String GET_MYTAGS_URL = DDAPIConstants.HOST + "v1/tag/mytags";

    /**
     * 初始化。需要DDClient和parser。二者皆不能为空
     * 
     * @param ddClient
     * @param parser
     * @return
     */
    public synchronized static DDClientInvoker init(DDClient ddClient, DDJSONParser parser) {
        instance = new DDClientInvoker(ddClient, parser);
        return instance;
    }

    public synchronized static DDClientInvoker getInstance() {
        if (instance == null) {
            throw new DDAPIException(DDAPIConstants.DDCLIENT_INVOKER_NOT_INIT, "invoker not init",
                    null);
        }
        return instance;
    }

    /**
     * 
     * @param ddClient
     * @param parser
     */
    private DDClientInvoker(DDClient ddClient, DDJSONParser parser) {
        this.ddClient = ddClient;
        this.parser = parser;
        this.json2BeanFactory = new DDJSON2BeanFactory();
    }

    /**
     * 同步获取博客信息
     * 
     * @param blogCName
     * @return BlogDetailInfo
     */
    public BlogDetailInfo getBlogInfo(String blogCName) {
        String url = String.format(GET_BLOG_INFO_FORMAT, blogCName);
        return getInfo(url, null, BlogDetailInfo.class, ddClient.getToken());
    }

    private DDParameters getParamForLimitAndOffset(int limit, int offset) {
        limit = Math.max(0, limit);
        DDParameters param = new DDParameters();
        param.add("limit", Math.min(limit, DDAPIConstants.MAX_ITEM_PERPAGE) + "");
        param.add("offset", Math.max(0, offset) + "");
        return param;

    }

    /**
     * 获取博客关注者信息
     * 
     * @param blogCName
     * @param limit
     * @param offset
     * @return FollowersView
     */
    public FollowersView getFollowers(String blogCName, int limit, int offset) {
        String url = String.format(GET_FOLLOWERS_FORMAT, blogCName);
        return getInfo(url, getParamForLimitAndOffset(limit, offset), FollowersView.class,
                ddClient.getToken());
    }

    private DDParameters getParamForPosts(String type, int limit, int offset, String tag,
            boolean reblogInfo, boolean notesInfo, String postId) {
        DDParameters param = new DDParameters();
        if (!StringUtils.isEmpty(postId)) {
            param.add("id", postId);
        } else {
            limit = Math.max(0, limit);
            param.add("limit", Math.min(limit, DDAPIConstants.MAX_ITEM_PERPAGE) + "");
            param.add("offset", Math.max(0, offset) + "");
            if (tag != null) {
                param.add("tag", tag);
            }
        }
        param.add("reblogInfo", String.valueOf(reblogInfo));
        param.add("notesInfo", String.valueOf(notesInfo));
        return param;
    }

    private String getPostUrlByType(String type, String blogCName) {
        if (StringUtils.isEmpty(type)) {
            return String.format(GET_ALL_TYPE_POST_FORMAT, blogCName);
        } else {
            return String.format(GET_ONE_TYPE_POST_FORMAT, blogCName, type);
        }
    }

    /**
     * 获取post。
     * 注：postId不为空，则取一post
     * 
     * @param blogCName
     * @param type
     * @param limit
     * @param offset
     * @param tag
     * @param reblogInfo
     * @param notesInfo
     * @param postId
     * @return PostView
     */
    public PostView getPosts(String blogCName, String type, int limit, int offset, String tag,
            boolean reblogInfo, boolean notesInfo, String postId) {
        String url = getPostUrlByType(type, blogCName);
        return getInfo(url,
                getParamForPosts(type, limit, offset, tag, notesInfo, notesInfo, postId),
                PostView.class, ddClient.getToken());
    }

    private DDParameters getParamForHome(String type, int limit, int offset, String sinceId,
            boolean reblogInfo, boolean notesInfo) {
        DDParameters param = new DDParameters();
        limit = Math.max(0, limit);
        if (!StringUtils.isEmpty(type)) {
            param.add("type", type);
        }
        param.add("limit", Math.min(limit, DDAPIConstants.MAX_ITEM_PERPAGE) + "");
        param.add("offset", Math.max(offset, 0) + "");
        if (!StringUtils.isEmpty(sinceId)) {
            param.add("sinceId", sinceId);
        }
        param.add("reblogInfo", String.valueOf(reblogInfo));
        param.add("notesInfo", String.valueOf(notesInfo));
        return param;
    }

    /**
     * 获取当前用户的着页信息
     * 
     * @param type
     * @param limit
     * @param offset
     * @param sinceId
     * @param reblogInfo
     * @param notesInfo
     * @return
     */
    public DashboardView getHome(String type, int limit, int offset, String sinceId,
            boolean reblogInfo, boolean notesInfo) {
        return getInfo(GET_HOME_URL,
                getParamForHome(type, limit, offset, sinceId, reblogInfo, notesInfo),
                DashboardView.class, ddClient.getToken());
    }

    private DDParameters getBaseParamForPost(String type, String state, String tag, String slug) {
        DDParameters param = new DDParameters();
        if (!StringUtils.isEmpty(type)) {
            param.add("type", type);
        }
        if (!StringUtils.isEmpty(state)) {
            param.add("state", state);
        }
        if (!StringUtils.isEmpty(tag)) {
            param.add("tag", tag);
        }
        if (!StringUtils.isEmpty(slug)) {
            param.add("slug", slug);
        }
        return param;
    }

    private DDParameters getTextParamForPost(String type, String state, String tag, String slug,
            String title, String body) {
        DDParameters param = getBaseParamForPost(type, state, tag, slug);
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(body)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS,
                    "title and body are both null", null);
        }
        if (!StringUtils.isEmpty(title)) {
            param.add("title", title);
        }
        if (!StringUtils.isEmpty(body)) {
            param.add("bode", body);
        }
        return param;
    }

    /**
     * 发布文字
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param title
     * @param body
     */
    public void postText(String blogCName, String state, String tag, String slug, String title,
            String body) {
        String url = String.format(POST_POST_FORMAT, blogCName);
        this.doPost(url,
                getTextParamForPost(DDAPIConstants.POST_TEXT, state, tag, slug, title, body),
                ddClient.getToken());

    }

    private DDParameters getLinkParamForPost(String type, String state, String tag, String slug,
            String title, String url, String description) {
        DDParameters param = getBaseParamForPost(type, state, tag, slug);
        if (StringUtils.isEmpty(url)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "url can not be null", null);
        }
        param.add("url", url);
        if (!StringUtils.isEmpty(title)) {
            param.add("title", title);
        }
        if (!StringUtils.isEmpty(description)) {
            param.add("description", description);
        }
        return param;
    }

    /**
     * 发布链接
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param title
     * @param url
     * @param description
     */
    public void postLink(String blogCName, String state, String tag, String slug, String title,
            String url, String description) {
        String postUrl = String.format(POST_POST_FORMAT, blogCName);
        this.doPost(postUrl, this.getLinkParamForPost(DDAPIConstants.POST_LINK, state, tag, slug,
                title, postUrl, description), ddClient.getToken());

    }

    private DDParameters getVideoParamForPost(String type, String state, String tag, String slug,
            String caption, String sourceUrl) {
        DDParameters param = this.getBaseParamForPost(type, state, tag, slug);
        if (StringUtils.isEmpty(sourceUrl)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS,
                    "sourceUrl can not by null", null);
        }
        param.add("sourceUrl", sourceUrl);
        if (!StringUtils.isEmpty(caption)) {
            param.add("caption", caption);
        }
        return param;

    }

    /**
     * 发布视频
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param sourceUrl
     */
    public void postVideo(String blogCName, String state, String tag, String slug, String caption,
            String sourceUrl) {
        String url = String.format(POST_POST_FORMAT, blogCName);
        this.doPost(url, this.getVideoParamForPost(DDAPIConstants.POST_VIDEO, state, tag, slug,
                caption, sourceUrl), ddClient.getToken());

    }

    private DDParameters getAudioParamForPost(String type, String state, String tag, String slug,
            String caption, String musicName, String musicSinger) {
        DDParameters param = this.getBaseParamForPost(type, state, tag, slug);
        if (StringUtils.isEmpty(musicName) || StringUtils.isEmpty(musicSinger)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS,
                    "musicName or musicSinger is null.musicName:" + musicName + "  musicSinger:"
                            + musicSinger, null);
        }
        param.add("musicName", musicName);
        param.add("musicSinger", musicSinger);
        if (!StringUtils.isEmpty(caption)) {
            param.add("caption", caption);
        }
        return param;
    }

    /**
     * 发布音乐
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param filePath
     * @param musicName
     * @param musicSinger
     */
    public void postAudio(String blogCName, String state, String tag, String slug, String caption,
            String filePath, String musicName, String musicSinger) {
        byte[] data = BaseUtil.getFileData(new File(filePath));
        String url = String.format(POST_POST_FORMAT, blogCName);
        ddClient.doUpload(url, this.getAudioParamForPost(DDAPIConstants.POST_AUDIO, state, tag,
                slug, caption, musicName, musicSinger), "data", filePath, data, ddClient.getToken());
    }

    private DDParameters getPhotoParamForPost(String type, String state, String tag, String slug,
            String caption) {
        DDParameters param = this.getBaseParamForPost(type, state, tag, slug);
        if (!StringUtils.isEmpty(caption)) {
            param.add("caption", caption);
        }
        return param;
    }

    /**
     * 发布照片
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param filePath
     */
    public void postPhoto(String blogCName, String state, String tag, String slug, String caption,
            String filePath) {
        byte[] data = BaseUtil.getFileData(new File(filePath));
        String url = String.format(POST_POST_FORMAT, blogCName);
        ddClient.doUpload(url,
                this.getPhotoParamForPost(DDAPIConstants.POST_PHOTO, state, tag, slug, caption),
                "data", filePath, data, ddClient.getToken());

    }

    /**
     * 获取信息
     * 
     * @param url
     * @param params
     * @param classType
     * @param token
     * @return
     */
    public <T> T getInfo(String url, DDParameters params, Class<T> classType, Token token) {
        String result = ddClient.doGet(url, token, params);
        return json2BeanFactory.fromJson2Bean(result, classType, parser);

    }

    /**
     * 发布信息。
     * 异常：DDAPIException
     * 
     * @param url
     * @param params
     * @param token
     */
    public void doPost(String url, DDParameters params, Token token) {
        String result = ddClient.doPost(url, token, params);
        try {
            JSONObject meta = new JSONObject(result).getJSONObject("meta");
            int code = meta.getInt("status");
            if (code != DDAPIConstants.RESULT_OK) {
                throw new DDAPIException(code, meta.getString("msg"), null);
            }
        } catch (JSONException e) {
            throw new DDAPIException(DDAPIConstants.DEFAULT_ERR_CODE, e.getMessage(), e);
        }

    }

    private DDParameters getEditParam(String type, String state, String tag, String slug,
            String title, String body, String url, String description, String caption,
            String musicName, String musicSinger, String sourceUrl, String id) {

        DDParameters param = null;
        if (DDAPIConstants.POST_AUDIO.equalsIgnoreCase(type)) {
            param = this.getAudioParamForPost(type, state, tag, slug, caption, musicName,
                    musicSinger);
        } else if (DDAPIConstants.POST_LINK.equalsIgnoreCase(type)) {
            param = this.getLinkParamForPost(type, state, tag, slug, title, url, description);
        } else if (DDAPIConstants.POST_PHOTO.equalsIgnoreCase(type)) {
            param = this.getPhotoParamForPost(type, state, tag, slug, caption);
        } else if (DDAPIConstants.POST_TEXT.equalsIgnoreCase(type)) {
            param = this.getTextParamForPost(type, state, tag, slug, title, body);
        } else if (DDAPIConstants.POST_VIDEO.equalsIgnoreCase(type)) {
            param = this.getVideoParamForPost(type, state, tag, slug, caption, sourceUrl);
        }

        if (param == null) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "invalid type type" + type,
                    null);
        }
        if (StringUtils.isEmpty(id)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "id can not be empty"
                    + type, null);
        }
        param.add("id", id);
        return param;
    }

    /**
     * 编辑文字
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param title
     * @param body
     * @param id
     */
    public void editText(String blogCName, String state, String tag, String slug, String title,
            String body, String id) {
        String url = String.format(EDIT_POST_FORMAT, blogCName);
        this.doPost(url, this.getEditParam(DDAPIConstants.POST_TEXT, state, tag, slug, title, body,
                url, null, null, null, null, null, id), ddClient.getToken());
    }

    /**
     * 编辑链接
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param title
     * @param url
     * @param description
     * @param id
     */
    public void editLink(String blogCName, String state, String tag, String slug, String title,
            String url, String description, String id) {
        this.doPost(String.format(EDIT_POST_FORMAT, blogCName), this.getEditParam(
                DDAPIConstants.POST_LINK, state, tag, slug, title, null, url, description, null,
                null, null, null, id), ddClient.getToken());

    }

    /**
     * 编辑视频
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param sourceUrl
     * @param id
     */
    public void editVideo(String blogCName, String state, String tag, String slug, String caption,
            String sourceUrl, String id) {
        this.doPost(String.format(EDIT_POST_FORMAT, blogCName), this.getEditParam(
                DDAPIConstants.POST_VIDEO, state, tag, slug, null, null, null, null, caption, null,
                null, sourceUrl, id), ddClient.getToken());
    }

    /**
     * 编辑音乐
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param filePath
     * @param musicName
     * @param musicSinger
     * @param id
     */
    public void editAudio(String blogCName, String state, String tag, String slug, String caption,
            String filePath, String musicName, String musicSinger, String id) {
        byte[] data = BaseUtil.getFileData(new File(filePath));
        ddClient.doUpload(String.format(EDIT_POST_FORMAT, blogCName), this.getEditParam(
                DDAPIConstants.POST_AUDIO, state, tag, slug, null, null, null, null, caption,
                musicName, musicSinger, null, id), "data", filePath, data, ddClient.getToken());

    }

    /**
     * 编辑图片
     * 
     * @param blogCName
     * @param state
     * @param tag
     * @param slug
     * @param caption
     * @param filePath
     * @param id
     */
    public void editPhoto(String blogCName, String state, String tag, String slug, String caption,
            String filePath, String id) {
        byte[] data = BaseUtil.getFileData(new File(filePath));
        ddClient.doUpload(String.format(EDIT_POST_FORMAT, blogCName), this.getEditParam(
                DDAPIConstants.POST_PHOTO, state, tag, slug, null, null, null, null, caption, null,
                null, null, id), "data", filePath, data, ddClient.getToken());
    }

    private DDParameters getParamFromReblogPost(String id, String tag, String comment) {
        DDParameters param = new DDParameters();
        if (StringUtils.isEmpty(id)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "post id can not by null",
                    null);
        }
        param.add("id", id);
        if (!StringUtils.isEmpty(tag)) {
            param.add("tag", tag);
        }
        if (!StringUtils.isEmpty(comment)) {
            param.add("comment", comment);
        }
        return param;
    }

    /**
     * 转载
     * 
     * @param blogCName
     * @param id
     * @param tag
     * @param comment
     */
    public void reblogPost(String blogCName, String id, String tag, String comment) {
        this.doPost(String.format(REBLOG_POST_FORMAT, blogCName),
                this.getParamFromReblogPost(id, tag, comment), ddClient.getToken());
    }

    private DDParameters getParamForId(String id) {
        DDParameters param = new DDParameters();
        if (StringUtils.isEmpty(id)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "id can not by null", null);
        }
        param.add("id", id);
        return param;

    }

    /**
     * 删除文章
     * 
     * @param blogCName
     * @param id
     */
    public void deletePost(String blogCName, String id) {
        this.doPost(String.format(DELETE_POST_FORMAT, blogCName), this.getParamForId(id),
                ddClient.getToken());
    }

    /**
     * 获取发布队列
     * 
     * @param blogCName
     * @return PostView
     */
    public PostView getQueue(String blogCName) {
        return this.getInfo(String.format(GET_QUEUE_FORMAT, blogCName), null, PostView.class,
                ddClient.getToken());
    }

    /**
     * 获取草稿队列
     * 
     * @param blogCName
     * @return PostView
     */
    public PostView getDraft(String blogCName) {
        return this.getInfo(String.format(GET_DRAFT_FORMAT, blogCName), null, PostView.class,
                ddClient.getToken());
    }

    /**
     * 获取投稿
     * 
     * @param blogCName
     * @return PostView
     */
    public PostView getSubmission(String blogCName) {
        return this.getInfo(String.format(GET_SUBMISSION_FORMAT, blogCName), null, PostView.class,
                ddClient.getToken());
    }

    /**
     * 获取用户信息
     * 
     * @return UserDetailInfo
     */
    public UserDetailInfo getUserInfo() {
        return this.getInfo(GET_USER_INFO_URL, null, UserDetailInfo.class, ddClient.getToken());
    }

    /**
     * 获取我的喜欢
     * 
     * @param limit
     * @param offset
     * @return LikesPostView
     */
    public LikesPostView getLikes(int limit, int offset) {
        return this.getInfo(GET_LIKES_URL, getParamForLimitAndOffset(limit, offset),
                LikesPostView.class, ddClient.getToken());
    }

    /**
     * 获取我关注的博客列表
     * 
     * @param limit
     * @param offset
     * @return FollowView
     */
    public FollowingView getFollowing(int limit, int offset) {
        return this.getInfo(GET_FOLLOWING_URL, getParamForLimitAndOffset(limit, offset),
                FollowingView.class, ddClient.getToken());
    }

    /**
     * 喜欢
     * 
     * @param id
     */
    public void like(String id) {
        this.doPost(POST_LIKE_URL, getParamForId(id), ddClient.getToken());
    }

    /**
     * 取消喜欢
     * 
     * @param id
     */
    public void unLike(String id) {
        this.doPost(DELETE_LIKE_URL, getParamForId(id), ddClient.getToken());
    }

    private DDParameters getBlogCNameParam(String blogCName) {
        DDParameters param = new DDParameters();
        if (StringUtils.isEmpty(blogCName)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS,
                    "blogCName can not be null", null);
        }
        param.add("blogCName", blogCName);
        return param;
    }

    /**
     * 关注
     * 
     * @param blogCName
     */
    public void follow(String blogCName) {
        this.doPost(POST_FOLLOW_URL, this.getBlogCNameParam(blogCName), ddClient.getToken());
    }

    /**
     * 取消关注
     * 
     * @param blogCName
     */
    public void unFollow(String blogCName) {
        this.doPost(DELETE_FOLLOW_URL, this.getBlogCNameParam(blogCName), ddClient.getToken());
    }

    /**
     * 检查tag。获tag合法，直接抛出异常
     * 
     * @param tag
     */
    private void checkTag(String tag) {
        if (StringUtils.isEmpty(tag)) {
            throw new DDAPIException(DDAPIConstants.INVALID_PARAMATERS, "tag can not be null", null);
        }
    }

    private DDParameters getParamForTagPost(String tag, int limit, String sinceId,
            Boolean reblogInfo, Boolean notesInfo) {
        checkTag(tag);

        DDParameters param = new DDParameters();
        param.add("limit", limit + "");
        if (StringUtils.isNotEmpty(sinceId)) {
            param.add("sinceId", sinceId);
        }
        param.add("reblogInfo", reblogInfo + "");
        param.add("notesInfo", notesInfo + "");
        return param;
    }

    /**
     * 获取标有某tag的posts
     * 
     * @param tag
     * @return
     */
    public DashboardView getTagPost(String tag, int limit, String sinceId, Boolean reblogInfo,
            Boolean notesInfo) {
        try {
            return this.getInfo(
                    String.format(GET_TAG_POSTS_FORMAT, URLEncoder.encode(tag, "UTF-8")),
                    getParamForTagPost(tag, limit, sinceId, reblogInfo, notesInfo),
                    DashboardView.class, this.ddClient.getToken());
        } catch (UnsupportedEncodingException e) {
            throw new DDAPIException(400, e.getMessage(), null);
        }
    }

    /**
     * 订阅标签
     * 
     * @param tag
     */
    public void watchTag(String tag) {
        checkTag(tag);
        try {
            this.doPost(String.format(POST_WATCH_FORMAT, URLEncoder.encode(tag, "UTF-8")), null,
                    ddClient.getToken());
        } catch (UnsupportedEncodingException e) {
            throw new DDAPIException(400, e.getMessage(), null);
        }

    }

    /**
     * 取消订阅标签
     * 
     * @param tag
     */
    public void unwatchTag(String tag) {
        checkTag(tag);
        try {
            this.doPost(String.format(DELETE_WATCH_FORMAT, URLEncoder.encode(tag, "UTF-8")), null,
                    ddClient.getToken());
        } catch (UnsupportedEncodingException e) {
            throw new DDAPIException(400, e.getMessage(), null);
        }

    }

    /**
     * 获取我订阅的标签
     * 
     * @return
     */
    public TagView getMyTags() {
        return this.getInfo(GET_MYTAGS_URL, null, TagView.class, ddClient.getToken());
    }

}
