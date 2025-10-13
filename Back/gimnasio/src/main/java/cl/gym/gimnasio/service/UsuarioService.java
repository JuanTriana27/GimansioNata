package cl.gym.gimnasio.service;

import cl.gym.gimnasio.dto.UsuarioDTO;
import cl.gym.gimnasio.dto.request.CreateUsuarioRequest;
import cl.gym.gimnasio.dto.response.CreateUsuarioResponse;
import cl.gym.gimnasio.model.Usuario;

import java.util.List;

public interface UsuarioService {
    // Metodo para listar Usuarios
    List<Usuario> getAllUsuarios();

    // Consultar Usuario Por ID
    UsuarioDTO getUsuarioPorId(Integer id);

    // Metodo para crear Usuario
    CreateUsuarioResponse createUsuario(CreateUsuarioRequest createUsuarioRequest) throws Exception;

    // Metodo para actulizar Usuario
    CreateUsuarioResponse updateUsuario(Integer id, CreateUsuarioRequest createUsuarioRequest) throws Exception;

    // Metodo para eliminar Usuario
    void deleteUsuario(Integer id) throws Exception;
}
