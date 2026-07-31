package projetofinal.SistemaInscricaoOficinasMinicursos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tb_instrutor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "area_atuacao", nullable = false, length = 100)
    private String areaAtuacao;

    @Column(nullable = false)
    private Boolean ativo = true;

    //RELACIONAMENTO
    @JsonIgnore
    @ManyToMany(mappedBy = "instrutores")
    private List<Oficina> oficinas;


}