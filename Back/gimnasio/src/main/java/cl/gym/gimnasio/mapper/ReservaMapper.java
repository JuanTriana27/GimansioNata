package cl.gym.gimnasio.mapper;

import cl.gym.gimnasio.dto.ReservaDTO;
import cl.gym.gimnasio.dto.request.CreateReservaRequest;
import cl.gym.gimnasio.dto.response.CreateReservaResponse;
import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.model.Reserva;
import cl.gym.gimnasio.model.Usuario;

public class ReservaMapper {

    public static ReservaDTO modelToDTO(Reserva reserva){
        return ReservaDTO.builder()
                .fechaReserva(reserva.getFechaReserva())
                .estado(reserva.getEstado())
                // Relaciones
                .socio(reserva.getSocio() != null ? reserva.getSocio().getIdUsuario() : null)
                .clase(reserva.getClase() != null ? reserva.getClase().getIdClase() : null)
                .build();
    }

    public static Reserva dtoToModel(ReservaDTO reservaDTO){
        return Reserva.builder()
                .fechaReserva(reservaDTO.getFechaReserva())
                .estado(reservaDTO.getEstado())
                // Relaciones: solo se setean IDs
                .socio(reservaDTO.getSocio() != null ?
                        Usuario.builder().idUsuario(reservaDTO.getSocio()).build() : null)
                .clase(reservaDTO.getClase() != null ?
                        Clase.builder().idClase(reservaDTO.getClase()).build() : null)
                .build();
    }

    public static Reserva createRequestToModel(CreateReservaRequest createReservaRequest){
        return Reserva.builder()
                .fechaReserva(createReservaRequest.getFechaReserva())
                .estado(createReservaRequest.getEstado())
                .build();
    }

    public static CreateReservaResponse modelToResponse(Reserva reserva){
        return CreateReservaResponse.builder()
                .fechaReserva(reserva.getFechaReserva())
                .estado(reserva.getEstado())
                // Relaciones como objetos
                .socio(
                        reserva.getSocio() == null ? null :
                                reserva.getSocio().getIdUsuario()
                )
                .clase(
                        reserva.getClase() == null ? null :
                                reserva.getClase().getIdClase()
                )
                .build();
    }
}
