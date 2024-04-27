package orderlist;

import java.time.LocalDate;

public class PurchaseOrder {
	private final int orderNumber;
    private final LocalDate date;
    private final double totalPrice;

    public PurchaseOrder(int orderNumber, LocalDate date, double totalPrice) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.totalPrice = totalPrice;
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
}