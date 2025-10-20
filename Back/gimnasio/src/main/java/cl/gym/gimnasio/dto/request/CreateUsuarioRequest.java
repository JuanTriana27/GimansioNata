package cl.gym.gimnasio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioRequest {
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String rol;
    private Date fechaRegistro;
}
