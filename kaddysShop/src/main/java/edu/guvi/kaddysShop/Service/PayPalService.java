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

    // Base URL for production (Render)
    private static final String PROD_URL = "https://ecomm-qhmy.onrender.com";

    // Base URL for local development
    private static final String LOCAL_URL = "http://localhost:8080";

    private boolean isLocalEnvironment() {
        // Spring Boot sets this correctly
        String env = System.getenv("ENV");
        return env != null && env.equals("LOCAL");
    }

    private String buildUrl(String path, String orderId) {
        if (isLocalEnvironment()) {
            return LOCAL_URL + path + "?orderId=" + orderId;
        } else {
            return PROD_URL + path + "?orderId=" + orderId;
        }
    }

    public String createPayment(String orderId, double inrAmount) throws IOException {

        double usdAmount = inrAmount / 85.0;

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
