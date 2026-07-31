package projetofinal.SistemaInscricaoOficinasMinicursos.dto;

public record InstrutorRequestDTO(
        String nome,
        String email,
        String areaAtuacao,
        Boolean ativo
) {
}
