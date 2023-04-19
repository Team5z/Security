package com.agile.demo.biz.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSignupResponseDto {

    private Long memberId;
    private String email;
    private String name;
    private LocalDateTime regdate;
}
