package projetofinal.SistemaInscricaoOficinasMinicursos.dto;

public record InstrutorResponseDTO(
        Long id,
        String nome,
        String email,
        String areaAtuacao,
        Boolean ativo
) {}
