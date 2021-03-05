package de.fll.regiom.commands;

import de.fll.regiom.controller.StorageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class SaveCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		if (StorageManager.INSTANCE.saveAll()) {
			event.getChannel().sendMessage("Data saved successfully").queue();
			return true;
		} else {
			event.getChannel().sendMessage("An error occurred while saving the data. Please check the logs.").queue();
			return false;
		}
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.equals("save");
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public String getInfo() {
		return "**save**\n\tSpeichert alle Daten. **Admin only**";
	}
}
