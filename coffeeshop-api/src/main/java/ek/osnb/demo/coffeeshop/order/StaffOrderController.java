package ek.osnb.demo.coffeeshop.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Only staff: SecurityConfig allows /api/staff/** for ROLE_STAFF only
@RestController
class StaffOrderController {

    private final OrderService service;

    StaffOrderController(OrderService service) {
        this.service = service;
    }

    // GET /api/staff/orders - every order, newest first
    @GetMapping("/api/staff/orders")
    List<OrderSummaryResponse> findAll() {
        return service.findAll();
    }
}
