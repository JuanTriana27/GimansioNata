package cl.gym.gimnasio.service;

import cl.gym.gimnasio.dto.HorarioDTO;
import cl.gym.gimnasio.dto.request.CreateHorarioRequest;
import cl.gym.gimnasio.dto.response.CreateHorarioResponse;
import cl.gym.gimnasio.model.Horario;

import java.util.List;

public interface HorarioService {
    // Metodo para listar horarios
    List<Horario> getAllHorarios();

    // Consultar Horarios por ID
    HorarioDTO getHorarioPorId(Integer id);

    // Metodo para crear Horario
    CreateHorarioResponse createHorario(CreateHorarioRequest createHorarioRequest) throws Exception;

    // Metodo para actualizar horario
    CreateHorarioResponse updateHorario(Integer id, CreateHorarioRequest createHorarioRequest) throws Exception;

    // Metodo para eliminar horario
    void deleteHorario(Integer id) throws Exception;
}
