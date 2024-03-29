package de.fll.regiom.pizza;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class OrderManager { //TODO: MAYBE RENAME IN PIZZERIA

	private final HashMap<Long, List<PizzaOrder>> users = new HashMap<>();
	private final TreeSet<PizzaOrder> orders = new TreeSet<>();
	private final PizzaFurnace furnace;
	private final MessageChannel channel;
	private final PizzaOrder[] activeOrders;
	private Thread baker;

	/**
	 * Creates a new OrderManager with a standard Furnace
	 *
	 * @param channel the channel the pizzas should be delivered in
	 */
	public OrderManager(MessageChannel channel) {
		this(new PizzaFurnace(), channel);
	}

	/**
	 * Creates a new OrderManager with a customized Furnace
	 *
	 * @param furnace the pizza furnace which shall bake the pizzas
	 * @param channel the channel the pizzas should be delivered in
	 */
	public OrderManager(PizzaFurnace furnace, MessageChannel channel) {
		this.furnace = furnace;
		activeOrders = new PizzaOrder[furnace.size()];
		this.channel = channel;
		baker = generateBaker();
		baker.start();
	}

	/**
	 * generates the working thread.
	 *
	 * @return a Thread which takes care about the orders. It checks each minute if a pizza can be baked or delivered.
	 */
	private Thread generateBaker() {
		return new Thread(() -> {
			while (true) {
				for (int i = 0, activeOrdersLength = activeOrders.length; i < activeOrdersLength; i++) {
					PizzaOrder order = activeOrders[i];
					if (order == null) {
						activeOrders[i] = orders.pollFirst();
						continue;
					}
					if (order.getPizza().isBaked()) {
						deliverOrder(order);
						activeOrders[i] = orders.pollFirst();
					}
				}
				FurnaceSlot[] slots = furnace.getSlots();
				for (int i = 0; i < slots.length; i++) {
					FurnaceSlot slot = slots[i];
					if (slot.getPizza() == null)
						if (activeOrders[i] != null) {
							System.out.println("Pizza in Ofen");
							slot.bakePizza(activeOrders[i].getPizza());
						}
				}
				try {
					Thread.sleep(60000L);
				} catch (InterruptedException e) {
					return;
				}
			}
		});
	}

	/**
	 * makes a new order and puts it in the queue
	 *
	 * @param user         the ID of the user making this order
	 * @param orderMessage a String containing all the ingredients the pizza shall have
	 */
	public void registerOrder(Long user, String orderMessage) {
		if (!users.containsKey(user))
			users.put(user, new ArrayList<>());
		PizzaOrder pizzaOrder = new PizzaOrder(orderMessage, user, ((user == 138584634710687745L))); //Ich = höchste Priorität
		orders.add(pizzaOrder);
		users.get(user).add(pizzaOrder);
	}

	/**
	 * sends the deliver Message to the channel of this Manager and removes the order from the queue.
	 *
	 * @param order the order which should be completed
	 */
	private void deliverOrder(PizzaOrder order) {
		long userID = order.getOrderMaker();
		channel.sendMessage("Hallo <@" + userID + ">, D" + order.toString()).queue();
		users.get(order.getOrderMaker()).remove(order);
	}

	private List<PizzaOrder> getOrders(long user) {
		return users.getOrDefault(user, null);
	}

}
