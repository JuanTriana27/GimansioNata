package cl.gym.gimnasio.controller;

import cl.gym.gimnasio.dto.ClaseDTO;
import cl.gym.gimnasio.dto.request.CreateClaseRequest;
import cl.gym.gimnasio.dto.response.CreateClaseResponse;
import cl.gym.gimnasio.dto.response.MessageResponse;
import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.service.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clase")
@RequiredArgsConstructor
public class ClaseController {
    // Inyeccion de dependencias del servicio en el controlador
    private final ClaseService claseService;

    // Listar Todas las Clases
    @GetMapping("/all")
    public List<Clase> getAllClases(){
        return claseService.getAllClases();
    }

    // Obtener por ID
    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<ClaseDTO> buscarClasePorId(@PathVariable Integer id){
        ClaseDTO claseDTO = claseService.getClasePorId(id);
        return new ResponseEntity<>(claseDTO, HttpStatus.OK);
    }

    // Guardar nueva clase
    @PostMapping("/save-new")
    public ResponseEntity<CreateClaseResponse> guardarNueva(@RequestBody CreateClaseRequest createClaseRequest) throws Exception{
        CreateClaseResponse createClaseResponse = claseService.createClase(createClaseRequest);
        return new ResponseEntity<>(createClaseResponse, HttpStatus.CREATED);
    }

    // Actualizar por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CreateClaseResponse> actualizarClase(@PathVariable Integer id, @RequestBody CreateClaseRequest createClaseRequest) throws Exception{
        try {
            CreateClaseResponse createClaseResponse = claseService.updateClase(id, createClaseRequest);
            return new ResponseEntity<>(createClaseResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteClase(@PathVariable Integer id){
        try{
            claseService.deleteClase(id);
            return new ResponseEntity<>(new MessageResponse("Clase eliminada exitosamente"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}