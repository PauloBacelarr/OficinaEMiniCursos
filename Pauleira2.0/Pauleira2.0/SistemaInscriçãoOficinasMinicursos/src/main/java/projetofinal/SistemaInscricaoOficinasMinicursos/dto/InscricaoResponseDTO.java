package projetofinal.SistemaInscricaoOficinasMinicursos.dto;

import projetofinal.SistemaInscricaoOficinasMinicursos.entity.StatusInscricao;

import java.time.LocalDateTime;

public record InscricaoResponseDTO(
        Long id,
        Long oficinaId,
        String oficinaTitulo,
        Long alunoId,
        String alunoNome,
        LocalDateTime dataInscricao,
        StatusInscricao status
) {
}
