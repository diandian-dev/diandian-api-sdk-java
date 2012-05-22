/**
 * 
 */
package com.diandian.api.sdk.java.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.diandian.api.sdk.java.sample.util.RequestThreadUtils;
import com.diandian.api.sdk.java.service.UserService;
import com.diandian.api.sdk.view.DashboardView;

/**
 * @author zhangdong zhangdong@diandian.com
 * 
 *         2012-5-14 上午11:02:35
 */
@Controller
@RequestMapping("/home")
public class IndexController {

    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("index");
        UserService userService = new UserService(RequestThreadUtils.getDDClientInvoker());
        DashboardView dashBoard = userService.getHome(null, 10, 1, null, false, false);

        mv.addObject("user", userService.getUserInfo());
        mv.addObject("dashboardView", dashBoard);
        return mv;
    }
}
