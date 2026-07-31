package projetofinal.SistemaInscricaoOficinasMinicursos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.OficinaRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.OficinaResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.service.OficinaService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaController {
    private final OficinaService service;

    public OficinaController(OficinaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OficinaResponseDTO>> listar(){
        List<OficinaResponseDTO> lista = service.lista();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponseDTO> buscarPorId(@PathVariable Long id) {
        OficinaResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OficinaResponseDTO> cadastrar(@RequestBody OficinaRequestDTO dto) {
        OficinaResponseDTO novaOficina = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaOficina);
    }

    // ADICIONADO: Endpoint obrigatório para atualização completa da Oficina
    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponseDTO> atualizar(@PathVariable Long id, @RequestBody OficinaRequestDTO dto) {
        OficinaResponseDTO atualizada = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    // ADICIONADO: Endpoint de alteração de status unificado conforme contrato técnico mínimo
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        service.alterarStatus(id, body.get("ativo"));
        return ResponseEntity.ok().build();
    }

    // ADICIONADO: Endpoint de vinculação e atualização de instrutores em lote via JSON
    @PatchMapping("/{id}/instrutores")
    public ResponseEntity<Void> atualizarInstrutores(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        List<Long> instrutoresIds = body.get("instrutoresIds");
        service.atualizarInstrutores(id, instrutoresIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}