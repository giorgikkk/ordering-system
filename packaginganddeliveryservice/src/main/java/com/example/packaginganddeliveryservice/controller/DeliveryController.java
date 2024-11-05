package com.example.packaginganddeliveryservice.controller;

import com.example.packaginganddeliveryservice.model.DeliveryOrder;
import com.example.packaginganddeliveryservice.model.DeliveryOrderStatus;
import com.example.packaginganddeliveryservice.model.enums.DeliveryOrderStatusType;
import com.example.packaginganddeliveryservice.service.DeliveryOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    private final DeliveryOrderService orderService;

    @Autowired
    public DeliveryController(final DeliveryOrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Retrieve orders by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryOrder[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid status provided", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/status")
    public ResponseEntity<List<DeliveryOrder>> getOrdersByStatus(@RequestParam DeliveryOrderStatusType status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(new DeliveryOrderStatus(status)));
    }

    @Operation(summary = "Retrieve orders by date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryOrder[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid date range provided", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<DeliveryOrder>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.getOrdersByDateRange(startDate, endDate));
    }
}
