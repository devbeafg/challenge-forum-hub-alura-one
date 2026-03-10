package com.devbeafg.forumhub.service.usuario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Perfil;
import com.devbeafg.forumhub.domain.model.Usuario;
import com.devbeafg.forumhub.domain.repository.IPerfilRepository;
import com.devbeafg.forumhub.domain.repository.IUsuarioRepository;
import com.devbeafg.forumhub.infra.dto.CadastroUsuarioRequest;
import com.devbeafg.forumhub.infra.dto.UsuarioRespostaDTO;

@Service
public class CadastroUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IPerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroUsuarioService(IUsuarioRepository usuarioRepository, IPerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrarUsuario(CadastroUsuarioRequest request) {
        validaEmailDuplicado(request.email(), null);

        Set<Perfil> perfis = buscarPerfis(request.perfilIds());

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfis(perfis);

        return usuarioRepository.save(usuario);
    }

    public Page<UsuarioRespostaDTO> listarUsuarios(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(UsuarioRespostaDTO::fromEntity);
    }

    public Usuario detalharUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
    }

    public Usuario atualizarUsuario(Long id, CadastroUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));

        validaEmailDuplicado(request.email(), id);

        Set<Perfil> perfis = buscarPerfis(request.perfilIds());

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfis(perfis);

        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));

        usuarioRepository.delete(usuario);
    }

    private void validaEmailDuplicado(String email, Long idAtual) {
        boolean emailDuplicado = idAtual == null
                ? usuarioRepository.existsByEmail(email)
                : usuarioRepository.existsByEmailAndIdNot(email, idAtual);

        if (emailDuplicado) {
            throw new RegraNegocioException("Já existe usuário com esse email.");
        }
    }

    private Set<Perfil> buscarPerfis(List<Long> perfilIds) {
        Set<Perfil> perfis = new HashSet<>();

        for (Long perfilId : perfilIds) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new RegraNegocioException("Perfil com ID " + perfilId + " não encontrado."));
            perfis.add(perfil);
        }

        return perfis;
    }
}
