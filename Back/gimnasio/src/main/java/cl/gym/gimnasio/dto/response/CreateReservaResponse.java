package cl.gym.gimnasio.dto.response;

import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservaResponse {
    private Integer idReserva;
    private Date fechaReserva;
    private String estado;

    // Llaves foraneas
    private Integer socio;
    private Integer clase;
}
