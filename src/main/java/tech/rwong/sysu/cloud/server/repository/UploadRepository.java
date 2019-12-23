package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rwong.sysu.cloud.server.model.Upload;
import tech.rwong.sysu.cloud.server.model.User;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    Upload save(Upload s);
    List<Upload> findByUser(User user);
    Upload findByUserAndId(User user, Long id);
}
