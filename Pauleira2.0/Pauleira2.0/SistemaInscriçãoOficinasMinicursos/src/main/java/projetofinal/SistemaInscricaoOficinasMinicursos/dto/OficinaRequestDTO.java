package projetofinal.SistemaInscricaoOficinasMinicursos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OficinaRequestDTO(
        String titulo,
        String descricao,
        LocalDateTime data, // Modificado para LocalDateTime para alinhar com a Entity
        Integer cargaHoraria,
        Integer quantidadeDeVagas,
        List<Long> instrutoresIds // IDs dos instrutores que serão vinculados
) {}
