package projetofinal.SistemaInscricaoOficinasMinicursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Instrutor;

@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
}