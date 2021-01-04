package de.fll.regiom.listeners;

import de.fll.regiom.controller.EvaluationSheetMessageSender;
import de.fll.regiom.model.evaluation.RobotDesignEvaluationSheet;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChatCommandListener extends ListenerAdapter {

	private final EvaluationSheetMessageSender sheetMessageSender = new EvaluationSheetMessageSender();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        } else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                    event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }
        //just for testing

        if (event.getMessage().getContentRaw().equals("!robotdesign")) {
            sheetMessageSender.sendSheet(event.getTextChannel(), new RobotDesignEvaluationSheet(null));
        }
		if (event.getMessage().getContentRaw().equals("!research")) {
			sheetMessageSender.sendSheet(event.getTextChannel(), new RobotDesignEvaluationSheet(null));
		}
    }
}
