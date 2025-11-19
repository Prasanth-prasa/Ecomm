package edu.guvi.kaddysShop.Service;

import com.paypal.orders.*;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalHttpClient client;

    // CHANGE THIS TO YOUR DEPLOYED URL
    private static final String DEPLOYED_URL = "https://kaddysShop.onrender.com";

    // Detect localhost automatically
    private boolean isLocalhost() {
        return java.net.InetAddress.getLoopbackAddress().getHostName().equals("localhost");
    }

    private String buildUrl(String path, String orderId) {
        if (isLocalhost()) {
            return "http://localhost:8080" + path + "?orderId=" + orderId;
        } else {
            return DEPLOYED_URL + path + "?orderId=" + orderId;
        }
    }

    public String createPayment(String orderId, double inrAmount) throws IOException {

        double usdAmount = inrAmount / 85.0;  // INR → USD approx

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext context = new ApplicationContext()
                .brandName("KaddysShop")
                .landingPage("LOGIN")
                .returnUrl(buildUrl("/payment/success", orderId))
                .cancelUrl(buildUrl("/payment/cancel", orderId));

        PurchaseUnitRequest unit = new PurchaseUnitRequest()
                .referenceId(orderId)
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.format("%.2f", usdAmount)));

        orderRequest.purchaseUnits(List.of(unit));
        orderRequest.applicationContext(context);

        OrdersCreateRequest request = new OrdersCreateRequest()
                .requestBody(orderRequest);

        Order order = client.execute(request).result();

        // Return approval URL
        return order.links().stream()
                .filter(x -> x.rel().equalsIgnoreCase("approve"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No approval URL"))
                .href();
    }

    public Order capturePayment(String paypalOrderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(paypalOrderId);
        return client.execute(request).result();
    }
}
