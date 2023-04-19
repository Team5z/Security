package com.agile.demo.biz.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Entity
@Table(name = "NAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, length = 25, unique = true)
    private long na_seq;

    @Column(nullable = false, updatable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, updatable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = true, updatable = true, length = 11)
    private String cellphone;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate regDate;


    // @OneToMany(cascade = CascadeType.MERGE)

}


