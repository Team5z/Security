package com.agile.demo.api.sample;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {

        return "loginForm";
    }

    @GetMapping("/login-error")
    public String loginerror(Model model) {
        model.addAttribute("loginError", true);
        return "loginForm";
    }

    @ResponseBody
    @GetMapping
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessDenied";
    }

    //@Test
   // void helloWorld() throws Exception{
        // 기대값 -> 200, 결과 -> 404 발생 ㅜㅜ
        // controller의 어노테이션 제거 + hello-world로 적용된거 수정 -> 잘 동작함
       // mockMvc.perform(MockMvcRequestBuilders.get("/hello-world"))
         //       .andDo(print())
          //      .andExpect(status().isOk()).andExpect(content().string("hello world"));
   // }
}
