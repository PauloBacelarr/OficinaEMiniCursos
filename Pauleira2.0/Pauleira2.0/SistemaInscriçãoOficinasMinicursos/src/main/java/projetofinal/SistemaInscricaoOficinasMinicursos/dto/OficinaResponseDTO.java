package projetofinal.SistemaInscricaoOficinasMinicursos.dto;


import projetofinal.SistemaInscricaoOficinasMinicursos.entity.Instrutor;
import java.time.LocalDateTime;
import java.util.List;

public record OficinaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime data,
        Integer cargaHoraria,
        Integer quantidadeDeVagas,
        Boolean ativo,
        List<Instrutor> instrutores
) {}