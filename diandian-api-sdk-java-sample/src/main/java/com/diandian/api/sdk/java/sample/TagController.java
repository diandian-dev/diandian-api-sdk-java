/**
 * 
 */
package com.diandian.api.sdk.java.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diandian.api.sdk.java.sample.util.RequestThreadUtils;
import com.diandian.api.sdk.java.service.TagService;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-22 上午10:16:24
 */
@Controller
@RequestMapping("/tag")
public class TagController {

    @RequestMapping("/{tag}")
    public ModelAndView getTagPost(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("tag") String tag,
            @RequestParam(value = "limit", defaultValue = "" + 20) int limit,
            @RequestParam(value = "sinceId", required = false) String sinceId,
            @RequestParam(value = "notesInfo", defaultValue = "false") boolean notesInfo,
            @RequestParam(value = "reblogInfo", defaultValue = "false") boolean reblogInfo) {

        ModelAndView mv = new ModelAndView("tagpost");
        TagService tagService = new TagService(RequestThreadUtils.getDDClientInvoker());
        if (StringUtils.isEmpty(tag)) {
            System.out.println("no tag");
            return mv;
        }
        System.out.println("tag:" + tag);
        mv.addObject("tagPost", tagService.getTagPost(tag, sinceId, limit, reblogInfo, notesInfo));
        return mv;
    }

}
