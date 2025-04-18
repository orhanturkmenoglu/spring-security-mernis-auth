package com.example.spring_security_mernis_auth.repository;

import com.example.spring_security_mernis_auth.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}