package cl.gym.gimnasio.controller;

import cl.gym.gimnasio.dto.HorarioDTO;
import cl.gym.gimnasio.dto.request.CreateHorarioRequest;
import cl.gym.gimnasio.dto.response.CreateHorarioResponse;
import cl.gym.gimnasio.dto.response.MessageResponse;
import cl.gym.gimnasio.model.Horario;
import cl.gym.gimnasio.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("horario")
@RequiredArgsConstructor
public class HorarioController {
    // Inyeccion de dependencias del servicio en el controlador
    private final HorarioService horarioService;

    // Listar Todos los Horarios
    @GetMapping("/all")
    public List<Horario> getAllHorarios(){
        return horarioService.getAllHorarios();
    }

    // Obtener por ID
    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<HorarioDTO> buscarHorarioPorId(@PathVariable Integer id){
        HorarioDTO horarioDTO = horarioService.getHorarioPorId(id);
        return new ResponseEntity<>(horarioDTO, HttpStatus.OK);
    }

    // Guardar nuevo horario
    @PostMapping("/save-new")
    public ResponseEntity<CreateHorarioResponse> guardarNuevo(@RequestBody CreateHorarioRequest createHorarioRequest) throws Exception{
        CreateHorarioResponse createHorarioResponse = horarioService.createHorario(createHorarioRequest);
        return new ResponseEntity<>(createHorarioResponse, HttpStatus.CREATED);
    }

    // Actualizar por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CreateHorarioResponse> actualizarHorario(@PathVariable Integer id, @RequestBody CreateHorarioRequest createHorarioRequest) throws Exception{
        try {
            CreateHorarioResponse createHorarioResponse = horarioService.updateHorario(id, createHorarioRequest);
            return new ResponseEntity<>(createHorarioResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteHorario(@PathVariable Integer id){
        try{
            horarioService.deleteHorario(id);
            return new ResponseEntity<>(new MessageResponse("Horario eliminado exitosamente"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}