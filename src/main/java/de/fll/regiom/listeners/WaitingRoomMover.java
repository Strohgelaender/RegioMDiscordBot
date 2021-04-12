package de.fll.regiom.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class WaitingRoomMover extends ListenerAdapter {

	private static final Map<Long, Long> WAITING_ROOM_MAP =
			Map.of(830514879919685652L, 830514021516967976L, 831098421351022603L, 795634259091390504L);
	private static final Map<Long, Boolean> MUTE_MAP = Map.of(830514879919685652L, true, 831098421351022603L, false);

	@Override
	public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
		System.out.println("Update: " + event);
		if (event.getChannelJoined() == null)
			return;
		long joinID = event.getChannelJoined().getIdLong();
		if (WAITING_ROOM_MAP.containsKey(joinID)) {
			moveToRoom(event.getEntity(), WAITING_ROOM_MAP.get(joinID));
			return;
		}
		if (event.getChannelLeft() == null) {
			unmute(event.getEntity());
			return;
		}
		long leaveID = event.getChannelLeft().getIdLong();
		if (WAITING_ROOM_MAP.containsValue(joinID) && WAITING_ROOM_MAP.containsKey(leaveID)
				&& WAITING_ROOM_MAP.get(leaveID) == joinID) {
			setMuted(event.getEntity(), MUTE_MAP.getOrDefault(leaveID, false));
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

	private static void moveToRoom(Member member, long room) {
		Guild guild = member.getGuild();
		guild.moveVoiceMember(member, guild.getVoiceChannelById(room)).queue();
	}

}
