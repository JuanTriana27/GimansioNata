package cl.gym.gimnasio.repository;

import cl.gym.gimnasio.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario,Integer> {
}
