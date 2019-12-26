package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rwong.sysu.cloud.server.model.Share;
import tech.rwong.sysu.cloud.server.model.User;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Share save(Share s);
    List<Share> findByUser(User user);
    Share findByUserAndId(User user, Long id);
}
