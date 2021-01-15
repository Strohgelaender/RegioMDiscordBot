package de.fll.regiom.pizza;

public class FurnaceSlot {
	private Pizza pizza;

	/**
	 * Fills the pizza into the slot and bakes it. The slot will be locked while this is happening.
	 *
	 * @param pizza The pizza which should be baked
	 **/
	public void bakePizza(Pizza pizza) {
		if (this.pizza != null || pizza == null) {
			System.out.println("An illegal attempt to bake a pizza has happened!");
			return;
		}
		this.pizza = pizza;
		new Thread(() -> {
			try {
				Thread.sleep(pizza.getBakeTime()); //Time in Millis!
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pizza.setBaked();
			this.pizza = null;
		}).start();
	}

	public Pizza getPizza() {
		return pizza;
	}

	public boolean isFull() {
		return pizza != null;
	}
}
