package project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.message.registration.RegistrationRequest;
import project.service.CustomerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/registration")
public class RegistrationController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        customerService.register(request);
        return ResponseEntity.ok().build();
    }
}
