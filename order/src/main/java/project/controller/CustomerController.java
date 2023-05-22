package project.controller;

import lombok.AllArgsConstructor;
import model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.service.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private CustomerService customerService;

    @GetMapping("get-balance")
    public ResponseEntity<String> getBalance() {
        return ResponseEntity.ok("Your Balance is " + customerService.getBalance());
    }

    @GetMapping("get-notifications")
    public ResponseEntity<NotificationDTO> getNotifications() {
        return ResponseEntity.ok(customerService.getNotifications());
    }

    @PostMapping("topUp")
    public ResponseEntity<String> topUp(@RequestBody MoneyDto money) {
        return ResponseEntity.ok(customerService.topUpBalance(money.getSum()));
    }
}
