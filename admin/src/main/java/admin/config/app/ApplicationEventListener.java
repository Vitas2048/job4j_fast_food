package admin.config.app;

import admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final ApplicationConfig config;
    private final AdminService adminService;

    @EventListener(ApplicationReadyEvent.class)
    public void handleReady() {
        adminService.init(config.getAdmin());
    }
}
