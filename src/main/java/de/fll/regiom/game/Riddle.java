package de.fll.regiom.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class Riddle {

	private static final String riddleTitle = "EUER AKTUELLES RÄTSEL";
	private final String url;

	protected Riddle(String url) {
		this.url = url;
	}

	abstract boolean checkSolution(String s);

	public String getRiddleUrl() {
		return url;
	}

	public MessageEmbed getMessage(){
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("EUER RÄTSEL");
		builder.setImage("https://cdn.discordapp.com/attachments/795627126232449034/806979930910228580/unknown.png");
		return builder.build();
	}
}
