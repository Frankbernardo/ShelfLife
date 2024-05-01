package orderlist;

import java.time.LocalDate;

public class PurchaseOrder {
    public int orderNumber; // ideally should be private with getters/setters
    public LocalDate date;
    public double totalPrice;

    public PurchaseOrder(int orderNumber, LocalDate date, double totalPrice) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.totalPrice = totalPrice;
    }

    // Getters and setters if fields are private
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
