package cl.gym.gimnasio.service.impl;

import cl.gym.gimnasio.dto.UsuarioDTO;
import cl.gym.gimnasio.dto.request.CreateUsuarioRequest;
import cl.gym.gimnasio.dto.response.CreateUsuarioResponse;
import cl.gym.gimnasio.mapper.UsuarioMapper;
import cl.gym.gimnasio.model.Usuario;
import cl.gym.gimnasio.repository.UsuarioRepository;
import cl.gym.gimnasio.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // ← INYECTAR PasswordEncoder

    // Metodo para listar usuarios
    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Metodo para obtener usuarios por su ID
    @Override
    public UsuarioDTO getUsuarioPorId(Integer id) {
        // Consultar en la db las personas por id
        Usuario usuario = usuarioRepository.getReferenceById(id);

        // Mapear hacia DTO el resultado que trae el modelo
        UsuarioDTO usuarioDTO = UsuarioMapper.modelToDTO(usuario);

        // Retornar el objeto mapeado a DTO
        return usuarioDTO;
    }

    // Metodo para crear usuario
    @Override
    public CreateUsuarioResponse createUsuario(CreateUsuarioRequest createUsuarioRequest) throws Exception {

        // Validar que el estado de Usuario no sea nulo
        if (createUsuarioRequest == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }

        // Validar que el nombre no sea nulo
        if (createUsuarioRequest.getNombre() == null ||
                createUsuarioRequest.getNombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo.");
        }

        // Validar que el email no sea nulo
        if (createUsuarioRequest.getEmail() == null ||
                createUsuarioRequest.getEmail().isBlank()) {
            throw new Exception("El email no puede ser nulo.");
        }

        // Validar que la contraseña no sea nula
        if (createUsuarioRequest.getPassword() == null ||
                createUsuarioRequest.getPassword().isBlank()) {
            throw new Exception("La password no puede ser nula.");
        }

        // Validar que el telefono no sea nulo
        if (createUsuarioRequest.getTelefono() == null ||
                createUsuarioRequest.getTelefono().isBlank()) {
            throw new Exception("El telefono no puede ser nulo.");
        }

        // Validar que el rol no sea nulo
        if (createUsuarioRequest.getRol() == null ||
                createUsuarioRequest.getRol().isBlank()) {
            throw new Exception("El rol no puede ser nulo.");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(createUsuarioRequest.getEmail())) {
            throw new Exception("El email ya está registrado.");
        }

        // Convertir de Request a Model
        Usuario usuario = UsuarioMapper.createRequestToModel(createUsuarioRequest);

        // HASHEAR LA CONTRASEÑA ANTES DE GUARDAR
        String hashedPassword = passwordEncoder.encode(createUsuarioRequest.getPassword());
        usuario.setPassword(hashedPassword);

        // Asignar fecha automática
        usuario.setFechaRegistro(new Date());

        // Persistir el modelo en db
        usuario = usuarioRepository.save(usuario);

        // Convertir a response para retornar
        CreateUsuarioResponse createUsuarioResponse = UsuarioMapper.modelToCreateResponse(usuario);

        // Retornar el Response persistido como lo solicita el método
        return createUsuarioResponse;
    }

    // Metodo para actualizar usuario
    @Override
    public CreateUsuarioResponse updateUsuario(Integer id, CreateUsuarioRequest createUsuarioRequest) throws Exception {

        // Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario no fue encontrado."));

        // Validar que el nombre no sea nulo
        if (createUsuarioRequest.getNombre() == null ||
                createUsuarioRequest.getNombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo.");
        }

        // Validar que el email no sea nulo
        if (createUsuarioRequest.getEmail() == null ||
                createUsuarioRequest.getEmail().isBlank()) {
            throw new Exception("El email no puede ser nulo.");
        }

        // Validar que el telefono no sea nulo
        if (createUsuarioRequest.getTelefono() == null ||
                createUsuarioRequest.getTelefono().isBlank()) {
            throw new Exception("El telefono no puede ser nulo.");
        }

        // Validar que el rol no sea nulo
        if (createUsuarioRequest.getRol() == null ||
                createUsuarioRequest.getRol().isBlank()) {
            throw new Exception("El rol no puede ser nulo.");
        }

        // Verificar si el email ya existe en otro usuario
        if (!usuario.getEmail().equals(createUsuarioRequest.getEmail()) &&
                usuarioRepository.existsByEmail(createUsuarioRequest.getEmail())) {
            throw new Exception("El email ya está registrado en otro usuario.");
        }

        // Actualizar los datos del usuario con los nuevos valores
        usuario.setNombre(createUsuarioRequest.getNombre());
        usuario.setEmail(createUsuarioRequest.getEmail());
        usuario.setTelefono(createUsuarioRequest.getTelefono());
        usuario.setRol(createUsuarioRequest.getRol());

        // ACTUALIZAR CONTRASEÑA SOLO SI SE PROPORCIONA UNA NUEVA
        if (createUsuarioRequest.getPassword() != null &&
                !createUsuarioRequest.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(createUsuarioRequest.getPassword());
            usuario.setPassword(hashedPassword);
        }

        // Guardar Usuario actualizado
        usuario = usuarioRepository.save(usuario);

        // Mapear y retornar el response
        return UsuarioMapper.modelToCreateResponse(usuario);
    }

    // Metodo para eliminar Usuario
    @Override
    public void deleteUsuario(Integer id) throws Exception {

        // Verificamos que exista el usuario
        if (!usuarioRepository.existsById(id)) {
            throw new Exception("Usuario no encontrado");
        }

        // Eliminamos
        usuarioRepository.deleteById(id);
    }
}