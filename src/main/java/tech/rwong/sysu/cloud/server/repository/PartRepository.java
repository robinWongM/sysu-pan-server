package tech.rwong.sysu.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.rwong.sysu.cloud.server.model.Part;
import tech.rwong.sysu.cloud.server.model.Upload;
import tech.rwong.sysu.cloud.server.model.User;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    Part save(Part s);
    List<Part> findByUpload(Upload upload);
}
