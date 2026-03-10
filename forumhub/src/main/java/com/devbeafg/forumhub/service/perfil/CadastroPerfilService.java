package com.devbeafg.forumhub.service.perfil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Perfil;
import com.devbeafg.forumhub.domain.repository.IPerfilRepository;
import com.devbeafg.forumhub.infra.dto.CadastroPerfilRequest;
import com.devbeafg.forumhub.infra.dto.PerfilDTO;

@Service
public class CadastroPerfilService {

    private final IPerfilRepository perfilRepository;

    public CadastroPerfilService(IPerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Perfil cadastrarPerfil(CadastroPerfilRequest request) {
        validaNomeDuplicado(request.nome(), null);

        Perfil perfil = new Perfil();
        perfil.setNome(request.nome());

        return perfilRepository.save(perfil);
    }

    public Page<PerfilDTO> listarPerfis(Pageable paginacao) {
        return perfilRepository.findAll(paginacao).map(PerfilDTO::fromEntity);
    }

    public Perfil detalharPerfil(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Perfil não encontrado."));
    }

    public Perfil atualizarPerfil(Long id, CadastroPerfilRequest request) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Perfil não encontrado."));

        validaNomeDuplicado(request.nome(), id);

        perfil.setNome(request.nome());

        return perfilRepository.save(perfil);
    }

    public void deletarPerfil(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Perfil não encontrado."));

        perfilRepository.delete(perfil);
    }

    private void validaNomeDuplicado(String nome, Long idAtual) {
        boolean perfilDuplicado = idAtual == null
                ? perfilRepository.existsByNome(nome)
                : perfilRepository.existsByNomeAndIdNot(nome, idAtual);

        if (perfilDuplicado) {
            throw new RegraNegocioException("Já existe perfil com esse nome.");
        }
    }
}
