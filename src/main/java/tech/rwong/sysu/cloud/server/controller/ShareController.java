package tech.rwong.sysu.cloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.rwong.sysu.cloud.server.dto.CreateShareDto;
import tech.rwong.sysu.cloud.server.model.Share;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.service.ShareService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private ShareService shareService;

    @GetMapping("")
    public List<Share> getShareList(Principal principal) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        return shareService.getShareList(currentUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadOneFile(@PathVariable(value = "id") final String id) {
        Resource resource = shareService.getShareFile(Long.parseLong(id));
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("")
    public Share createShare(Principal principal, @RequestBody CreateShareDto createShareDto) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        return shareService.createShare(currentUser, createShareDto.getFileId());
    }

    @DeleteMapping("/{id}")
    public void deleteShare(Principal principal, @PathVariable(value = "id") final String id) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        Share deletedShare = shareService.deleteShare(currentUser, Long.parseLong(id));
        if (deletedShare == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Share not found.");
        }
    }
}
