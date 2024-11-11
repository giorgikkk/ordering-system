package com.example.notificationservice.service;

import com.example.notificationservice.config.RabbitMQConfig;
import com.example.notificationservice.model.dto.OrderDTO;
import com.example.productsservice.model.Product;
import com.example.productsservice.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private final EmailService emailService;
    private final SmsService smsService;
    private final ProductService productService;

    @Autowired
    public NotificationService(final EmailService emailService, final SmsService smsService, final ProductService productService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onOrderPlaced(OrderDTO order) {
        sendEmailToSellers(order);
        sendSmsToClient(order);
    }

    private void sendEmailToSellers(final OrderDTO order) {
        Map<String, List<String>> clientEmailAndProductsInfo = getClientEmailAndProductsInfo(order);

        for (String sellerEmail : clientEmailAndProductsInfo.keySet()) {
            String subject = "New Order Placed: " + order.getId();
            String emailContent = buildSellerEmailContent(order, clientEmailAndProductsInfo.get(sellerEmail));

            emailService.sendEmail(sellerEmail, subject, emailContent);
        }
    }

    private Map<String, List<String>> getClientEmailAndProductsInfo(final OrderDTO order) {
        Map<String, List<String>> clientEmailAdProductsInfo = new HashMap<>();

        List<String> sellerEmails = order.getSellersEmails();
        List<Long> productIds = order.getProductIds();
        List<Integer> quantities = order.getQuantities();

        for (int i = 0; i < sellerEmails.size(); i++) {
            String sellerEmail = sellerEmails.get(i);
            if (sellerEmail == null) {
                continue;
            }
            Long sellerProductId = productIds.get(i);
            int productQuantity = quantities.get(i);

            final String productInfo = "Product ID: " + sellerProductId +
                    ", Quantity: " + productQuantity + "\n";

            if (!clientEmailAdProductsInfo.containsKey(sellerEmail)) {
                clientEmailAdProductsInfo.put(sellerEmail, new ArrayList<>());
            }
            List<String> productsInfo = clientEmailAdProductsInfo.get(sellerEmail);
            productsInfo.add(productInfo);
        }
        return clientEmailAdProductsInfo;
    }

    private String buildSellerEmailContent(final OrderDTO order, final List<String> productsInfo) {
        StringBuilder content = new StringBuilder();
        content.append("Order ID: ").append(order.getId()).append("\n");
        content.append("Client Username: ").append(order.getClientUsername()).append("\n\n");
        content.append("Ordered Products:\n");

        for (final String productInfo : productsInfo) {
            content.append(productInfo);
        }
        return content.toString();
    }

    private void sendSmsToClient(final OrderDTO order) {
        String smsContent = "Order #" + order.getId() + " placed. Total Price: " + calculateTotalPrice(order);
        smsService.sendSms(order.getClientPhoneNumber(), smsContent);
    }

    private double calculateTotalPrice(final OrderDTO order) {
        double totalPrice = 0.0;
        List<Long> productIds = order.getProductIds();
        List<Integer> quantities = order.getQuantities();

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productService.getProductById(productIds.get(i));
            if (product != null) {
                totalPrice += product.getPrice() * quantities.get(i);
            }
        }
        return totalPrice;
    }
}
