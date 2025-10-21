package cl.gym.gimnasio.service.impl;

import cl.gym.gimnasio.dto.HorarioDTO;
import cl.gym.gimnasio.dto.request.CreateHorarioRequest;
import cl.gym.gimnasio.dto.response.CreateHorarioResponse;
import cl.gym.gimnasio.mapper.HorarioMapper;
import cl.gym.gimnasio.model.Horario;
import cl.gym.gimnasio.model.Usuario;
import cl.gym.gimnasio.repository.HorarioRepository;
import cl.gym.gimnasio.repository.UsuarioRepository;
import cl.gym.gimnasio.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;

    // Metodo para listar los horarios
    @Override
    public List<Horario> getAllHorarios(){
        return horarioRepository.findAll();
    }

    // Metodo para buscar horario por id
    @Override
    public HorarioDTO  getHorarioPorId(Integer id) {

        // Consultar en db el horario por id
        Horario horario = horarioRepository.getReferenceById(id);

        // Mapear hacia DTO el resultado que trae el modelo
        HorarioDTO horarioDTO = HorarioMapper.modelToDTO(horario);

        // Retornar el objeto mapeado a dto
        return horarioDTO;
    }

    // Metodo para crear horario
    @Override
    public CreateHorarioResponse createHorario(CreateHorarioRequest createHorarioRequest) throws  Exception {

        // Validar que el horario exista
        if(createHorarioRequest == null){
            throw new Exception("El horario no puede ser nulo");
        }

        // Validar que el dia de la semana no sea nulo
        if(createHorarioRequest.getDiaSemana() == null){
            throw new Exception("El dia de semana no puede ser nulo");
        }

        // Validar hora inicio
        if(createHorarioRequest.getHoraInicio() == null){
            throw new Exception("La hora de inicio no puede ser nula");
        }

        // Validar que la hora fin no sea nula
        if(createHorarioRequest.getHoraFin() == null){
            throw new Exception("La hora de fin no puede ser nula");
        }

        // Validar IDs de entidades relacionadas
        if(createHorarioRequest.getCoach() == null || createHorarioRequest.getCoach() <= 0){
            throw new Exception("El coach no puede ser nulo");
        }

        // Obtener entidad relacionada
        Usuario usuario = usuarioRepository.findById(createHorarioRequest.getCoach())
                .orElseThrow(() -> new Exception("Coach no encontrado con ID: " + createHorarioRequest.getCoach()));

        // Mapear request a modelo y establecer relaciones
        Horario horario = HorarioMapper.createRequestToModel(createHorarioRequest);

        // Establecer relacion
        horario.setCoach(usuario);

        // Guardar en la db
        horario = horarioRepository.save(horario);

        // Convertir a response y retornar
        return HorarioMapper.modelToResponse(horario);
    }

    // Metodo para actualizar el horario
    @Override
    public CreateHorarioResponse updateHorario(Integer id, CreateHorarioRequest createHorarioRequest) throws Exception {

        // Verificamos que exista
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Horario no encontrado"));

        // Verificar que el horario no sea nulo
        if(createHorarioRequest == null){
            throw new Exception("El horario no puede ser nulo");
        }

        // Verificar que dia de semana no sea nulo
        if(createHorarioRequest.getDiaSemana() == null){
            throw new Exception("El día de semana no puede ser nulo"); // Corregido "La" por "El"
        }

        // Verificar que hora inicio no sea nulo
        if(createHorarioRequest.getHoraInicio() == null){
            throw new Exception("La hora de inicio no puede ser nula"); // Corregido "nulo" por "nula"
        }

        // Verificar que hora fin no sea nulo
        if(createHorarioRequest.getHoraFin() == null){
            throw new Exception("La hora de fin no puede ser nula"); // Corregido "nulo" por "nula"
        }

        // Validar Ids de entidad relacionada
        if (createHorarioRequest.getCoach() == null || createHorarioRequest.getCoach() <= 0)
            throw new Exception("El coach no puede ser nulo");

        // Obtener entidad relacionada
        Usuario usuario = usuarioRepository.findById(createHorarioRequest.getCoach())
                .orElseThrow(() -> new Exception("Coach no encontrado con ID: " + createHorarioRequest.getCoach()));

        // Actualizar campos de la entidad existente
        horario.setDiaSemana(createHorarioRequest.getDiaSemana());
        horario.setHoraInicio(createHorarioRequest.getHoraInicio());
        horario.setHoraFin(createHorarioRequest.getHoraFin());

        // Entidad relacionada
        horario.setCoach(usuario);

        // Guardar cambios en db
        Horario horarioUpdate = horarioRepository.save(horario);

        // Mapear a CreateHorarioResponse
        return HorarioMapper.modelToResponse(horarioUpdate);
    }

    // Metodo para eliminar horario
    @Override
    public void deleteHorario(Integer id) throws Exception {

        // Verificamos que exista el horario
        if(!horarioRepository.existsById(id)){
            throw new Exception("El horario no existe");
        }

        // Eliminamos
        horarioRepository.deleteById(id);
    }
}
