package projetofinal.SistemaInscricaoOficinasMinicursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Inscricao;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.StatusInscricao;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    // Conta apenas vagas ocupadas por inscrições CONFIRMADAS (Inscrições CANCELADAS liberam vaga)
    long countByOficinaIdAndStatus(Long oficinaId, StatusInscricao status);

    // Verifica se a oficina possui alguma inscrição ativa antes de permitir a exclusão
    boolean existsByOficinaIdAndStatus(Long oficinaId, StatusInscricao status);

    boolean existsByAlunoIdAndOficinaId(Long alunoId, Long oficinaId);
}