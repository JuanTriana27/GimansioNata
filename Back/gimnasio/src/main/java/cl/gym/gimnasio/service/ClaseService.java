package cl.gym.gimnasio.service;

import cl.gym.gimnasio.dto.ClaseDTO;
import cl.gym.gimnasio.dto.request.CreateClaseRequest;
import cl.gym.gimnasio.dto.response.CreateClaseResponse;
import cl.gym.gimnasio.model.Clase;

import java.util.List;

public interface ClaseService {
    // Metodo para listar clases
    List<Clase> getAllClases();

    // Metodo para consultar clase por id
    ClaseDTO getClasePorId(Integer id);

    // Metodo para crear Clase
    CreateClaseResponse createClase(CreateClaseRequest createClaseRequest) throws Exception;

    // Metodo para actualizar clase
    CreateClaseResponse updateClase(Integer id, CreateClaseRequest createClaseRequest) throws Exception;

    // Metodo para eliminar clase
    void deleteClase(Integer id) throws Exception;
}