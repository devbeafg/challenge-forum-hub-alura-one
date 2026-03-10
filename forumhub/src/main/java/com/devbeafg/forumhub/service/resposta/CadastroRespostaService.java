package com.devbeafg.forumhub.service.resposta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Resposta;
import com.devbeafg.forumhub.domain.model.Topico;
import com.devbeafg.forumhub.domain.model.Usuario;
import com.devbeafg.forumhub.domain.repository.IRespostaRepository;
import com.devbeafg.forumhub.domain.repository.ITopicoRepository;
import com.devbeafg.forumhub.domain.repository.IUsuarioRepository;
import com.devbeafg.forumhub.infra.dto.AtualizacaoRespostaRequest;
import com.devbeafg.forumhub.infra.dto.CadastroRespostaRequest;
import com.devbeafg.forumhub.infra.dto.RespostaDTO;
import com.devbeafg.forumhub.infra.dto.UsuarioRespostasDTO;

@Service
public class CadastroRespostaService {
    private final IRespostaRepository respostaRepository;
    private final ITopicoRepository topicoRepository;
    private final IUsuarioRepository usuarioRepository;

    public CadastroRespostaService(IRespostaRepository respostaRepository,
            ITopicoRepository topicoRepository,
            IUsuarioRepository usuarioRepository) {
        this.respostaRepository = respostaRepository;
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Resposta cadastrarResposta(CadastroRespostaRequest req) throws Exception {
        try {
            Topico topico = topicoRepository.findById(req.topicoId())
                    .orElseThrow(() -> new RegraNegocioException("Tópico não encontrado."));

            Usuario autor = usuarioRepository.findById(req.autorId())
                    .orElseThrow(() -> new RegraNegocioException("Autor não encontrado."));

            Resposta resposta = new Resposta();
            resposta.setMensagem(req.mensagem());
            resposta.setTopico(topico);
            resposta.setAutor(autor);
            resposta.setSolucao(false);

            return respostaRepository.save(resposta);
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Um erro ocorreu ao cadastrar resposta", e);
        }
    }

    public Page<RespostaDTO> listarRespostas(Pageable paginacao) {
        try {
            return respostaRepository.findAll(paginacao).map(RespostaDTO::fromEntity);
        } catch (Exception e) {
            throw new RuntimeException("listarRespostas::exception", e);
        }
    }

    public RespostaDTO detalharResposta(Long id) {
        try {
            Resposta resposta = respostaRepository.findById(id)
                    .orElseThrow(() -> new RegraNegocioException("Resposta não encontrada."));
            return RespostaDTO.fromEntity(resposta);
        } catch (Exception e) {
            throw new RuntimeException("detalharResposta::exception", e);
        }
    }

    public RespostaDTO atualizarResposta(Long id, AtualizacaoRespostaRequest req) throws Exception {
        try {
            Resposta resposta = respostaRepository.findById(id)
                    .orElseThrow(() -> new RegraNegocioException("Resposta não encontrada."));

            resposta.setMensagem(req.mensagem());
            if (req.solucao() != null) {
                resposta.setSolucao(req.solucao());
            }

            Resposta atualizada = respostaRepository.save(resposta);
            return RespostaDTO.fromEntity(atualizada);
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Um erro ocorreu ao atualizar resposta", e);
        }
    }

    public void deletarResposta(Long id) throws Exception {
        try {
            Resposta resposta = respostaRepository.findById(id)
                    .orElseThrow(() -> new RegraNegocioException("Resposta não encontrada."));
            respostaRepository.delete(resposta);
        } catch (RegraNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Um erro ocorreu ao deletar resposta", e);
        }
    }

    public Page<UsuarioRespostasDTO> listarRespostasDoUsuario(Long usuarioId, Pageable paginacao) {
        try {
            usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
            return respostaRepository.findByAutorId(usuarioId, paginacao).map(UsuarioRespostasDTO::fromEntity);
        } catch (Exception e) {
            throw new RuntimeException("listarRespostasDoUsuario::exception", e);
        }
    }
}
