package cl.gym.gimnasio.dto.response;

import cl.gym.gimnasio.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateHorarioResponse {
    private Integer idHorario;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Llave Foranea
    private Usuario coach;
}
