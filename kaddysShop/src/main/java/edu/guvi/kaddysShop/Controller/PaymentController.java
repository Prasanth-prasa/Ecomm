package edu.guvi.kaddysShop.Controller;

import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Service.OrderService;
import edu.guvi.kaddysShop.Service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayPalService payPalService;

    @GetMapping("/{orderId}")
    public String pay(@PathVariable Long orderId) {

        Order order = orderService.getOrder(orderId);

        try {
            String approvalUrl = payPalService.createPayment(
                    "ORDER_" + order.getId(),
                    order.getTotalAmount());

            if (approvalUrl == null || approvalUrl.isEmpty()) {
                System.out.println("PayPal returned NULL approval URL");
                return "redirect:/order/checkout"; 
            }

            return "redirect:" + approvalUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/success")
    public String success(@RequestParam String token,
            @RequestParam String orderId,
            Model model) {

        try {
            // Use PayPal Order, not your local Order model
            com.paypal.orders.Order capturedOrder = payPalService.capturePayment(token);

            // Extract PayPal transaction ID
            String transactionId = capturedOrder.purchaseUnits().get(0)
                    .payments()
                    .captures()
                    .get(0)
                    .id();

            // Convert ORDER_7 → 7
            Long localOrderId = Long.valueOf(orderId.replace("ORDER_", ""));

            // Load your local ecommerce order
            edu.guvi.kaddysShop.Model.Order order = orderService.getOrder(localOrderId);

            order.setStatus("PAID");
            orderService.save(order);

            model.addAttribute("orderId", localOrderId);
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("amount", order.getTotalAmount());
            model.addAttribute("date", new java.util.Date().toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "order-success";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "order-cancel";
    }
}
