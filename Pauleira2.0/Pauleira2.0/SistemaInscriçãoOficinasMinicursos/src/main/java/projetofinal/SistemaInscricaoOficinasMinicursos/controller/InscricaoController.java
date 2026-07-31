package projetofinal.SistemaInscricaoOficinasMinicursos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InscricaoRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InscricaoResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.service.InscricaoService;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService service;

    public InscricaoController(InscricaoService service) {
        this.service = service;
    }

    // GET /api/inscricoes
    @GetMapping
    public ResponseEntity<List<InscricaoResponseDTO>> listar() {
        List<InscricaoResponseDTO> lista = service.listar();
        return ResponseEntity.ok(lista);
    }

    // GET /api/inscricoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<InscricaoResponseDTO> buscarPorId(@PathVariable Long id) {
        InscricaoResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // POST /api/inscricoes
    @PostMapping
    public ResponseEntity<InscricaoResponseDTO> inscrever(@RequestBody InscricaoRequestDTO dto) {
        InscricaoResponseDTO resposta = service.realizarInscricao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    // PATCH /api/inscricoes/{id}/cancelar
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<InscricaoResponseDTO> cancelar(@PathVariable Long id) {
        InscricaoResponseDTO atualizada = service.cancelar(id);
        return ResponseEntity.ok(atualizada);
    }
}