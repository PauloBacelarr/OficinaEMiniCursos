package projetofinal.SistemaInscricaoOficinasMinicursos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_oficina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "quantidade_vagas", nullable = false)
    private Integer quantidadeDeVagas;

    @Column(nullable = false)
    private Boolean ativo = true;

    //RELACIONAMENTO
    @ManyToMany
    @JoinTable(
            name = "oficina_instrutor",
            joinColumns = @JoinColumn(name = "oficina_id"),
            inverseJoinColumns = @JoinColumn(name = "instrutor_id")
    )
    private List<Instrutor> instrutores = new ArrayList<>();
}