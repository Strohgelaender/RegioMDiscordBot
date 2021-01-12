package de.fll.regiom.pizza;

public class Pizza {

	private boolean isBaked = false;
	private final String[] ingredients;
	private final int bakeTime;

	/**
	 * Creates a raw pizza with needs to be baked a minute for each ingredient
	 *
	 * @param ingredients the ingredients this pizza will have
	 */

	public Pizza(String[] ingredients) {
		this.ingredients = ingredients;
		bakeTime = ingredients.length;
	}

	/**
	 * Bakes the Pizza. Changes the state of isBaked to true.
	 * This is typically irreversible
	 **/
	public void setBaked() {
		isBaked = true;
	}

	/**
	 * @return the bake time in millis
	 **/
	public long getBakeTime() {
		return bakeTime * 60000L;
	}

	public boolean isBaked() {
		return isBaked;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("eine leckere Pizza ");
		if (ingredients.length == 0) {
			builder.append("mit Tomatensauce, Käse und etwas Basilikum");
		} else if (ingredients.length == 1) {
			builder.append(ingredients[0]);
		} else {
			builder.append("mit ");
			for (int i = 0, ingredientsLength = ingredients.length - 1; i < ingredientsLength; i++) {
				builder.append(ingredients[i]).append(", ");
			}
			builder.append("und ").append(ingredients[ingredients.length - 1]);
		}
		return builder.toString();
	}
}
