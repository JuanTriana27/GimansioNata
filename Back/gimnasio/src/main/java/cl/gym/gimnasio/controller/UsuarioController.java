package cl.gym.gimnasio.controller;

import cl.gym.gimnasio.dto.UsuarioDTO;
import cl.gym.gimnasio.dto.request.CreateUsuarioRequest;
import cl.gym.gimnasio.dto.response.CreateUsuarioResponse;
import cl.gym.gimnasio.dto.response.MessageResponse;
import cl.gym.gimnasio.model.Usuario;
import cl.gym.gimnasio.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {
    // Inyeccion de dependencias del servicio en el controlador
    private final UsuarioService usuarioService;

    // Listar Todos los Usuarios
    @GetMapping("/all")
    public List<Usuario> getAllUsuarios(){
        return usuarioService.getAllUsuarios();
    }

    // Obtener por ID
    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Integer id){

        UsuarioDTO usuarioDTO = usuarioService.getUsuarioPorId(id);

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    // Guardar nuevo usuario
    @PostMapping("/save-new")
    public ResponseEntity<CreateUsuarioResponse> guardarNuevo(@RequestBody CreateUsuarioRequest createUsuarioRequest) throws Exception{
        CreateUsuarioResponse createUsuarioResponse = usuarioService.createUsuario(createUsuarioRequest);
        return new ResponseEntity<>(createUsuarioResponse, HttpStatus.CREATED);
    }

    // Actualizar por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CreateUsuarioResponse> usuario(@PathVariable Integer id, @RequestBody CreateUsuarioRequest createUsuarioRequest) throws Exception{
        try {
            CreateUsuarioResponse createUsuarioResponse = usuarioService.updateUsuario(id, createUsuarioRequest);
            return new ResponseEntity<>(createUsuarioResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteUsuario(@PathVariable Integer id){
        try{
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<>(new MessageResponse("Usuario eliminado exitosamente"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
