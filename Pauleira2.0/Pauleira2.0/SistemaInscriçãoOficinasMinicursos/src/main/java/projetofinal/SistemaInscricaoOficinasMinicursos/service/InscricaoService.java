package projetofinal.SistemaInscricaoOficinasMinicursos.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InscricaoRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InscricaoResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Aluno;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Inscricao;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Oficina;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.StatusInscricao;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RecursoNaoEncontradoException;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RegraNegocioException;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.AlunoRepository;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.InscricaoRepository;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.OficinaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscricaoService {
    private final InscricaoRepository repository;
    private final OficinaRepository oficinaRepository;
    private final AlunoRepository alunoRepository;

    public InscricaoService(InscricaoRepository repository, OficinaRepository oficinaRepository, AlunoRepository alunoRepository) {
        this.repository = repository;
        this.oficinaRepository = oficinaRepository;
        this.alunoRepository = alunoRepository;
    }

    public List<InscricaoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public InscricaoResponseDTO buscarPorId(Long id) {
        Inscricao inscricao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada com ID: " + id));
        return converterParaResponse(inscricao);
    }

    @Transactional
    public InscricaoResponseDTO realizarInscricao(InscricaoRequestDTO dto) {
        Oficina oficina = oficinaRepository.findById(dto.oficinaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oficina não encontrada com ID: " + dto.oficinaId()));

        if (!oficina.getAtivo()) {
            throw new RegraNegocioException("Esta oficina encontra-se inativa.");
        }

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + dto.alunoId()));

        if (repository.existsByAlunoIdAndOficinaId(aluno.getId(), oficina.getId())) {
            throw new RegraNegocioException("O aluno " + aluno.getNome() + " já está inscrito nesta oficina.");
        }

        // CORREÇÃO: Conta apenas as inscrições CONFIRMADAS para validar o limite de vagas
        long totalInscritos = repository.countByOficinaIdAndStatus(oficina.getId(), StatusInscricao.CONFIRMADA);
        if (totalInscritos >= oficina.getQuantidadeDeVagas()) {
            throw new RegraNegocioException("Não há vagas disponíveis para a oficina: " + oficina.getTitulo());
        }

        Inscricao novaInscricao = new Inscricao();
        novaInscricao.setAluno(aluno);
        novaInscricao.setOficina(oficina);
        novaInscricao.setDataInscricao(LocalDateTime.now());
        novaInscricao.setStatus(StatusInscricao.CONFIRMADA);
        novaInscricao = repository.save(novaInscricao);
        return converterParaResponse(novaInscricao);
    }

    @Transactional
    public InscricaoResponseDTO cancelar(Long id) {
        Inscricao inscricao = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada com ID: " + id));

        if (inscricao.getStatus() == StatusInscricao.CANCELADA) {
            throw new RegraNegocioException("Esta inscrição já se encontra cancelada.");
        }

        inscricao.setStatus(StatusInscricao.CANCELADA);
        inscricao = repository.save(inscricao);
        return converterParaResponse(inscricao);
    }

    private InscricaoResponseDTO converterParaResponse(Inscricao inscricao) {
        return new InscricaoResponseDTO(
                inscricao.getId(),
                inscricao.getOficina().getId(),
                inscricao.getOficina().getTitulo(),
                inscricao.getAluno().getId(),
                inscricao.getAluno().getNome(),
                inscricao.getDataInscricao(),
                inscricao.getStatus()
        );
    }
}