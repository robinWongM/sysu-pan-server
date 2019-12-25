package tech.rwong.sysu.cloud.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(FileService.class);

    public Node createFolder(User user, String path) {
        LinkedList<String> pathComponents = new LinkedList<>(Arrays.asList(path.split("/", -1)));
        LinkedList<String> toBeCreatedPath = new LinkedList<>();

        if (pathComponents.getLast().equals("")) {
            pathComponents.removeLast();
        }

        Node lastExistFolder = null;
        while (!pathComponents.isEmpty()) {
            logger.info("Recursive creating folder: " + String.join("/", pathComponents));
            lastExistFolder = nodeService.findFolderByFullPath(user, String.join("/", pathComponents));
            if (lastExistFolder == null) {
                logger.info("Folder not found. ");
                toBeCreatedPath.push(pathComponents.getLast());
                pathComponents.removeLast();
            } else {
                break;
            }
        }
        for (String folder: toBeCreatedPath) {
            lastExistFolder = nodeService.createFolder(folder, lastExistFolder, user);
        }

        try {
            Files.createDirectories(Paths.get("./data/" + user.getId() + lastExistFolder.getFullPath()));
        } catch (IOException e) {
            return null;
        }
        return lastExistFolder;
    }

    public Node createFile(User user, String path, MultipartFile file) {
        if (path.contains("..")) {
            return null;
        }

        Node existFile = nodeService.findFileByFullPath(user, path);
        if (existFile != null) {
            return existFile;
        }

        LinkedList<String> pathComponents = new LinkedList<>(Arrays.asList(path.split("/", -1)));
        String filename = pathComponents.removeLast();
        if (filename.equals("")) {
            // Missing filename.
            return null;
        }

        Node folderNode = createFolder(
                user,
                String.join("/", pathComponents) + "/");
        Node fileNode = nodeService.createFile(filename, folderNode, user);
        try {
            Path localFile = Paths.get("./data/" + user.getId() + fileNode.getFullPath());
            Files.createFile(localFile);
            Files.copy(file.getInputStream(), localFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return null;
        }
        return fileNode;
    }

    public Resource downloadFile(User user, Long id) {
        Node fileNode = nodeService.findNodeById(user, id);
        if (fileNode == null || fileNode.getType() != Node.NodeType.FILE) {
            return null;
        }
        Path localFile = Paths.get("./data/" + user.getId() + fileNode.getFullPath());
        try {
            Resource resource = new UrlResource(localFile.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node deleteFile(User user, Long id) {
        Node fileNode = nodeService.findNodeById(user, id);
        if (fileNode == null || fileNode.getType() != Node.NodeType.FILE) {
            return null;
        }
        nodeService.deleteNode(fileNode);
        Path localFile = Paths.get("./data/" + user.getId() + fileNode.getFullPath());
        try {
            Files.deleteIfExists(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileNode;
    }

    public Node deleteFolder(User user, String path) {
        Node folderNode = nodeService.findFolderByFullPath(user, path);
        if (folderNode == null || folderNode.getType() != Node.NodeType.FOLDER) {
            return null;
        }
        // Will automatically delete all children node
        nodeService.deleteNode(folderNode);
        Path localFolder = Paths.get("./data/" + user.getId() + folderNode.getFullPath());
        try {
            try (Stream<Path> walk = Files.walk(localFolder)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .peek(System.out::println)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return folderNode;
    }
}
