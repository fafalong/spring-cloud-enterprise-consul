package com.sc.auth.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sc.auth.entity.Authority;
import com.sc.auth.entity.OAuthUser;
import com.sc.auth.entity.User;
import com.sc.auth.jpa.UserJPA;

 
@Component("userDetailsService")
public class AuthUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserJPA userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {

        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        }
        //获取用户的所有权限并且SpringSecurity需要的集合
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : userFromDatabase.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }
        //返回一个SpringSecurity需要的用户对象
        return new OAuthUser(
                userFromDatabase.getUsername(),
                userFromDatabase.getPassword(),
                grantedAuthorities);

    }
}
