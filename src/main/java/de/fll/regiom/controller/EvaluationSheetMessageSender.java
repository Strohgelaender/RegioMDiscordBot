package de.fll.regiom.controller;

import de.fll.regiom.model.evaluation.EvaluationCategory;
import de.fll.regiom.model.evaluation.EvaluationSheet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class EvaluationSheetMessageSender {

	public void sendSheet(MessageChannel channel, EvaluationSheet sheet) {
		if (channel == null || sheet == null)
			return;
		sendTitle(channel, sheet);
		for (EvaluationCategory category : sheet.getCategories()) {
			sendCategory(channel, category);
		}
	}

	private void sendTitle(MessageChannel channel, EvaluationSheet sheet) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(sheet.getTitle());
		builder.setDescription(sheet.getTeam().toString());
		channel.sendMessage(builder.build()).complete();
	}

	private void sendCategory(MessageChannel channel, EvaluationCategory category) {
		MessageBuilder builder = new MessageBuilder();
		builder.append(category.getTitle(), MessageBuilder.Formatting.BOLD);
		builder.append("\n");
		builder.append(category.getDescription());
		builder.append("\n");
		for (int i = 0; i < category.getEntries().length; i++) {
			for (int j = 0; j < category.getEntries()[i].getOptions().length; j++) {
				builder.append(indexToReactionEmoji(i, j));
				builder.append(" ");
				builder.append(freitext(category.getEntries()[i].getOptions()[j]));
				builder.append(" ");
			}
			builder.append("\n");
			builder.append(category.getEntries()[i].getTitle());
			builder.append("\n");
		}
		channel.sendMessage(builder.build()).queue(message -> addVotingReactions(category, message));
	}

	private void addVotingReactions(EvaluationCategory category, Message message) {
		addVotingReaction(category, message, 0, 0);
	}

	private void addVotingReaction(EvaluationCategory category, Message message, int i, int j) {
		Pair<Integer, Integer> nextIndex = nextIndex(category, i, j);
		if (nextIndex == null)
			message.addReaction(indexToReactionEmoji(i, j)).queue();
		else
			message.addReaction(indexToReactionEmoji(i, j)).queue(v ->
					addVotingReaction(category, message, nextIndex.getLeft(), nextIndex.getRight()));
	}

	private Pair<Integer, Integer> nextIndex(EvaluationCategory category, int i, int j) {
		if (j < category.getEntries()[i].getOptions().length - 1) {
			return Pair.of(i, j + 1);
		} else if (i < category.getEntries().length - 1)
			return Pair.of(i + 1, 0);
		return null;
	}

	private String indexToReactionEmoji(int i, int j) {
		if (i == 0) {
			return (j + 1) + "\u20E3";
		} else {
			return "\uD83C" + ((char) ('\uDDE6' + j));
		}
	}

	private String freitext(String text) {
		return text == null ? "Freitext" : text;
	}
}
