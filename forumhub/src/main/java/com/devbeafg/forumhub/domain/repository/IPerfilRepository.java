package com.devbeafg.forumhub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devbeafg.forumhub.domain.model.Perfil;

public interface IPerfilRepository extends JpaRepository<Perfil, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}
