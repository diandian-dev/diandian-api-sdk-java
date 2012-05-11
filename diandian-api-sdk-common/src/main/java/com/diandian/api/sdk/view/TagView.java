/**
 * 
 */
package com.diandian.api.sdk.view;

import java.util.List;

/**
 * 
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-10 下午4:45:41
 */
public class TagView {

    private List<String> tags;

    public TagView() {

    }

    public TagView(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TagView [tags=" + this.tags + "]";
    }

}
