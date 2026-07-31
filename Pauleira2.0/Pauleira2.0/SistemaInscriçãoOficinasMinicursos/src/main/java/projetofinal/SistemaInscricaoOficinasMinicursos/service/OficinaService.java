package projetofinal.SistemaInscricaoOficinasMinicursos.service;

import java.util.ArrayList;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.OficinaRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.OficinaResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Instrutor;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Oficina;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.StatusInscricao;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RecursoNaoEncontradoException;
import projetofinal.SistemaInscricaoOficinasMinicursos.exception.RegraNegocioException;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.InstrutorRepository;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.OficinaRepository;
import projetofinal.SistemaInscricaoOficinasMinicursos.repository.InscricaoRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OficinaService {
    private final OficinaRepository repository;
    private final InstrutorRepository instrutorRepository;
    private final InscricaoRepository inscricaoRepository;

    public OficinaService(OficinaRepository repository, InstrutorRepository instrutorRepository, InscricaoRepository inscricaoRepository) {
        this.repository = repository;
        this.instrutorRepository = instrutorRepository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public List<OficinaResponseDTO> lista() {
        return repository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public OficinaResponseDTO buscarPorId(Long id) {
        Oficina oficina = this.buscarOficinaPorId(id);
        return converterParaResponse(oficina);
    }

    @Transactional
    public OficinaResponseDTO cadastrar(OficinaRequestDTO dto) {
        this.validarDados(dto);
        if (dto.instrutoresIds() == null || dto.instrutoresIds().isEmpty()) {
            throw new RegraNegocioException("Uma oficina deve possuir pelo menos um instrutor vinculado.");
        }
        List<Instrutor> instrutoresVinculados = new ArrayList<>();
        for (Long idInstrutor : dto.instrutoresIds()) {
            Instrutor instrutor = instrutorRepository.findById(idInstrutor)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + idInstrutor));
            if (!instrutor.getAtivo()) {
                throw new RegraNegocioException("O instrutor " + instrutor.getNome() + " está inativo e não pode ser vinculado.");
            }
            instrutoresVinculados.add(instrutor);
        }

        Oficina novaOficina = new Oficina();
        novaOficina.setTitulo(dto.titulo());
        novaOficina.setDescricao(dto.descricao());
        novaOficina.setData(dto.data());
        novaOficina.setCargaHoraria(dto.cargaHoraria());
        novaOficina.setQuantidadeDeVagas(dto.quantidadeDeVagas());
        novaOficina.setAtivo(true);
        novaOficina.setInstrutores(instrutoresVinculados);

        novaOficina = repository.save(novaOficina);
        return converterParaResponse(novaOficina);
    }

    @Transactional
    public OficinaResponseDTO atualizar(Long id, OficinaRequestDTO dto) {
        Oficina oficina = this.buscarOficinaPorId(id);
        this.validarDados(dto);

        oficina.setTitulo(dto.titulo());
        oficina.setDescricao(dto.descricao());
        oficina.setData(dto.data());
        oficina.setCargaHoraria(dto.cargaHoraria());
        oficina.setQuantidadeDeVagas(dto.quantidadeDeVagas());

        if (dto.instrutoresIds() != null && !dto.instrutoresIds().isEmpty()) {
            List<Instrutor> instrutoresVinculados = new ArrayList<>();
            for (Long idInstrutor : dto.instrutoresIds()) {
                Instrutor instrutor = instrutorRepository.findById(idInstrutor)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + idInstrutor));
                if (!instrutor.getAtivo()) {
                    throw new RegraNegocioException("O instrutor " + instrutor.getNome() + " está inativo.");
                }
                instrutoresVinculados.add(instrutor);
            }
            oficina.setInstrutores(instrutoresVinculados);
        }

        oficina = repository.save(oficina);
        return converterParaResponse(oficina);
    }

    @Transactional
    public void alterarStatus(Long id, Boolean ativo) {
        Oficina oficina = this.buscarOficinaPorId(id);
        if (ativo == null) {
            throw new RegraNegocioException("O campo 'ativo' é obrigatório.");
        }
        if (!ativo && inscricaoRepository.existsByOficinaIdAndStatus(id, StatusInscricao.CONFIRMADA)) {
            throw new RegraNegocioException("Não é possível desativar uma oficina com inscrições confirmadas.");
        }
        oficina.setAtivo(ativo);
        repository.save(oficina);
    }

    @Transactional
    public void atualizarInstrutores(Long id, List<Long> instrutoresIds) {
        Oficina oficina = this.buscarOficinaPorId(id);
        if (instrutoresIds == null || instrutoresIds.isEmpty()) {
            throw new RegraNegocioException("Uma oficina deve possuir pelo menos um instrutor vinculado.");
        }
        List<Instrutor> instrutoresVinculados = new ArrayList<>();
        for (Long idInstrutor : instrutoresIds) {
            Instrutor instrutor = instrutorRepository.findById(idInstrutor)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com o Id: " + idInstrutor));
            if (!instrutor.getAtivo()) {
                throw new RegraNegocioException("O instrutor " + instrutor.getNome() + " está inativo.");
            }
            instrutoresVinculados.add(instrutor);
        }
        oficina.setInstrutores(instrutoresVinculados);
        repository.save(oficina);
    }

    @Transactional
    public void desativar(Long id) {
        // CORREÇÃO: Impede exclusão/desativação física ou lógica se houver inscrições confirmadas
        if (inscricaoRepository.existsByOficinaIdAndStatus(id, StatusInscricao.CONFIRMADA)) {
            throw new RegraNegocioException("Tentativa de excluir oficina com inscrições confirmadas.");
        }
        Oficina oficina = this.buscarOficinaPorId(id);
        oficina.setAtivo(false);
        repository.save(oficina);
    }

    @Transactional
    public void ativar(Long id) {
        Oficina oficina = this.buscarOficinaPorId(id);
        oficina.setAtivo(true);
        repository.save(oficina);
    }

    public Oficina buscarOficinaPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oficina não encontrada com o Id: " + id));
    }

    public OficinaResponseDTO converterParaResponse(Oficina oficina) {
        return new OficinaResponseDTO(
                oficina.getId(),
                oficina.getTitulo(),
                oficina.getDescricao(),
                oficina.getData(),
                oficina.getCargaHoraria(),
                oficina.getQuantidadeDeVagas(),
                oficina.getAtivo(),
                oficina.getInstrutores()
        );
    }

    public void validarDados(OficinaRequestDTO dto) {
        if (dto.titulo() == null || dto.titulo().trim().isEmpty()) {
            throw new RegraNegocioException("O titulo da oficina é obrigatório.");
        }
        if (dto.descricao() == null || dto.descricao().trim().isEmpty()) {
            throw new RegraNegocioException("A descrição da oficina é obrigatória.");
        }
        if (dto.data() == null) {
            throw new RegraNegocioException("A data da oficina é obrigatória.");
        }
        if (dto.cargaHoraria() == null || dto.cargaHoraria() <= 0) {
            throw new RegraNegocioException("A Carga Horária da oficina é obrigatória e deve ser maior que zero.");
        }
        if (dto.quantidadeDeVagas() == null || dto.quantidadeDeVagas() <= 0) {
            throw new RegraNegocioException("A quantidade de vagas da oficina é obrigatória e deve ser maior que zero.");
        }
    }
}