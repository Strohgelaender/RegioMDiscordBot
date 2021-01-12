package de.fll.regiom.pizza;

public class PizzaFurnace {

	private final FurnaceSlot[] slots;
	private final int maxTemperature; // TODO: implement furnace temperature mechanic

	/**
	 * Creates a furnace with 300°C maximum temperature and 4 slots
	 */
	public PizzaFurnace() {
		this(4);
	}

	/**
	 * Creates a furnace with the given size and 300°C maximum temperature
	 *
	 * @param size the count of furnace slots this furnace should have
	 */
	public PizzaFurnace(int size) {
		this(size, 300);
	}

	/**
	 * Creates a furnace with the given size and maximum temperature
	 *
	 * @param size           the count of furnace slots this furnace will have
	 * @param maxTemperature the maximum temperature this furnace will be able to reach
	 */
	public PizzaFurnace(int size, int maxTemperature) {
		if (size == 0)
			size = 4;
		if (size < 0)
			size = -size;
		this.slots = new FurnaceSlot[size];
		for (int i = 0; i < size; i++)
			slots[i] = new FurnaceSlot();
		this.maxTemperature = maxTemperature;
	}

	/**
	 * checks if there is a free furnace slot
	 *
	 * @return false, if all slots are full, otherwise true
	 */
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
