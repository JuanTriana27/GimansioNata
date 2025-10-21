package cl.gym.gimnasio.service;

import cl.gym.gimnasio.dto.ReservaDTO;
import cl.gym.gimnasio.dto.request.CreateReservaRequest;
import cl.gym.gimnasio.dto.response.CreateReservaResponse;
import cl.gym.gimnasio.model.Reserva;

import java.util.List;

public interface ReservaService {

    // Metodo para listar Reservas
    List<Reserva> getAllReservas();

    // Metodo para consultar reserva por ID
    ReservaDTO getReservaPorId(Integer id);

    // Metodo para crear Reserva
    CreateReservaResponse createReserva(CreateReservaRequest createReservaRequest) throws Exception;

    // Metodo para actualizar Reserva
    CreateReservaResponse updateReserva(Integer id, CreateReservaRequest createReservaRequest) throws Exception;

    // Metodo para eliminar reserva
    void deleteReserva(Integer id) throws Exception;
}
