package de.fll.regiom.pizza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
