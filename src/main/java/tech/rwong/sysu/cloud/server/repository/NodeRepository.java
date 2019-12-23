package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Node save(Node s);
    Node findByUserAndId(User user, Long id);
    Node findByUserAndFullPath(User user, String fullPath);
}
