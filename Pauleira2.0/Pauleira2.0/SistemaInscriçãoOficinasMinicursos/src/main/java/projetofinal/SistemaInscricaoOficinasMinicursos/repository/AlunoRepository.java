package projetofinal.SistemaInscricaoOficinasMinicursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    // Métodos customizados (se necessários) entrarão aqui depois!
}