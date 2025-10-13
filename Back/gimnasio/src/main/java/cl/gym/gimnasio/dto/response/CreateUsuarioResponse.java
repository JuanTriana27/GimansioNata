package cl.gym.gimnasio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioResponse {
    private Integer idUsuario;
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String rol;
    private Date fechaRegistro;
}
