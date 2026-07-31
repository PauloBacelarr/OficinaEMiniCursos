package projetofinal.SistemaInscricaoOficinasMinicursos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InstrutorRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.InstrutorResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.service.InstrutorService;
import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {
    private final InstrutorService service;

    public InstrutorController(InstrutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InstrutorResponseDTO>> listar() {
        List<InstrutorResponseDTO> lista = service.lista();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrutorResponseDTO> buscarPorId(@PathVariable Long id) {
        InstrutorResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<InstrutorResponseDTO> cadastrar(@RequestBody InstrutorRequestDTO dto) {
        InstrutorResponseDTO novoInstrutor = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstrutorResponseDTO> atualizar(@PathVariable Long id, @RequestBody InstrutorRequestDTO dto) {
        InstrutorResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}