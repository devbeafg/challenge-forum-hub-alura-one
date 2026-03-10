package com.devbeafg.forumhub.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.devbeafg.forumhub.domain.model.Resposta;

public interface IRespostaRepository extends JpaRepository<Resposta, Long> {
    List<Resposta> findByTopicoId(Long topicoId);

    @Query("SELECT r FROM Resposta r WHERE r.autor.id = :autorId")
    Page<Resposta> findByAutorId(@Param("autorId") Long autorId, Pageable paginacao);

    List<Resposta> findByAutorId(Long autorId);

    @Modifying
    @Transactional
    @Query("UPDATE Resposta r SET r.solucao = :solucao WHERE r.id = :id")
    void updateSolucao(@Param("id") Long id, @Param("solucao") Boolean solucao);
}
