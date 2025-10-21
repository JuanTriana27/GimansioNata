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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, length =30)
    private Integer idUsuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column (name = "email", length = 30)
    private String email;

    @Column(name = "password", length = 250)
    private String password;

    @Column (name = "telefono", nullable = false, length =30)
    private String telefono;

    @Column(name = "rol", nullable = false, length = 30)
    private String rol;

    @Column(name = "fecha_registro", nullable = false)
    private Date fechaRegistro = new Date();
}
