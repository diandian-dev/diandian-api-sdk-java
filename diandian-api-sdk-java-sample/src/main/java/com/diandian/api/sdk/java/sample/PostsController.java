/**
 * 
 */
package com.diandian.api.sdk.java.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diandian.api.sdk.java.sample.util.RequestThreadUtils;
import com.diandian.api.sdk.java.service.BlogService;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-22 下午4:34:56
 */
@Controller
@RequestMapping("/posts")
public class PostsController {

    @RequestMapping("")
    public ModelAndView getPosts(@RequestParam(value = "blogCName") String blogCName,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "" + 20) int limit,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) String postId,
            @RequestParam(value = "notesInfo", defaultValue = "false") boolean notesInfo,
            @RequestParam(value = "reblogInfo", defaultValue = "false") boolean reblogInfo,
            @RequestParam(value = "type", required = false) String type) {

        ModelAndView mv = new ModelAndView("posts");
        BlogService blogService = new BlogService(RequestThreadUtils.getDDClientInvoker());
        mv.addObject("postsView", blogService.getPost(blogCName, limit, offset, tag, type,
                reblogInfo, notesInfo, postId));
        return mv;
    }

}
