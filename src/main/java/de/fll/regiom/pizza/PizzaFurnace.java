package de.fll.regiom.pizza;

public class PizzaFurnace {

	private final FurnaceSlot[] slots;
	private final int maxTemperature; // TODO;

	public PizzaFurnace() {
		this(4);
	}

	public PizzaFurnace(int size) {
		this(size, 300);
	}

	public PizzaFurnace(int size, int maxTemperature) {
		this.slots = new FurnaceSlot[size];
		for (int i = 0; i < size; i++)
			slots[i] = new FurnaceSlot();
		this.maxTemperature = maxTemperature;
	}

	public boolean hasSpace() {
		boolean hasSpace = false;
		for (FurnaceSlot slot : slots)
			hasSpace |= slot.getPizza() == null;
		return hasSpace;
	}

	public int size() {
		return slots.length;
	}

	public FurnaceSlot[] getSlots() {
		return slots;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < slots.length; i++) {
			builder.append("Slot ").append(i).append(": ");
			if (slots[i].getPizza() == null) {
				builder.append("frei!");
			} else builder.append("Bäckt ").append(slots[i].getPizza().toString());
			builder.append("\n");
		}
		return builder.toString();
	}
}
