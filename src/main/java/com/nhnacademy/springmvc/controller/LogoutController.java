package com.nhnacademy.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (Objects.nonNull(session) && request.getSession() == session) {
            if(session.getAttribute("id").equals("admin")) {
                Cookie cookie = new Cookie("SESSION", null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                session.setAttribute("id", null);
                session.invalidate();
            }
        }
            return "redirect:/";
    }
}
