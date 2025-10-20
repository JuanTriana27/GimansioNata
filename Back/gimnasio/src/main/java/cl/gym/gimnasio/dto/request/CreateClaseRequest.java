package cl.gym.gimnasio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaseRequest {
    private String nombre;
    private String descripcion;
    private LocalDate fechaClase;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer cupoMaximo;
    private Integer cupoDisponible;

    // Llave Foranea
    private Integer idCoach;
}
