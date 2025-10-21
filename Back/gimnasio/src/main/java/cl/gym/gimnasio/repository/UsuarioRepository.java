package cl.gym.gimnasio.repository;

import cl.gym.gimnasio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Verificar email
    boolean existsByEmail(String email);
}