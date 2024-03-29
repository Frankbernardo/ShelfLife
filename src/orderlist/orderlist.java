package orderlist;

public class orderlist {
	 private String item;
	 private String SKU;
	 private double price;
	 private int inventory;
	 private int input;
	 
	
	 public orderlist (String item, String SKU, double price, int inventory, int input) {
		 this.item = item;
		 this.SKU = SKU;
		 this.price = price;
		 this.inventory = inventory;
		 this.input = input;
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


	public int getInput() {
		return input;
	}


	public void setInput(int input) {
		this.input = input;
	}
	 
	 
}
