package de.fll.regiom.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class WaitingRoomMover extends ListenerAdapter {

	private static final long WAITING_ROOM_ID = 830514879919685652L;
	private static final long MAIN_ROOM_ID = 830514021516967976L;


	@Override
	public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
		System.out.println("Update: " + event);
		if (event.getChannelJoined() == null)
			return;
		if (event.getChannelLeft() == null) {
			unmute(event.getEntity());
			return;
		}
		long joinID = event.getChannelJoined().getIdLong();
		if (joinID == WAITING_ROOM_ID) {
			moveToMainRoom(event.getEntity(), event.getChannelJoined().getGuild());
			return;
		}
		if (joinID == MAIN_ROOM_ID && event.getChannelLeft().getIdLong() == WAITING_ROOM_ID) {
			mute(event.getEntity());
			return;
		}
		unmute(event.getEntity());
	}

	private static void unmute(Member member) {
		setMuted(member, false);
	}

	private static void mute(Member member) {
		setMuted(member, true);
	}

	private static void setMuted(Member member, boolean isMute) {
		member.getGuild().mute(member, isMute).queue();
	}

	private static void moveToMainRoom(Member member, Guild guild) {
		guild.moveVoiceMember(member, guild.getVoiceChannelById(MAIN_ROOM_ID)).queue();
	}

}
