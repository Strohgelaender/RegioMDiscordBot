package de.fll.regiom.pizza;

import java.util.HashMap;
import java.util.Map;

public class PizzaOrder {
	private static final Map<String, Integer> PRICES = new HashMap<>();


	private final Pizza pizza;
	private static int actualID = 0;
	private final int price;
	private final int orderID;
	private final long orderMaker;

	public PizzaOrder(String orderMessage, long orderMaker) {
		this(orderMessage, ++actualID, orderMaker);
	}

	public PizzaOrder(String orderMessage, int orderID, long orderMaker) {
		this.orderID = orderID;
		orderMessage = orderMessage.replaceAll(" mit", "").replaceAll(" und", "");
		String[] ingredients = orderMessage.split(";");
		int sum = 0;
		for (String ingredient : ingredients) {
			ingredient = ingredient.trim();
			sum += PRICES.getOrDefault(ingredient, 4);
		}
		this.price = sum;
		this.pizza = new Pizza(ingredients);
		this.orderMaker = orderMaker;
	}

	public long getOrderMaker() {
		return orderMaker;
	}

	public int getOrderID() {
		return orderID;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		if (pizza == null)
			return "Auftrag wurde noch nicht begonnen.";
		if (!pizza.isBaked())
			return "Pizza muss noch gebacken werden.";
		return pizza.toString() + " ist fertig";
	}

	private static void setPrices() {
		PRICES.put("Cheese", 1);
		PRICES.put("ham", 1);
		PRICES.put("salami", 1);
	}
}
