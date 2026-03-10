package com.devbeafg.forumhub.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devbeafg.forumhub.domain.model.Topico;

public interface ITopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Topico t WHERE SHA2(CONCAT(t.titulo, '||', t.mensagem), 256) = " +
            "SHA2(CONCAT(:titulo, '||', :mensagem), 256)")
    boolean existsByHash(@Param("titulo") String titulo, @Param("mensagem") String mensagem);

    boolean existsByTituloAndMensagemAndIdNot(String titulo, String mensagem, Long id);

    @Query("SELECT DISTINCT t FROM Topico t LEFT JOIN FETCH t.respostas WHERE t.id = :id")
    Optional<Topico> findByIdWithRespostas(@Param("id") Long id);

    @Query("SELECT DISTINCT t FROM Topico t LEFT JOIN FETCH t.respostas ORDER BY t.titulo ASC")
    Page<Topico> findAllWithRespostas(Pageable paginacao);
}
