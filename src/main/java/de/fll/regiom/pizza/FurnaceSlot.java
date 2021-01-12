package de.fll.regiom.pizza;

public class FurnaceSlot {
	private Pizza pizza;

	public void bakePizza(Pizza pizza) {
		this.pizza = pizza;
		new Thread(() -> {
			try {
				Thread.sleep(pizza.getBakeTime()); //Time in Millis!
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		pizza.setBaked();
		this.pizza = null;
	}

	public Pizza getPizza() {
		return pizza;
	}
}
