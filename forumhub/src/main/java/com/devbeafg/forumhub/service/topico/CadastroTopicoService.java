package com.devbeafg.forumhub.service.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Curso;
import com.devbeafg.forumhub.domain.model.Resposta;
import com.devbeafg.forumhub.domain.model.Topico;
import com.devbeafg.forumhub.domain.model.Usuario;
import com.devbeafg.forumhub.domain.repository.ICursoRepository;
import com.devbeafg.forumhub.domain.repository.IRespostaRepository;
import com.devbeafg.forumhub.domain.repository.ITopicoRepository;
import com.devbeafg.forumhub.domain.repository.IUsuarioRepository;
import com.devbeafg.forumhub.infra.dto.AtualizacaoTopicoRequest;
import com.devbeafg.forumhub.infra.dto.CadastroTopicoRequest;
import com.devbeafg.forumhub.infra.dto.TopicoRespostaDTO;

@Service
public class CadastroTopicoService {
    private final ITopicoRepository topicoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ICursoRepository cursoRepository;
    private final IRespostaRepository respostaRepository;

    public CadastroTopicoService(ITopicoRepository topicoRepository,
            IUsuarioRepository usuarioRepository,
            ICursoRepository cursoRepository,
            IRespostaRepository respostaRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.respostaRepository = respostaRepository;
    }

    public Topico cadastrarTopico(CadastroTopicoRequest req) throws Exception {
        try {
            if (topicoRepository.existsByHash(req.titulo(), req.mensagem())) {
                throw new RegraNegocioException("O Tópico inserido já existe na base de dados atual");
            }

            Usuario autor = usuarioRepository.findById(req.autorId())
                    .orElseThrow(() -> new RegraNegocioException("Autor não encontrado."));

            Curso curso = cursoRepository.findById(req.cursoId())
                    .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));

            return this.salvarNovoTopico(autor, curso, req.titulo(), req.mensagem());
        } catch (Exception e) {
            throw new Exception("Um erro ocorreu", e);
        }
    }

    public Page<TopicoRespostaDTO> listarTopicos(Pageable paginacao) {
        try {
            return topicoRepository.findAllWithRespostas(paginacao).map(TopicoRespostaDTO::fromEntity);
        } catch (Exception e) {
            throw new RuntimeException("listarTopicos::exception", e);
        }
    }

    public Topico detalharTopico(Long id) {
        try {
            return topicoRepository.findByIdWithRespostas(id)
                    .orElseThrow(() -> new RegraNegocioException("Tópico não encontrado."));

        } catch (Exception e) {
            throw new RuntimeException("detalharTopico::exception", e);
        }
    }

    public Topico atualizarTopico(Long id, AtualizacaoTopicoRequest topico) {
        try {
            var topicoOpt = topicoRepository.findById(id);
            if (!topicoOpt.isPresent()) {
                throw new RegraNegocioException("Tópico não encontrado.");
            }
            Topico topicoExistente = topicoOpt.get();

            this.validaSeDuplicado(topico.titulo(), topico.mensagem(), id);

            var autorOpt = usuarioRepository.findById(topico.autorId());
            if (!autorOpt.isPresent()) {
                throw new RegraNegocioException(
                        String.format("Usuário com ID %d não encontrado.", topico.autorId()));
            }
            Usuario autor = autorOpt.get();

            var cursoOpt = cursoRepository.findById(topico.cursoId());
            if (!cursoOpt.isPresent()) {
                throw new RegraNegocioException(
                        String.format("Curso com ID %d não encontrado.", topico.cursoId()));
            }
            Curso curso = cursoOpt.get();

            topicoExistente.setId(id);
            topicoExistente.setTitulo(topico.titulo());
            topicoExistente.setMensagem(topico.mensagem());
            topicoExistente.setAutor(autor);
            topicoExistente.setCurso(curso);
            if (topico.status() != null) {
                topicoExistente.setStatus(topico.status());
            }
            return topicoRepository.save(topicoExistente);
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao atualizar o tópico: " + e.getMessage(), e);
        }
    }

    private Topico salvarNovoTopico(Usuario autor, Curso curso, String titulo, String Message) throws Exception {
        try {
            Topico topico = new Topico();
            topico.setTitulo(titulo);
            topico.setMensagem(Message);
            topico.setAutor(autor);
            topico.setCurso(curso);

            return topicoRepository.save(topico);

        } catch (Exception e) {
            throw new Exception("salvarNovoTopico::exception", e);
        }

    }

    private void validaSeDuplicado(String titulo, String mensagem, Long id) {
        boolean duplicado = topicoRepository.existsByTituloAndMensagemAndIdNot(
                titulo, mensagem, id);

        if (duplicado) {
            throw new RegraNegocioException("Já existe um tópico com mesmo título e mensagem.");
        }
    }

    public String deletarTopico(Long id) {
        try {
            var topicoOpt = topicoRepository.findById(id);
            if (!topicoOpt.isPresent()) {
                throw new RegraNegocioException("Tópico não encontrado.");
            }
            Topico topico = topicoOpt.get();
            topicoRepository.delete(topico);
            return "Tópico deletado com sucesso.";
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao deletar o tópico: " + e.getMessage(), e);
        }
    }

    public Topico resolverTopico(Long topicoId, Long respostaId) {
        try {
            // Valida tópico
            var topicoOpt = topicoRepository.findById(topicoId);
            if (!topicoOpt.isPresent()) {
                throw new RegraNegocioException("Tópico não encontrado.");
            }
            Topico topico = topicoOpt.get();

            // Valida resposta
            var respostaOpt = respostaRepository.findById(respostaId);
            if (!respostaOpt.isPresent()) {
                throw new RegraNegocioException("Resposta não encontrada.");
            }
            Resposta resposta = respostaOpt.get();

            // Valida se a resposta pertence ao tópico
            if (!resposta.getTopico().getId().equals(topicoId)) {
                throw new RegraNegocioException("Resposta não pertence a este tópico.");
            }

            // Atualiza a resposta como solução
            resposta.setSolucao(true);
            respostaRepository.save(resposta);

            // Atualiza status do tópico para RESOLVIDO
            topico.setStatus("RESOLVIDO");
            topicoRepository.save(topico);

            return topico;
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao resolver o tópico: " + e.getMessage(), e);
        }
    }

}
