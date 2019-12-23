package tech.rwong.sysu.cloud.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.rwong.sysu.cloud.server.model.Part;
import tech.rwong.sysu.cloud.server.model.Upload;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.repository.PartRepository;
import tech.rwong.sysu.cloud.server.repository.UploadRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UploadService {
    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private FileService fileService;

    public Upload initializeUpload(User user) {
        Upload upload = new Upload();
        upload.setUser(user);
        return this.uploadRepository.save(upload);
    }

    public Part uploadPart(User user, Long id, MultipartFile file) {
        Upload upload = this.uploadRepository.findByUserAndId(user, id);
        if (upload == null) {
            return null;
        }
        Part part = new Part();
        part.setStatus(Part.PartStatus.UPLOADING);
        part.setUpload(upload);
        part = partRepository.save(part);

        Path location = Paths.get("./data/parts/" + upload.getId() + "/" + part.getId());
        try {
            Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return part;
    }

    public Upload completeUpload(User user, Long id, List<Long> partList, String fullPath) {
        Upload upload = this.uploadRepository.findByUserAndId(user, id);
        if (upload == null) {
            return null;
        }
        // TODO
        return upload;
    }

    public Upload abortUpload(User user, Long id) {
        Upload upload = this.uploadRepository.findByUserAndId(user, id);
        if (upload == null) {
            return null;
        }
        this.uploadRepository.delete(upload);
        return upload;
    }
}
