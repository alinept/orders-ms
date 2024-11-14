package ca.com.food.orders.controller;

import ca.com.food.orders.dto.OrderDto;
import ca.com.food.orders.dto.StatusDto;
import ca.com.food.orders.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

        @Autowired
        private OrderService service;

        @GetMapping()
        public List<OrderDto> getAll() {
            return service.getAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDto> findById(@PathVariable @NotNull Long id) {
            OrderDto dto = service.findById(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping()
        public ResponseEntity<OrderDto> makeOrder(@RequestBody @Valid OrderDto dto, UriComponentsBuilder uriBuilder) {
            OrderDto order = service.createOrder(dto);

            URI address = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();

            return ResponseEntity.created(address).body(order);

        }

        @PutMapping("/{id}/status")
        public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto status){
           OrderDto dto = service.updateStatus(id, status);

            return ResponseEntity.ok(dto);
        }


        @PutMapping("/{id}/paid")
        public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
            service.approvePaymentOrder(id);

            return ResponseEntity.ok().build();

        }
}
