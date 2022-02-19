package de.fll.regiom.commands;

import de.fll.regiom.model.Constants;
import de.fll.regiom.music.MusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayMusicCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		VoiceChannel channel = event.getGuild().getVoiceChannelById(Constants.AUDITORIUM_CHANNEL);
		if (channel == null)
			return false;
		var audioManager = event.getGuild().getAudioManager();
		if (!audioManager.isConnected()) {
			audioManager.openAudioConnection(channel);
			audioManager.setSendingHandler(MusicManager.INSTANCE.getSendHandler());
		}
		String link = event.getMessage().getContentDisplay().substring("!play".length()).strip();
		System.out.println(link);
		MusicManager.INSTANCE.loadAndPlay(link);
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith("play");
	}

	@Override
	public @Nullable String getInfo() {
		return null;
	}
}
