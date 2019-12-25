package tech.rwong.sysu.cloud.server.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.rwong.sysu.cloud.server.dto.UserDto;
import tech.rwong.sysu.cloud.server.exception.ResourceNotFoundException;
import tech.rwong.sysu.cloud.server.model.Node;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.repository.UserRepository;
import tech.rwong.sysu.cloud.server.service.FileService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    @PostMapping("/sign_up")
    public User createUser(@Valid @RequestBody UserDto userDto) {
        User user = new User(
                userDto.getUsername(),
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail()
        );
        user = userRepository.save(user);
        fileService.createFolder(user, "/");
        return user;
    }
}
