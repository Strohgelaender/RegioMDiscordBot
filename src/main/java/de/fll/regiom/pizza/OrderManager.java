package de.fll.regiom.pizza;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class OrderManager { //TODO: MAYBE RENAME IN PIZZERIA
	private final HashMap<Long, List<PizzaOrder>> users = new HashMap<>();
	private final TreeMap<Integer, PizzaOrder> orders = new TreeMap<>();
	private final PizzaFurnace furnace;
	private final MessageChannel channel;
	PizzaOrder[] activeOrders;
	Thread baker;

	public OrderManager(MessageChannel channel) {
		this(4, channel);
	}

	public OrderManager(int furnaceSize, MessageChannel channel) {
		furnace = new PizzaFurnace(furnaceSize);
		activeOrders = new PizzaOrder[furnaceSize];
		this.channel = channel;
		baker = new Thread(() -> {
			while (true) {
				for (PizzaOrder order : activeOrders) {
					if (order == null)
						continue;
					if (order.getPizza().isBaked())
						deliverOrder(order);
				}
				FurnaceSlot[] slots = furnace.getSlots();
				for (int i = 0; i < slots.length; i++) {
					FurnaceSlot slot = slots[i];
					if (slot.getPizza() == null)
						if (activeOrders[i] != null){
							System.out.println("Pizza in Ofen");
							slot.bakePizza(activeOrders[i].getPizza());
						}
				}
				try {
					Thread.sleep(60000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		baker.start();
	}

	public void makeOrder(Long user, String orderMessage) {
		if (!users.containsKey(user))
			users.put(user, new ArrayList<>());
		PizzaOrder pizzaOrder = (user != 138584634710687745L) ? new PizzaOrder(orderMessage, user) : new PizzaOrder(orderMessage, Integer.MAX_VALUE, user); //Ich = höchste Priorität
		orders.put(pizzaOrder.getOrderID(), pizzaOrder);
		users.get(user).add(pizzaOrder);
		for (int i = 0; i < activeOrders.length; i++) {
			if (activeOrders[i] == null) {
				activeOrders[i] = orders.pollFirstEntry().getValue();
				break;
			}
		}
	}

	private void deliverOrder(PizzaOrder order) {
		channel.sendMessage(order.toString()).queue();
		users.get(order.getOrderMaker()).remove(order);
		for (int i = 0; i < activeOrders.length; i++) {
			if (activeOrders[i] == order)
				if (!orders.isEmpty())
					activeOrders[i] = orders.pollFirstEntry().getValue();
				else activeOrders[i] = null;
		}
	}

	private List<PizzaOrder> getOrders(long user) {
		return users.getOrDefault(user, null);
	}
}
