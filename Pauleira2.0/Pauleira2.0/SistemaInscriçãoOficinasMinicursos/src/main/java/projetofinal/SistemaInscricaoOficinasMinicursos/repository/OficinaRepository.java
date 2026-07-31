package projetofinal.SistemaInscricaoOficinasMinicursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {
}
