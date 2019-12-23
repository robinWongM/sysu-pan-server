package tech.rwong.sysu.cloud.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class UploadController {
    @PostMapping("/start")
    public void startMultipart() {

    }

    @PostMapping("/merge")
    public void mergePieces() {

    }
}
