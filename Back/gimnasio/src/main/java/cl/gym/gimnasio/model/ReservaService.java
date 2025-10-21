package cl.gym.gimnasio.model;

import cl.gym.gimnasio.dto.ReservaDTO;
import cl.gym.gimnasio.dto.request.CreateReservaRequest;
import cl.gym.gimnasio.dto.response.CreateReservaResponse;

import java.util.List;

public interface ReservaService {
    // Metodo para listar reservas
    List<Reserva> getAllReservas();

    // Consultar reserva por id
    ReservaDTO getReservaPorId(Integer id);

    // Metodo para crear Reserva
    CreateReservaResponse createReserva(CreateReservaRequest createReservaRequest) throws Exception;

    // Metodo para actualizar reserva
    CreateReservaResponse updateReserva(Integer id, CreateReservaRequest createReservaRequest) throws Exception;

    // Metodo para eliminar reserva
    void deleteReserva(Integer id) throws Exception;
}
