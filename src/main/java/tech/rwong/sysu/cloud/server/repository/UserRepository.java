package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import tech.rwong.sysu.cloud.server.model.User;

import java.util.Optional;

// @RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User s);
    User findByUsername(String username);
}
