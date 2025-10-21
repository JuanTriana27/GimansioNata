package cl.gym.gimnasio.controller;

import cl.gym.gimnasio.dto.ReservaDTO;
import cl.gym.gimnasio.dto.request.CreateReservaRequest;
import cl.gym.gimnasio.dto.response.CreateReservaResponse;
import cl.gym.gimnasio.dto.response.MessageResponse;
import cl.gym.gimnasio.model.Reserva;
import cl.gym.gimnasio.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reserva")
@RequiredArgsConstructor
public class ReservaController {
    // Inyeccion de dependencias del servicio en el controlador
    private final ReservaService reservaService;

    // Listar Todas las Reservas
    @GetMapping("/all")
    public List<Reserva> getAllReservas(){
        return reservaService.getAllReservas();
    }

    // Obtener por ID
    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<ReservaDTO> buscarReservaPorId(@PathVariable Integer id){
        ReservaDTO reservaDTO = reservaService.getReservaPorId(id);
        return new ResponseEntity<>(reservaDTO, HttpStatus.OK);
    }

    // Guardar nueva reserva
    @PostMapping("/save-new")
    public ResponseEntity<CreateReservaResponse> guardarNueva(@RequestBody CreateReservaRequest createReservaRequest) throws Exception{
        CreateReservaResponse createReservaResponse = reservaService.createReserva(createReservaRequest);
        return new ResponseEntity<>(createReservaResponse, HttpStatus.CREATED);
    }

    // Actualizar por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CreateReservaResponse> actualizarReserva(@PathVariable Integer id, @RequestBody CreateReservaRequest createReservaRequest) throws Exception{
        try {
            CreateReservaResponse createReservaResponse = reservaService.updateReserva(id, createReservaRequest);
            return new ResponseEntity<>(createReservaResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteReserva(@PathVariable Integer id){
        try{
            reservaService.deleteReserva(id);
            return new ResponseEntity<>(new MessageResponse("Reserva eliminada exitosamente"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}