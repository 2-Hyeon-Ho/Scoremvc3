package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Admin;
import com.nhnacademy.springmvc.repository.AdminRepository;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LoginController {
    private final AdminRepository adminRepository;

    public LoginController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping("/")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (!Objects.isNull(session) && !Objects.isNull(session.getAttribute("id"))) {
            return "thymeleaf/studentRegister";
        }

        return "thymeleaf/loginForm";
    }

    @PostMapping("/")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) {
        if (adminRepository.matches(id, pwd)) {
            HttpSession session = request.getSession(true);
            Cookie cookie = new Cookie("SESSION", session.getId());
            response.addCookie(cookie);

            modelMap.put("id", session.getId());
            session.setAttribute("id",id);
            return "thymeleaf/studentRegister";
        } else {
            return "redirect:thymeleaf/loginForm";
        }
    }

}
