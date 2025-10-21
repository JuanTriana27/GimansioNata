package cl.gym.gimnasio.service.impl;

import cl.gym.gimnasio.dto.ClaseDTO;
import cl.gym.gimnasio.dto.request.CreateClaseRequest;
import cl.gym.gimnasio.dto.response.CreateClaseResponse;
import cl.gym.gimnasio.mapper.ClaseMapper;
import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.model.Usuario;
import cl.gym.gimnasio.repository.ClaseRepository;
import cl.gym.gimnasio.repository.UsuarioRepository;
import cl.gym.gimnasio.service.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaseServiceImpl implements ClaseService {

    private final ClaseRepository claseRepository;
    private final UsuarioRepository usuarioRepository;

    // Metodo para listar clases
    @Override
    public List<Clase> getAllClases(){
        return  claseRepository.findAll();
    }

    // Metodo para buscar clase por id
    @Override
    public ClaseDTO getClasePorId(Integer id){

        // Consultar en db las clases por id
        Clase clase = claseRepository.getReferenceById(id);

        // Mapear hacia DTO el resultado que trae el modelo
        ClaseDTO claseDTO = ClaseMapper.modelToDTO(clase);

        // Retornar el objeto mapeado a dto
        return claseDTO;
    }

    // Metodo para crear clase
    @Override
    public CreateClaseResponse createClase(CreateClaseRequest createClaseRequest) throws Exception{

        // Validar que la clase exista
        if(createClaseRequest == null){
            throw new Exception("La Clase no puede ser nula");
        }

        // Validar que el nombre no sea nulo
        if(createClaseRequest.getNombre() == null){
            throw new Exception("El nombre no puede ser nulo");
        }

        // Validar que la descripcion no sea nula
        if(createClaseRequest.getDescripcion() == null){
            throw new Exception("La descripcion no puede ser nula");
        }

        // Validar fecha clase
        if(createClaseRequest.getFechaClase()== null){
            throw new Exception("La fecha no puede ser nula");
        }

        // Validar Hora inicio
        if(createClaseRequest.getHoraInicio() == null){
            throw new Exception("La hora ingresada no puede ser nula");
        }

        // Validar Hora Fin
        if(createClaseRequest.getHoraFin() == null){
            throw new Exception("La hora ingresada no puede ser nula");
        }

        // Validar cupo maximo
        if(createClaseRequest.getCupoMaximo() == null){
            throw new Exception("La cupo maximo no puede ser nula");
        }

        // Validar cupo disponible
        if(createClaseRequest.getCupoDisponible() == null){
            throw new Exception("La cupo disponible no puede ser nula");
        }

        // Validar IDs de entidades relacionadas
        if (createClaseRequest.getIdCoach() == null || createClaseRequest.getIdCoach() <= 0){
            throw  new Exception("El Coach no puede ser nula");
        }

        // Obtener entidad relacionada
        Usuario usuario = usuarioRepository.findById(createClaseRequest.getIdCoach())
                .orElseThrow(() -> new Exception("Coach no encontrado con ID: " +  createClaseRequest.getIdCoach()));

        // Mapear request a modelo y establecer relaciones
        Clase clase = ClaseMapper.createRequestToModel(createClaseRequest);
        clase.setCoach(usuario);

        // Guardar en la db
        clase = claseRepository.save(clase);

        // convertir a Response y retornar
        return ClaseMapper.modelToResponse(clase);
    }

    // Metodo para actualizar clase
    @Override
    public CreateClaseResponse updateClase(Integer id, CreateClaseRequest createClaseRequest) throws Exception {

        // Verificamos que exista
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new Exception("Clase no Encontrada"));
        
        // Validar que la clase exista
        if(createClaseRequest == null){
            throw new Exception("La Clase no puede ser nula");
        }

        // Validar que el nombre no sea nulo
        if(createClaseRequest.getNombre() == null){
            throw new Exception("El nombre no puede ser nulo");
        }

        // Validar que la descripcion no sea nula
        if(createClaseRequest.getDescripcion() == null){
            throw new Exception("La descripcion no puede ser nula");
        }

        // Validar fecha clase
        if(createClaseRequest.getFechaClase()== null){
            throw new Exception("La fecha no puede ser nula");
        }

        // Validar Hora inicio
        if(createClaseRequest.getHoraInicio() == null){
            throw new Exception("La hora ingresada no puede ser nula");
        }

        // Validar Hora Fin
        if(createClaseRequest.getHoraFin() == null){
            throw new Exception("La hora ingresada no puede ser nula");
        }

        // Validar cupo maximo
        if(createClaseRequest.getCupoMaximo() == null){
            throw new Exception("La cupo maximo no puede ser nula");
        }

        // Validar cupo disponible
        if(createClaseRequest.getCupoDisponible() == null){
            throw new Exception("La cupo disponible no puede ser nula");
        }

        // Validar IDs de entidades relacionadas
        if (createClaseRequest.getIdCoach() == null || createClaseRequest.getIdCoach() <= 0){
            throw  new Exception("El Coach no puede ser nulo");
        }

        // Obtener entidad relacionada
        Usuario usuario = usuarioRepository.findById(createClaseRequest.getIdCoach())
                .orElseThrow(() -> new Exception("Coach no encontrado con ID: " +  createClaseRequest.getIdCoach()));

        // Actualizar los campos de la entidad existente
        clase.setNombre(createClaseRequest.getNombre());
        clase.setDescripcion(createClaseRequest.getDescripcion());
        clase.setFechaClase(createClaseRequest.getFechaClase());
        clase.setHoraInicio(createClaseRequest.getHoraInicio());
        clase.setHoraFin(createClaseRequest.getHoraFin());
        clase.setCupoMaximo(createClaseRequest.getCupoMaximo());
        clase.setCupoDisponible(createClaseRequest.getCupoDisponible());

        // Entidad relacionada
        clase.setCoach(usuario);

        // Guardar cambios en db
        Clase claseUpdate = claseRepository.save(clase);

        // Mapear a CreateClaseResponse
        return ClaseMapper.modelToResponse(claseUpdate);

    }

    // Metodo para eliminar clase
    @Override
    public void deleteClase(Integer id) throws Exception{

        // Verificamos que exista la clase
        if(!claseRepository.existsById(id)){
            throw new Exception("La Clase no existe");
        }

        // Eliminamos
        claseRepository.deleteById(id);
    }
}
