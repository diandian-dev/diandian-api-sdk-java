/**
 * 
 */
package com.diandian.api.sdk.java.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.diandian.api.sdk.java.DDClient;
import com.diandian.api.sdk.java.HttpsTools;
import com.diandian.api.sdk.java.sample.util.RequestThreadUtils;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-14 下午12:06:47
 */
@Controller
@RequestMapping("")
public class InitController {

    DDClient ddClient;

    {
        ddClient = new DDClient("appKey", "appSecret",
                "http://127.0.0.1");
        ddClient.setDdHttpTools(new HttpsTools());
    }

    @RequestMapping("oauthcode")
    public ModelAndView OauthCode(HttpServletRequest request, HttpServletResponse response) {
        String url = ddClient.getOauthUrl();
        System.out.println(url);
        return new ModelAndView("redirect:" + url);

    }

    @RequestMapping("")
    public ModelAndView oauth(HttpServletRequest request, HttpServletResponse response) {

        String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) {
            return new ModelAndView("redirect:/oauthcode");
        }
        ddClient.initAccessTokenByCode(code);
        ModelAndView mv = new ModelAndView("oauth");
        mv.addObject("token", ddClient.getToken());
        RequestThreadUtils.saveToken(ddClient.getToken(), response);
        return mv;

    }
}
