package de.fll.regiom.model.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class Riddle {

	private static final String TITLE = "EUER AKTUELLES RÄTSEL";
	private final String url;
	private final int id;

	protected Riddle(String url, int id) {
		this.url = url;
		this.id = id;
	}

	public abstract boolean checkSolution(String s);

	public String getRiddleUrl() {
		return url;
	}

	public MessageEmbed getMessage() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(TITLE);
		builder.setImage(url);
		return builder.build();
	}

	public int getId() {
		return id;
	}
}
