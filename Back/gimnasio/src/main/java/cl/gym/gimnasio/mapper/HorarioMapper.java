package cl.gym.gimnasio.mapper;

import cl.gym.gimnasio.dto.HorarioDTO;
import cl.gym.gimnasio.dto.request.CreateHorarioRequest;
import cl.gym.gimnasio.dto.response.CreateHorarioResponse;
import cl.gym.gimnasio.model.Horario;
import cl.gym.gimnasio.model.Usuario;

public class HorarioMapper {

    public static HorarioDTO modelToDTO(Horario horario){
        return HorarioDTO.builder()
                .diaSemana(horario.getDiaSemana())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                // Relacion: aquí devuelvo el objeto Usuario reducido (igual que hacías)
                .coach(horario.getCoach() != null ? horario.getCoach().getIdUsuario() : null)
                .build();
    }

    public static Horario dtoToModel(HorarioDTO horarioDTO){
        return Horario.builder()
                .diaSemana(horarioDTO.getDiaSemana())
                .horaInicio(horarioDTO.getHoraInicio())
                .horaFin(horarioDTO.getHoraFin())
                // Relacion: solo seteamos ID para persistencia
                .coach(horarioDTO.getCoach() != null ? Usuario.builder().idUsuario(horarioDTO.getCoach()).build() : null)
                .build();
    }

    public static Horario createRequestToModel(CreateHorarioRequest createHorarioRequest){
        return Horario.builder()
                .diaSemana(createHorarioRequest.getDiaSemana())
                .horaInicio(createHorarioRequest.getHoraInicio())
                .horaFin(createHorarioRequest.getHoraFin())
                .build();
    }

    public static CreateHorarioResponse modelToResponse(Horario horario){
        return CreateHorarioResponse.builder()
                .diaSemana(horario.getDiaSemana())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                // si CreateHorarioResponse tiene un campo Usuario coach:
                .coach(
                        horario.getCoach() != null ? null :
                                horario.getCoach().getIdUsuario()
                )
                .build();
    }
}
