package com.devbeafg.forumhub.service.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Curso;
import com.devbeafg.forumhub.domain.repository.ICursoRepository;
import com.devbeafg.forumhub.infra.dto.CadastroCursoRequest;
import com.devbeafg.forumhub.infra.dto.CursoDTO;

@Service
public class CadastroCursoService {

    private final ICursoRepository cursoRepository;

    public CadastroCursoService(ICursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso cadastrarCurso(CadastroCursoRequest request) {
        validaCursoDuplicado(request.nome(), request.categoria(), null);

        Curso curso = new Curso();
        curso.setNome(request.nome());
        curso.setCategoria(request.categoria());

        return cursoRepository.save(curso);
    }

    public Page<CursoDTO> listarCursos(Pageable paginacao) {
        return cursoRepository.findAll(paginacao).map(CursoDTO::fromEntity);
    }

    public Curso detalharCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));
    }

    public Curso atualizarCurso(Long id, CadastroCursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));

        validaCursoDuplicado(request.nome(), request.categoria(), id);

        curso.setNome(request.nome());
        curso.setCategoria(request.categoria());

        return cursoRepository.save(curso);
    }

    public void deletarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));

        cursoRepository.delete(curso);
    }

    private void validaCursoDuplicado(String nome, String categoria, Long idAtual) {
        boolean cursoDuplicado = idAtual == null
                ? cursoRepository.existsByNomeAndCategoria(nome, categoria)
                : cursoRepository.existsByNomeAndCategoriaAndIdNot(nome, categoria, idAtual);

        if (cursoDuplicado) {
            throw new RegraNegocioException("Já existe curso com esse nome e categoria.");
        }
    }
}
