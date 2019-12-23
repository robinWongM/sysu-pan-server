package tech.rwong.sysu.cloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.service.FileService;
import tech.rwong.sysu.cloud.server.service.NodeService;

import java.security.Principal;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    NodeService nodeService;

    @GetMapping("/{id}")
    public Node getOneFile(Principal principal, @PathVariable(value = "id") final String id) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        Node fileNode = this.nodeService.findNodeById(currentUser, Long.parseLong(id));

        if (fileNode == null || fileNode.getType() != Node.NodeType.FILE
                || !fileNode.getUser().getId().equals(currentUser.getId())) {
            return null;
        }
        return fileNode;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadOneFile(Principal principal, @PathVariable(value = "id") final String id) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        Resource resource = fileService.downloadFile(currentUser, Long.parseLong(id));
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("")
    public Node createFile(Principal principal, @RequestParam("file") MultipartFile file, @RequestParam("fullPath") String fullPath) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        Node fileNode = fileService.createFile(currentUser, fullPath, file);
        if (fileNode == null) {
            return null;
        }
        return fileNode;
    }
}
