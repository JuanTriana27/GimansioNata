package cl.gym.gimnasio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva", nullable = false, length =30)
    private Integer idReserva;

    @Column(name = "fecha_reserva", nullable = false)
    private Date fechaReserva;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    // LLaves foraneas
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "id_usuario", nullable = false)
    private Usuario socio;

    @ManyToOne
    @JoinColumn(name = "id_clase", referencedColumnName = "id_clase", nullable = false)
    private Clase clase;
}