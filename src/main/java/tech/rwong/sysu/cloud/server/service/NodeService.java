package tech.rwong.sysu.cloud.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.repository.NodeRepository;

@Service
public class NodeService {
    @Autowired
    private NodeRepository repository;

    public Node findNodeById(User user, Long id) {
        return this.repository.findByUserAndId(user, id);
    }
    public Node findFileByFullPath(User user, String fullPath) {
        return this.repository.findByUserAndFullPath(user, fullPath);
    }
    public Node findFolderByFullPath(User user, String fullPath) {
        return this.repository.findByUserAndFullPath(user, fullPath + "/");
    }

    public Node createFolder(String name, Node parent, User user) {
        Node node = new Node();
        node.setName(name);
        node.setType(Node.NodeType.FOLDER);
        if (parent == null) {
            node.setFullPath("/");
        } else {
            node.setParent(parent);
            node.setFullPath(parent.getFullPath() + name + "/");
        }
        node.setUser(user);
        return repository.save(node);
    }

    public Node createFile(String name, Node parent, User user) {
        Node node = new Node();
        node.setName(name);
        node.setType(Node.NodeType.FILE);
        node.setParent(parent);
        node.setFullPath(parent.getFullPath() + name);
        node.setUser(user);
        return repository.save(node);
    }

    public Node deleteNode(Node node) {
        this.repository.delete(node);
        return node;
    }
}
