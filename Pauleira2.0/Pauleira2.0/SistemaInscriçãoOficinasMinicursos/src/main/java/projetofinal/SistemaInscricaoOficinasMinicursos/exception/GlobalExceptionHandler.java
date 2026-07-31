package projetofinal.SistemaInscricaoOficinasMinicursos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(RegraNegocioException ex) {
        String erroTitulo = "Regra de negócio violada";
        String msg = ex.getMessage().toLowerCase();

        // REANÁLISE: Mapeamento cirúrgico baseado exatamente na lista de "Erros esperados" do PDF
        if (msg.contains("já está inscrito") || msg.contains("duplicada")) {
            erroTitulo = "Inscrição duplicada";
        } else if (msg.contains("vagas") || msg.contains("esgotadas")) {
            erroTitulo = "Vagas esgotadas";
        } else if (msg.contains("oficina") && msg.contains("inativa")) {
            erroTitulo = "Oficina inativa";
        } else if (msg.contains("instrutor") && msg.contains("inativo")) {
            erroTitulo = "Instrutor inativo";
        } else if (msg.contains("pelo menos um instrutor") || msg.contains("sem instrutor")) {
            erroTitulo = "Oficina sem instrutor";
        } else if (msg.contains("já se encontra cancelada") || msg.contains("cancelar inscrição")) {
            erroTitulo = "Tentativa de cancelar inscrição já cancelada";
        } else if (msg.contains("excluir oficina") || msg.contains("confirmadas")) {
            erroTitulo = "Tentativa de excluir oficina com inscrições confirmadas";
        }

        // Retorna HTTP 409 Conflict conforme o exemplo oficial do Desafio
        ErroResponse erro = new ErroResponse(
                HttpStatus.CONFLICT.value(),
                erroTitulo,
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroGenerico(Exception ex) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado no sistema"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }
}