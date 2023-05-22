package admin.controller;

import admin.message.account.AdminDTO;
import admin.service.AdminService;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private AdminService adminService;

    @GetMapping("/info")
    public ResponseEntity<AdminDTO> get() {
        final var body = adminService.get();
        return ResponseEntity.ok(new AdminDTO(body));
    }

    @PostMapping("/delete/dish")
    public ResponseEntity<String> removeDish(@RequestBody IdDTO id) {
        adminService.removeDish(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("delete/customer")
    public ResponseEntity<String> removeCustomer(@RequestBody IdDTO id) {
        adminService.removeCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-income")
    public ResponseEntity<IncomeDto> getIncome() {
        var body = adminService.getIncomeDto();
        if (body == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping("get-customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(adminService.getCustomers());
    }

    @GetMapping("get-dishes")
    public ResponseEntity<List<Dish>> getDishes() {
        return ResponseEntity.ok(adminService.getDishes());
    }
}
