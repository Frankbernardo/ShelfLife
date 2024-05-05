package orderlist;

import java.time.LocalDate;
import java.util.List;

public class PurchaseOrder {
    private int orderNumber;
    private LocalDate date;
    private double totalPrice;
    private List<orderlist> items;

    public PurchaseOrder(int orderNumber, LocalDate date, double totalPrice, List<orderlist> items) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<orderlist> getItems() {
        return items;
    }
}
