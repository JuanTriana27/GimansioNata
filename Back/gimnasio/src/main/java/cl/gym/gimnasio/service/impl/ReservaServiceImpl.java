package cl.gym.gimnasio.service.impl;

import cl.gym.gimnasio.dto.ReservaDTO;
import cl.gym.gimnasio.dto.request.CreateReservaRequest;
import cl.gym.gimnasio.dto.response.CreateReservaResponse;
import cl.gym.gimnasio.mapper.ReservaMapper;
import cl.gym.gimnasio.model.Clase;
import cl.gym.gimnasio.model.Reserva;
import cl.gym.gimnasio.model.Usuario;
import cl.gym.gimnasio.repository.ClaseRepository;
import cl.gym.gimnasio.repository.ReservaRepository;
import cl.gym.gimnasio.repository.UsuarioRepository;
import cl.gym.gimnasio.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClaseRepository claseRepository;

    // Metodo para listar las reservas
    @Override
    public List<Reserva> getAllReservas(){
        return reservaRepository.findAll();
    }

    // Metodo para buscar reserva por id
    @Override
    public ReservaDTO getReservaPorId(Integer id) {

        // Consultar en db la reserva por id
        Reserva reserva = reservaRepository.getReferenceById(id);

        // Mapear hacia DTO el resultado que trae el modelo
        ReservaDTO reservaDTO = ReservaMapper.modelToDTO(reserva);

        // Retornar el objeto mapeado a dto
        return reservaDTO;
    }

    // Metodo para crear reserva
    @Override
    @Transactional
    public CreateReservaResponse createReserva(CreateReservaRequest createReservaRequest) throws Exception {

        // Validar que la reserva exista
        if(createReservaRequest == null){
            throw new Exception("La reserva no puede ser nula");
        }

        // Validar que la fecha de reserva no sea nula
        if(createReservaRequest.getFechaReserva() == null){
            throw new Exception("La fecha de reserva no puede ser nula");
        }

        // Validar estado
        if(createReservaRequest.getEstado() == null || createReservaRequest.getEstado().trim().isEmpty()){
            throw new Exception("El estado no puede ser nulo o vacío");
        }

        // Validar IDs de entidades relacionadas - Socio
        if(createReservaRequest.getSocio() == null || createReservaRequest.getSocio() <= 0){
            throw new Exception("El socio no puede ser nulo");
        }

        // Validar IDs de entidades relacionadas - Clase
        if(createReservaRequest.getClase() == null || createReservaRequest.getClase() <= 0){
            throw new Exception("La clase no puede ser nula");
        }

        // Obtener entidad relacionada - Socio
        Usuario socio = usuarioRepository.findById(createReservaRequest.getSocio())
                .orElseThrow(() -> new Exception("Socio no encontrado con ID: " + createReservaRequest.getSocio()));

        // Obtener entidad relacionada - Clase
        Clase clase = claseRepository.findById(createReservaRequest.getClase())
                .orElseThrow(() -> new Exception("Clase no encontrada con ID: " + createReservaRequest.getClase()));

        // Validar cupo disponible SOLO para estados que ocupan cupo
        if (("confirmado".equals(createReservaRequest.getEstado()) ||
                "pendiente".equals(createReservaRequest.getEstado())) &&
                clase.getCupoDisponible() <= 0) {
            throw new Exception("No hay cupos disponibles para esta clase");
        }

        // Mapear request a modelo
        Reserva reserva = ReservaMapper.createRequestToModel(createReservaRequest);

        // Establecer relaciones
        reserva.setSocio(socio);
        reserva.setClase(clase);

        // Guardar en la db
        reserva = reservaRepository.save(reserva);

        // ACTUALIZAR CUPO DISPONIBLE si el estado es pendiente o confirmado
        if ("confirmado".equals(createReservaRequest.getEstado()) ||
                "pendiente".equals(createReservaRequest.getEstado())) {
            clase.setCupoDisponible(clase.getCupoDisponible() - 1);
            claseRepository.save(clase);
        }

        // Convertir a response y retornar
        return ReservaMapper.modelToResponse(reserva);
    }

    // Metodo para actualizar la reserva
    @Override
    @Transactional
    public CreateReservaResponse updateReserva(Integer id, CreateReservaRequest createReservaRequest) throws Exception {

        // Verificamos que exista
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        // Guardar estado anterior para lógica de cupos
        String estadoAnterior = reserva.getEstado();
        Clase claseAnterior = reserva.getClase();

        // Verificar que la reserva no sea nula
        if(createReservaRequest == null){
            throw new Exception("La reserva no puede ser nula");
        }

        // Verificar que fecha de reserva no sea nula
        if(createReservaRequest.getFechaReserva() == null){
            throw new Exception("La fecha de reserva no puede ser nula");
        }

        // Verificar que estado no sea nulo
        if(createReservaRequest.getEstado() == null || createReservaRequest.getEstado().trim().isEmpty()){
            throw new Exception("El estado no puede ser nulo o vacío");
        }

        // Validar IDs de entidades relacionadas - Socio
        if(createReservaRequest.getSocio() == null || createReservaRequest.getSocio() <= 0){
            throw new Exception("El socio no puede ser nulo");
        }

        // Validar IDs de entidades relacionadas - Clase
        if(createReservaRequest.getClase() == null || createReservaRequest.getClase() <= 0){
            throw new Exception("La clase no puede ser nula");
        }

        // Obtener entidad relacionada - Socio
        Usuario socio = usuarioRepository.findById(createReservaRequest.getSocio())
                .orElseThrow(() -> new Exception("Socio no encontrado con ID: " + createReservaRequest.getSocio()));

        // Obtener entidad relacionada - Clase
        Clase clase = claseRepository.findById(createReservaRequest.getClase())
                .orElseThrow(() -> new Exception("Clase no encontrada con ID: " + createReservaRequest.getClase()));

        // LÓGICA DE ACTUALIZACIÓN DE CUPOS
        String nuevoEstado = createReservaRequest.getEstado();

        // Solo procesar cambios de cupo si cambió el estado
        if (!estadoAnterior.equals(nuevoEstado)) {

            // Caso 1: De pendiente/confirmado a cancelado - LIBERAR CUPO
            if (("pendiente".equals(estadoAnterior) || "confirmado".equals(estadoAnterior))
                    && "cancelado".equals(nuevoEstado)) {

                // Liberar cupo en la clase anterior
                claseAnterior.setCupoDisponible(claseAnterior.getCupoDisponible() + 1);
                claseRepository.save(claseAnterior);
            }
            // Caso 2: De cancelado a pendiente/confirmado - OCUPAR CUPO
            else if ("cancelado".equals(estadoAnterior)
                    && ("pendiente".equals(nuevoEstado) || "confirmado".equals(nuevoEstado))) {

                // Validar que haya cupo disponible
                if (clase.getCupoDisponible() <= 0) {
                    throw new Exception("No hay cupos disponibles para confirmar esta reserva");
                }

                // Ocupar cupo en la nueva clase
                clase.setCupoDisponible(clase.getCupoDisponible() - 1);
                claseRepository.save(clase);
            }
            // Caso 3: Cambio entre pendiente y confirmado (no afecta cupos)
            // No se necesita acción
        }

        // Actualizar campos de la entidad existente
        reserva.setFechaReserva(createReservaRequest.getFechaReserva());
        reserva.setEstado(createReservaRequest.getEstado());

        // Actualizar entidades relacionadas
        reserva.setSocio(socio);
        reserva.setClase(clase);

        // Guardar cambios en db
        Reserva reservaUpdate = reservaRepository.save(reserva);

        // Mapear a CreateReservaResponse
        return ReservaMapper.modelToResponse(reservaUpdate);
    }

    // Metodo para eliminar reserva
    @Override
    @Transactional
    public void deleteReserva(Integer id) throws Exception {

        // Verificamos que exista la reserva
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new Exception("La reserva no existe"));

        // LIBERAR CUPO si la reserva estaba en estado que ocupa cupo
        if ("pendiente".equals(reserva.getEstado()) || "confirmado".equals(reserva.getEstado())) {
            Clase clase = reserva.getClase();
            clase.setCupoDisponible(clase.getCupoDisponible() + 1);
            claseRepository.save(clase);
        }

        // Eliminamos
        reservaRepository.deleteById(id);
    }
}