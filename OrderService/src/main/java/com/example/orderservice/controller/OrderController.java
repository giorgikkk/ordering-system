package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.dto.OrderRequest;
import com.example.orderservice.model.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order for the authenticated client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid order data", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Client not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") final String token, @RequestBody final OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.placeOrder(orderRequest, token);
        return ResponseEntity.ok(orderResponse);
    }
}
