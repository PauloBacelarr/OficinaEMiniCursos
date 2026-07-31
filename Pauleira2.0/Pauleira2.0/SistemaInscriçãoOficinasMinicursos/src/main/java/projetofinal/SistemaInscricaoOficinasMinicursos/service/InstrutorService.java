package projetofinal.SistemaInscricaoOficinasMinicursos.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InstrutorRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InstrutorResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Instrutor;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RecursoNaoEncontradoException;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RegraNegocioException;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.InstrutorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrutorService {

    private final InstrutorRepository repository;

    public InstrutorService(InstrutorRepository repository) {
        this.repository = repository;
    }

    public List<InstrutorResponseDTO> lista(){
        return repository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public InstrutorResponseDTO buscarPorId(Long id) {
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + id));
        return converterParaResponse(instrutor);
    }



    @Transactional
    public InstrutorResponseDTO cadastrar(InstrutorRequestDTO dto) {

        this.validarDados(dto);


        Instrutor novoInstrutor = new Instrutor();
        novoInstrutor.setNome(dto.nome());
        novoInstrutor.setEmail(dto.email());
        novoInstrutor.setAreaAtuacao(dto.areaAtuacao());
        novoInstrutor.setAtivo(true);

        novoInstrutor = repository.save(novoInstrutor);
        return converterParaResponse(novoInstrutor);
    }


    @Transactional
    public InstrutorResponseDTO atualizar(Long id, InstrutorRequestDTO dto) {

        this.validarDados(dto);

        Instrutor instrutorExistente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + id));

        instrutorExistente.setNome(dto.nome());
        instrutorExistente.setEmail(dto.email());
        instrutorExistente.setAreaAtuacao(dto.areaAtuacao());
        instrutorExistente.setAtivo(dto.ativo());

        instrutorExistente = repository.save(instrutorExistente);
        return converterParaResponse(instrutorExistente);
    }

    @Transactional
    public void desativar(Long id) {
        // 1. Busca o instrutor no banco ou lança erro 404 se não existir
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + id));

        // 2. Regra de Negócio: Verifica se ele já não está inativo
        if (!instrutor.getAtivo()) {
            throw new RegraNegocioException("Este instrutor já está inativado no sistema.");
        }

        // 3. Altera o atributo para false
        instrutor.setAtivo(false);

        // 4. Salva a atualização de status no banco de dados
        repository.save(instrutor);
    }

/*
    @Transactional
    public void excluir(Long id) {

        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + id);
        }

        // Deleta fisicamente do banco de dados (PostgreSQL)
        repository.deleteById(id);
    }
    */


    private InstrutorResponseDTO converterParaResponse(Instrutor instrutor) {
        return new InstrutorResponseDTO(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getAreaAtuacao(),
                instrutor.getAtivo()
        );
    }


    private void validarDados(InstrutorRequestDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do instrutor é obrigatório.");
        }
        if (dto.email() == null || dto.email().trim().isEmpty()) {
            throw new RegraNegocioException("O e-mail do instrutor é obrigatório.");
        }
        if (dto.areaAtuacao() == null || dto.areaAtuacao().trim().isEmpty()) {
            throw new RegraNegocioException("A área de atuação do instrutor é obrigatória.");
        }
    }


}
