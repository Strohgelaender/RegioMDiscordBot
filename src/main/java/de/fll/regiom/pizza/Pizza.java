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
			String ing = ingredients[0];
			if (ing.contains("extra") || ing.contains("viel"))
				builder.append("mit ");
			builder.append(firstToUpperCase(ing));
		} else {
			builder.append("mit ");
			for (int i = 0, ingredientsLength = ingredients.length - 1; i < ingredientsLength; i++) {
				builder.append(firstToUpperCase(ingredients[i])).append(", ");
			}
			builder.deleteCharAt(builder.length() - 2).append("und ").append(firstToUpperCase(ingredients[ingredients.length - 1]));
		}
		return builder.toString();
	}

	private String firstToUpperCase(String old) {
		if (old.isEmpty())
			return old;
		boolean extra = old.contains("extra");
		boolean viel = old.contains("viel");
		if (extra && !viel) {
			return "extra " + firstToUpperCase(old.replaceFirst("extra", "").trim());
		}
		if (viel && !extra) {
			return "viel" + firstToUpperCase(old.replaceFirst("viel", "").trim());
		}
		if (viel) {
			return old.substring(0, "extra viel".length()) + " "
					+ firstToUpperCase(old.replaceFirst("extra", "").replaceFirst("viel", "").trim());
		}
		char upper = old.toUpperCase().charAt(0);
		return old.replaceFirst(String.valueOf(old.charAt(0)), String.valueOf(upper));
	}


}
