package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rwong.sysu.cloud.server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User s);
    User findByUsername(String username);
}
