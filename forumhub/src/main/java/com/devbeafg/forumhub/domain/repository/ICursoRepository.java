package com.devbeafg.forumhub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devbeafg.forumhub.domain.model.Curso;

public interface ICursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNomeAndCategoria(String nome, String categoria);

    boolean existsByNomeAndCategoriaAndIdNot(String nome, String categoria, Long id);

}
