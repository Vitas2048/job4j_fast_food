package admin.controller;

import admin.message.registration.RegistrationRequest;
import admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        adminService.register(request);
        return ResponseEntity.ok().build();
    }
}
