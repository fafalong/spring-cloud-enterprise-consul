package com.sc.auth.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import com.sc.auth.entity.Authority;

public interface AuthorityJPA extends JpaRepository<Authority, String> {
}
