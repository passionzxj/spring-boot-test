package com.zhang.conf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @author
 * @date 2018-11-23
 */
@Data
@AllArgsConstructor
public class JwtUser implements UserDetails {

    public JwtUser(){}

    private String id;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<GrantedAuthority> authorities;
    private Date createTime;

    private String orgId;
    private String account;
    private Byte status;
    private String telephone;
    private String roleName;
//    private List<School> schools;

    private boolean enabled;

//    private String currentSchoolId;
//    private String currentSchoolName;


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

//    public void receiveCurrentSchool(){
//        if (schools.size() == 1) {
//            currentSchoolId = schools.get(0).getId();
//            currentSchoolName = schools.get(0).getSchoolName();
//        }
//    }


}
