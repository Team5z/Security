package com.agile.demo.biz.user;

import com.agile.demo.biz.account.AccountEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "NUSER")
@Data
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name="nu_seq")
    private Long nu_seq;

    @OneToOne
    @JoinColumn(name="na_seq")
    private AccountEntity accountEntity;

     @Enumerated(EnumType.STRING)
     @Column()
     private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

}

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }


//    @AllArgsConstructor
//    @Getter
//    public enum Role {
//        ADMIN("ROLE_ADMIN");
//        private String value;
//    }


