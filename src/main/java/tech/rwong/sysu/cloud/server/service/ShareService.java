package tech.rwong.sysu.cloud.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.Share;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.repository.NodeRepository;
import tech.rwong.sysu.cloud.server.repository.ShareRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShareService {
    @Autowired
    private ShareRepository repository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private NodeService nodeService;

    public Resource getShareFile(Long id) {
        Optional<Share> share = repository.findById(id);
        if (!share.isPresent()) {
            return null;
        }
        Share existShare = share.get();
        Path localFile = Paths.get("./data/" + existShare.getUser().getId() + existShare.getFullPath());
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

    public List<Share> getShareList(User user) {
        return this.repository.findByUser(user);
    }

    public Share createShare(User user, Long fileId) {
        Node fileNode = nodeService.findNodeById(user, fileId);
        if (fileNode == null || fileNode.getType() != Node.NodeType.FILE) {
            return null;
        }
        if (fileNode.getShare() != null) {
            return fileNode.getShare();
        }
        Share share = new Share();
        share.setFile(fileNode);
        share.setFullPath(fileNode.getFullPath());
        share.setName(fileNode.getName());
        share.setUser(user);
        fileNode.setShare(share);
        repository.save(share);
        nodeRepository.save(fileNode);
        return share;
    }

    public Share deleteShare(User user, Long shareId) {
        Share deletedShare = repository.findByUserAndId(user, shareId);
        if (deletedShare == null) {
            return null;
        }
        deletedShare.getFile().setShare(null);
        repository.delete(deletedShare);
        return deletedShare;
    }
}
