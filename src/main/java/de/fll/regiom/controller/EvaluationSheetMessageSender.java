package de.fll.regiom.controller;

import de.fll.regiom.model.evaluation.EvaluationCategory;
import de.fll.regiom.model.evaluation.EvaluationSheet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.CompletableFuture;

public class EvaluationSheetMessageSender {

    public void sendSheet(TextChannel channel, EvaluationSheet sheet) {
        if (channel == null || sheet == null)
            return;
        sendTitle(channel, sheet);
        for (EvaluationCategory category : sheet.getCategories()) {
            sendCategory(channel, category);
        }
    }

    private void sendTitle(TextChannel channel, EvaluationSheet sheet) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(sheet.getTitle());
        builder.setDescription("Hier kommen noch Teaminformationen hin");
        channel.sendMessage(builder.build()).complete();
    }

    private void sendCategory(TextChannel channel, EvaluationCategory category) {
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
        CompletableFuture<Message> message = channel.sendMessage(builder.build()).submit();
        for (int i = 1; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
                int finalI = i;
                int finalJ = j;
                message.thenAccept(msg -> msg.addReaction(indexToReactionEmoji(finalI, finalJ)).submit());
            }
        }

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