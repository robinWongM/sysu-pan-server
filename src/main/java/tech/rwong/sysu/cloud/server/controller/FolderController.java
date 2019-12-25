package tech.rwong.sysu.cloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.service.FileService;
import tech.rwong.sysu.cloud.server.service.NodeService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/folders")
public class FolderController {
    @Autowired
    private NodeService nodeService;

    @Autowired
    private FileService fileService;

    @GetMapping(value = {"**"})
    public Node listFolder(Principal principal, HttpServletRequest request) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("^/folders", "").replaceFirst("/$", "");
        return nodeService.findFolderByFullPath(currentUser, path);
    }

    @PostMapping(value = {"**"})
    public Node createFolder(Principal principal, HttpServletRequest request) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("^/folders", "");
        return fileService.createFolder(currentUser, path);
    }

    @DeleteMapping(value = {"**"})
    public Node deleteFolder(Principal principal, HttpServletRequest request) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("^/folders", "");
        return fileService.deleteFolder(currentUser, path);
    }
}
