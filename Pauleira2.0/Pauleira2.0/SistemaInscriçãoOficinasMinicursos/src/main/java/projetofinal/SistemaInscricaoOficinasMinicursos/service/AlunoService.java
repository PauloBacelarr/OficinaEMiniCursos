package projetofinal.SistemaInscricaoOficinasMinicursos.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.AlunoRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.AlunoResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Aluno;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RecursoNaoEncontradoException;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RegraNegocioException;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.AlunoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public List<AlunoResponseDTO> lista() {
        return repository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com o Id: " + id));
        return converterParaResponse(aluno);
    }

    @Transactional
    public AlunoResponseDTO cadastrar(AlunoRequestDTO dto) {
        this.validarDados(dto);

        Aluno novoAluno = new Aluno();
        novoAluno.setNome(dto.nome());
        novoAluno.setEmail(dto.email());
        novoAluno.setMatricula(dto.matricula());

        novoAluno = repository.save(novoAluno);
        return converterParaResponse(novoAluno);
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, AlunoRequestDTO dto) {
        this.validarDados(dto);

        Aluno alunoExistente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com o Id: " + id));

        alunoExistente.setNome(dto.nome());
        alunoExistente.setEmail(dto.email());
        alunoExistente.setMatricula(dto.matricula());

        alunoExistente = repository.save(alunoExistente);
        return converterParaResponse(alunoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado com o Id: " + id);
        }
        // Exclusão física do banco de dados
        repository.deleteById(id);
    }

    // Métodos Auxiliares
    private AlunoResponseDTO converterParaResponse(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatricula()
        );
    }

    private void validarDados(AlunoRequestDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do aluno é obrigatório.");
        }
        if (dto.email() == null || dto.email().trim().isEmpty()) {
            throw new RegraNegocioException("O e-mail do aluno é obrigatório.");
        }
        if (dto.matricula() == null || dto.matricula().trim().isEmpty()) {
            throw new RegraNegocioException("A matrícula do aluno é obrigatória.");
        }
    }
}

