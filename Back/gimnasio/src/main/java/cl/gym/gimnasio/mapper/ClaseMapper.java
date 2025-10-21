package cl.gym.gimnasio.mapper;

import cl.gym.gimnasio.dto.ClaseDTO;
import cl.gym.gimnasio.dto.request.CreateClaseRequest;
import cl.gym.gimnasio.dto.response.CreateClaseResponse;
import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.model.Usuario;

public class ClaseMapper {

    public static ClaseDTO modelToDTO(Clase clase){
        return ClaseDTO.builder()
                .nombre(clase.getNombre())
                .descripcion(clase.getDescripcion())
                .fechaClase(clase.getFechaClase())
                .horaInicio(clase.getHoraInicio())
                .horaFin(clase.getHoraFin())
                .cupoMaximo(clase.getCupoMaximo())
                .cupoDisponible(clase.getCupoDisponible())
                // Relaciones
                .idCoach(clase.getCoach() != null ? clase.getCoach().getIdUsuario() : null)
                .build();
    }

    public static Clase dtoToModel(ClaseDTO claseDTO){
        return Clase.builder()
                .nombre(claseDTO.getNombre())
                .descripcion(claseDTO.getDescripcion())
                .fechaClase(claseDTO.getFechaClase())
                .horaInicio(claseDTO.getHoraInicio())
                .horaFin(claseDTO.getHoraFin())
                .cupoMaximo(claseDTO.getCupoMaximo())
                .cupoDisponible(claseDTO.getCupoDisponible())
                 // Para las relaciones, solo seteamos el ID, los demás campos deben buscarse y asignarse aparte en el servicio
                .coach(claseDTO.getIdCoach() != null ? Usuario.builder().idUsuario(claseDTO.getIdCoach()).build() : null)
                .build();
    }

    public static Clase createRequestToModel(CreateClaseRequest createClaseRequest){
        return Clase.builder()
                .nombre(createClaseRequest.getNombre())
                .descripcion(createClaseRequest.getDescripcion())
                .fechaClase(createClaseRequest.getFechaClase())
                .horaInicio(createClaseRequest.getHoraInicio())
                .horaFin(createClaseRequest.getHoraFin())
                .cupoMaximo(createClaseRequest.getCupoMaximo())
                .cupoDisponible(createClaseRequest.getCupoDisponible())
                .build();
    }

    public static CreateClaseResponse modelToResponse(Clase clase){
        return CreateClaseResponse.builder()
                .nombre(clase.getNombre())
                .descripcion(clase.getDescripcion())
                .fechaClase(clase.getFechaClase())
                .horaInicio(clase.getHoraInicio())
                .horaFin(clase.getHoraFin())
                .cupoMaximo(clase.getCupoMaximo())
                .cupoDisponible(clase.getCupoDisponible())
                // Relaciones
                .nombre(
                        clase.getCoach() == null ? null :
                                clase.getCoach().getNombre() + " " + clase.getCoach().getRol())
                .build();
    }
}
