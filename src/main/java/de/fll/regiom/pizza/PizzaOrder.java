package de.fll.regiom.pizza;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PizzaOrder implements Comparable<PizzaOrder> {

	private static final Map<String, Integer> PRICES = new HashMap<>();
	private static int actualID = 0;
	private static int highestID = Integer.MAX_VALUE;
	private final Pizza pizza;
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
	 * @param highPriority if high priority should be used
	 * @param orderMaker   the ID of the user who is making a Order
	 */
	public PizzaOrder(String orderMessage, long orderMaker, boolean highPriority) {
		this(orderMessage, (highPriority) ? highestID-- : ++actualID, orderMaker);
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
		orderMessage = orderMessage.replaceAll("mit ", "").replaceAll("und ", "");
		orderMessage = orderMessage.replaceAll(", ", ",").replaceAll(",", " ").replaceAll("bitte", "").trim();
		List<String> ingredients;
		if (orderMessage.contains(";"))
			ingredients = new ArrayList<>(Arrays.asList(orderMessage.split(";")));
		else ingredients = new ArrayList<>(Arrays.asList(orderMessage.split(" ")));
		int sum = 0;
		for (int i = 0, ingredientsLength = ingredients.size(); i < ingredientsLength; i++) {
			ingredients.set(i, ingredients.get(i).trim());
			if (ingredients.get(i).contains("(")) {
				ingredients.set(i, null);
			}
			if (ingredients.get(i).equals("extra")) {
				mergeWords(ingredients, i, "extra");
				continue;
			}
			if (ingredients.get(i).equals("viel")) {
				mergeWords(ingredients, i, "viel");
				continue;
			}
			if (ingredients.get(i).equals("extra viel") || ingredients.get(i).equals("viel extra")) {
				mergeWords(ingredients, i, ingredients.get(i));
			}
			sum += PRICES.getOrDefault(ingredients.get(i), 4);
		}
		this.price = sum;
		ingredients.removeIf(Objects::isNull);
		this.pizza = new Pizza(ingredients.toArray(new String[0]));
		this.orderMaker = orderMaker;
	}

	private void mergeWords(List<String> ingredients, int i, String added) {
		int target = (i < ingredients.size() - 1) ? i + 1 : i - 1;
		ingredients.set(target, added + " " + ingredients.get(target));
		ingredients.set(i, null);
	}

	// Getters and Setters
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


	// SETUP
	private static void setPrices() {
		PRICES.put("Cheese", 1);
		PRICES.put("ham", 1);
		PRICES.put("salami", 1);
	}

	//Overriding Object Methods
	@Override
	public String toString() {
		if (pizza == null)
			return "Auftrag wurde noch nicht begonnen.";
		if (!pizza.isBaked())
			return "Pizza muss noch gebacken werden.";
		return pizza.toString() + " ist fertig";
	}

	@Override
	public int compareTo(@NotNull PizzaOrder o) {
		return Integer.compare(this.getOrderID(), o.getOrderID());
	}
}
