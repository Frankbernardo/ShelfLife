package orderlist;

public class orderlist {
	 private String item;
	 private String SKU;
	 private double price;
	 private int inventory;
	 private String input;
	 private double total;
	 
	
	 public orderlist (String item, String SKU, double price, int inventory, String input, double total) {
		 this.item = item;
		 this.SKU = SKU;
		 this.price = price;
		 this.inventory = inventory;
		 this.input = input;
		 this.total = total;
	 }


	public String getItem() {
		return item;
	}


	public void setItem(String item) {
		this.item = item;
	}


	public String getSKU() {
		return SKU;
	}


	public void setSKU(String sKU) {
		SKU = sKU;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getInventory() {
		return inventory;
	}


	public void setInventory(int inventory) {
		this.inventory = inventory;
	}


	public String getInput() {
		return input;
	}


	public void setInput(String input) {
		this.input = input;
	}
	
	public double getTotal() {
	    return total;
	}

	public void setTotal(double total) {
	    this.total = total;
	}
	 
	 
}
