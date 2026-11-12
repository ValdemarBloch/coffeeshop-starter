package ek.osnb.demo.coffeeshop.order;


import ek.osnb.demo.coffeeshop.common.NotFoundException;
import ek.osnb.demo.coffeeshop.customer.CustomerApi;
import ek.osnb.demo.coffeeshop.customer.CustomerInfo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private final OrderService service;
    private final CustomerApi customer;

    OrderController(OrderService service, CustomerApi customer) {
        this.service = service;
        this.customer = customer;
    }

    // Checkout: turns a cart into an order
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request, Authentication auth) {
        CustomerInfo customerinfo = customer.findCustomerByEmail(auth.getName())
                .orElseThrow(()-> new NotFoundException("Please register a profile before placing an order"));

        return service.placeOrder(request, customerinfo);
    }

    // GET /api/orders?customerId=1 - newest first
    @GetMapping("/orders")
    List<OrderSummaryResponse> findByCustomer(Authentication auth) {
        CustomerInfo customerInfo = customer.findCustomerByEmail(auth.getName())
                .orElseThrow(()-> new NotFoundException("No customer profile found"));
        return service.findByCustomer(customerInfo.id());
    }

    @GetMapping("/{id}")
    OrderResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/{id}/cancel")
    OrderResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    // Should only be allowed for staff - a job for Spring Security later
    @PostMapping("/{id}/ship")
    OrderResponse ship(@PathVariable Long id) {
        return service.ship(id);
    }
}
