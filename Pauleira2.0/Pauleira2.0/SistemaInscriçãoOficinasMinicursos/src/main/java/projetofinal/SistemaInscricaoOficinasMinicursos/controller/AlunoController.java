package projetofinal.SistemaInscricaoOficinasMinicursos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.AlunoRequestDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.dto.AlunoResponseDTO;
import projetofinal.SistemaInscricaoOficinasMinicursos.service.AlunoService;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;
    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listar() {
        List<AlunoResponseDTO> lista = service.lista();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        AlunoResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrar(@RequestBody AlunoRequestDTO dto) {
        AlunoResponseDTO novoAluno = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {
        AlunoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

