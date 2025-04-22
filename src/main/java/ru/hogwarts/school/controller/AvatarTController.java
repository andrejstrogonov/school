package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatars")
public class AvatarTController {
    private final AvatarService avatarService;

    @Autowired
    public AvatarTController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    public ResponseEntity<Page<Avatar>> getAvatars(@RequestParam int page, @RequestParam int size) {
        Page<Avatar> avatars = avatarService.getAvatars(page, size);
        return ResponseEntity.ok(avatars);
    }
}