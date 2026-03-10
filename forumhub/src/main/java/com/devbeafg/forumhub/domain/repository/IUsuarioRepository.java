package com.devbeafg.forumhub.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devbeafg.forumhub.domain.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Usuario> findByEmail(String email);
}
