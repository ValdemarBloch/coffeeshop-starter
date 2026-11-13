package ek.osnb.demo.coffeeshop.assistant;

import ek.osnb.demo.coffeeshop.order.OrderApi;
import ek.osnb.demo.coffeeshop.order.OrderInfo;
import ek.osnb.demo.coffeeshop.order.OrderSummaryInfo;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class OrderTools {

    private final OrderApi orders;

    OrderTools(OrderApi orders) {
        this.orders = orders;
    }

    @Tool(description = "List the customer's own orders, newest first, "
            + "with status, date, number of items and total")
    List<OrderSummaryInfo> myOrders(ToolContext context) {
        return orders.findOrders(customerId(context));
    }

    @Tool(description = "Get one of the customer's own orders, with its lines and delivery address. "
            + "Get the order id from myOrders first.")
    OrderInfo myOrder(
            @ToolParam(description = "The id of the order") Long orderId,
            ToolContext context) {
        return orders.getOrder(customerId(context), orderId);
    }

    private Long customerId(ToolContext context) {
        return (Long) context.getContext().get("customerId");
    }
}