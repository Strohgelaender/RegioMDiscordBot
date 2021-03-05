package de.fll.regiom.controller;

import de.fll.regiom.model.evaluation.EvaluationCategory;
import de.fll.regiom.model.evaluation.EvaluationSheet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvaluationSheetMessageSender {

	public void sendSheet(@NotNull MessageChannel channel, @NotNull EvaluationSheet sheet) {
		sendTitle(channel, sheet);
		for (EvaluationCategory category : sheet.getCategories()) {
			sendCategory(channel, category);
		}
	}

	private void sendTitle(@NotNull MessageChannel channel, @NotNull EvaluationSheet sheet) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(sheet.getTitle());
		builder.setDescription(sheet.getTeam().toString());
		channel.sendMessage(builder.build()).complete();
	}

	private void sendCategory(@NotNull MessageChannel channel, @NotNull EvaluationCategory category) {
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

	private void addVotingReactions(@NotNull EvaluationCategory category, @NotNull Message message) {
		addVotingReaction(category, message, 0, 0);
	}

	private void addVotingReaction(@NotNull EvaluationCategory category, @NotNull Message message, int i, int j) {
		Pair<Integer, Integer> nextIndex = nextIndex(category, i, j);
		if (nextIndex == null)
			message.addReaction(indexToReactionEmoji(i, j)).queue();
		else
			message.addReaction(indexToReactionEmoji(i, j)).queue(v ->
					addVotingReaction(category, message, nextIndex.getLeft(), nextIndex.getRight()));
	}

	private Pair<Integer, Integer> nextIndex(@NotNull EvaluationCategory category, int i, int j) {
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

	@NotNull
	private String freitext(@Nullable String text) {
		return text == null ? "Freitext" : text;
	}
}
