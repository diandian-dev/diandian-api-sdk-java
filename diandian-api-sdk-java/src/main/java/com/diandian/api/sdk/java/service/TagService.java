/**
 * 
 */
package com.diandian.api.sdk.java.service;

import com.diandian.api.sdk.java.DDClientInvoker;
import com.diandian.api.sdk.view.DashboardView;
import com.diandian.api.sdk.view.TagView;

/**
 * 标签相关接口的集合。可以用它获取获含某标签下的所有文章,
 * 订阅标签，取消订阅标签，我订阅的标签等。
 * 
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-8 下午5:19:39
 */
public class TagService {

    private final DDClientInvoker ddClientInvoker;

    public TagService(DDClientInvoker ddClientInvoker) {
        this.ddClientInvoker = ddClientInvoker;
    }

    /**
     * 获取含某tag的post
     * 
     * @param tag
     * @return
     */
    public DashboardView getTagPost(String tag) {
        return this.ddClientInvoker.getTagPost(tag);
    }

    /**
     * 获取我收藏的tag
     * 
     * @return
     */
    public TagView getMyTags() {
        return this.ddClientInvoker.getMyTags();

    }

    /**
     * 收藏标签
     * 
     * @param tag
     */
    public void watchTag(String tag) {
        this.ddClientInvoker.watchTag(tag);
    }

    /**
     * 取消收藏标签
     * 
     * @param tag
     */
    public void unwatchTag(String tag) {
        this.ddClientInvoker.unwatchTag(tag);
    }

}
