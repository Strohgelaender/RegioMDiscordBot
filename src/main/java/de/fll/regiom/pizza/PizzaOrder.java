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


	/**
	 * Standard Constructor which automatically sets the ID of this order
	 *
	 * @param orderMessage the String of the order Message without any command prefixes
	 * @param orderMaker   the ID of the user who is making a Order
	 */
	public PizzaOrder(String orderMessage, long orderMaker) {
		this(orderMessage, ++actualID, orderMaker);
	}

	/**
	 * Alternative Constructor if some users should have higher priority than others
	 *
	 * @param orderMessage the String of the order Message without any command prefixes
	 * @param orderID      the ID this order should have. the lower the higher priority this order will have
	 * @param orderMaker   the ID of the user who is making a Order
	 */
	public PizzaOrder(String orderMessage, int orderID, long orderMaker) {
		this.orderID = orderID;
		orderMessage = orderMessage.replaceAll("mit ", "").replaceAll("und ", "").replaceAll(",", "");
		String[] ingredients;
		if (orderMessage.contains(";"))
			ingredients = orderMessage.split(";");
		else ingredients = orderMessage.split(" ");
		int sum = 0;
		for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++) {
			String ingredient = ingredients[i];
			ingredient = ingredient.trim();
			if (ingredients[i].equals("extra")) {
				if (i < ingredientsLength - 1)
					ingredients[i + 1] = "extra " + ingredients[i + 1];
				else ingredients[i - 1] = "extra " + ingredients[i - 1];
			}
			sum += PRICES.getOrDefault(ingredient, 4);
		}
		this.price = sum;
		this.pizza = (orderMessage.length() > 0) ? new Pizza(ingredients) : new Pizza(new String[0]);
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
