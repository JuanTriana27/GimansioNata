package cl.gym.gimnasio.mapper;

import cl.gym.gimnasio.dto.UsuarioDTO;
import cl.gym.gimnasio.dto.request.CreateUsuarioRequest;
import cl.gym.gimnasio.dto.response.CreateUsuarioResponse;
import cl.gym.gimnasio.model.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO modelToDTO(Usuario usuario) {
        return  UsuarioDTO.builder()
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .telefono(usuario.getPassword())
                .rol(usuario.getRol())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }

    public static Usuario dtoToModel(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nombre(usuarioDTO.getNombre())
                .email(usuarioDTO.getEmail())
                .password(usuarioDTO.getPassword())
                .telefono(usuarioDTO.getTelefono())
                .rol(usuarioDTO.getRol())
                .fechaRegistro(usuarioDTO.getFechaRegistro())
                .build();
    }

    public static Usuario createRequestToModel(CreateUsuarioRequest createUsuarioRequest) {
        return Usuario.builder()
                .nombre(createUsuarioRequest.getNombre())
                .email(createUsuarioRequest.getEmail())
                .password(createUsuarioRequest.getPassword())
                .telefono(createUsuarioRequest.getTelefono())
                .rol(createUsuarioRequest.getRol())
                .fechaRegistro(createUsuarioRequest.getFechaRegistro())
                .build();
    }

    public static CreateUsuarioResponse modelToCreateResponse (Usuario usuario){
        return CreateUsuarioResponse.builder()
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
