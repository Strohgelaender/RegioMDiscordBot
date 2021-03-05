package de.fll.regiom.controller;

import de.fll.regiom.model.evaluation.EvaluationCategory;
import de.fll.regiom.model.evaluation.EvaluationSheet;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EvaluationSheetMessageSender {

	@NotNull
	public CompletableFuture<?> sendSheet(@NotNull MessageChannel channel, @NotNull EvaluationSheet sheet) {
		int categoriesSize = sheet.getCategories().size();
		var allFutures = new CompletableFuture[categoriesSize + 1];
		allFutures[0] = sendTitle(channel, sheet);
		for (int i = 0; i < categoriesSize; i++) {
			EvaluationCategory category = sheet.getCategories().get(i);
			allFutures[i + 1] = sendCategory(channel, category);
		}
		return CompletableFuture.allOf(allFutures);
	}

	@NotNull
	private CompletableFuture<Message> sendTitle(@NotNull MessageChannel channel, @NotNull EvaluationSheet sheet) {
		return channel.sendMessage("**" + sheet.getTitle() + "**").submit();
	}

	@NotNull
	private CompletableFuture<?> sendCategory(@NotNull MessageChannel channel, @NotNull EvaluationCategory category) {
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
		return channel.sendMessage(builder.build()).submit()
				.thenComposeAsync(message -> addVotingReactions(category, message));
	}

	@NotNull
	private CompletableFuture<?> addVotingReactions(@NotNull EvaluationCategory category, @NotNull Message message) {
		return addVotingReaction(category, message, 0, 0);
	}

	@NotNull
	private CompletableFuture<?> addVotingReaction(@NotNull EvaluationCategory category, @NotNull Message message, int i, int j) {
		Pair<Integer, Integer> nextIndex = nextIndex(category, i, j);
		if (nextIndex == null)
			return message.addReaction(indexToReactionEmoji(i, j)).submit();
		else
			return message.addReaction(indexToReactionEmoji(i, j)).submit()
					.thenComposeAsync(v -> addVotingReaction(category, message, nextIndex.getLeft(), nextIndex.getRight()));
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
