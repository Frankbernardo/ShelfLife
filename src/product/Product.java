package product;

public class Product{
	private String upc;
	private String itemName;
	private String description;
	private String category;
	private double cost;
	private int quantityLeft;

    public Product(String upc, String itemName, String description, String category, double cost, int quantityLeft) {
    	this.upc = upc;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.quantityLeft = quantityLeft;
    }

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getQuantityLeft() {
		return quantityLeft;
	}

	public void setQuantityLeft(int quantityLeft) {
		this.quantityLeft = quantityLeft;
	}
    
    
}
