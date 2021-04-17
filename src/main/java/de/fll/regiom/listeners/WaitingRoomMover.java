package de.fll.regiom.listeners;

import de.fll.regiom.commands.RoleHelper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class WaitingRoomMover extends ListenerAdapter {

	private static final Map<Long, Long> WAITING_ROOM_MAP =
			Map.of(830514879919685652L, 830514021516967976L, 831098421351022603L, 795634259091390504L);
	private static final Map<Long, Boolean> MUTE_MAP = Map.of(830514879919685652L, true, 831098421351022603L, false);

	private int counter = 0;
	private final Map<Long, Integer> namePrefix = new HashMap<>();
	private final Map<Long, String> removedNamePart = new HashMap<>();

	@Override
	public synchronized void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
		System.out.println("Update: " + event);
		if (event.getChannelJoined() == null)
			return;
		long joinID = event.getChannelJoined().getIdLong();
		if (joinID == 795608993627242506L) {
			long entityID = event.getEntity().getIdLong();
			namePrefix.computeIfAbsent(entityID, l -> counter++);
			String name = event.getEntity().getEffectiveName();
			name = namePrefix.get(entityID) + " " + name;
			event.getEntity().getGuild().modifyNickname(event.getEntity(), name.substring(0, Math.min(31, name.length()))).queue();
			if (name.length() > 32)
				removedNamePart.put(entityID, name.substring(32));
		}
		if (WAITING_ROOM_MAP.containsKey(joinID)) {
			moveToRoom(event.getEntity(), WAITING_ROOM_MAP.get(joinID));
			return;
		}
		if (event.getChannelLeft() == null) {
			unmute(event.getEntity());
			return;
		}
		long leaveID = event.getChannelLeft().getIdLong();
		if (leaveID == 795608993627242506L) {
			long userID = event.getEntity().getIdLong();
			String name = event.getEntity().getEffectiveName();
			name = name.replaceFirst(namePrefix.get(userID) + " ", "");
			name += removedNamePart.getOrDefault(userID, "");
			event.getEntity().getGuild().modifyNickname(event.getEntity(), name).queue();
		}
		if (WAITING_ROOM_MAP.containsValue(joinID) && WAITING_ROOM_MAP.containsKey(leaveID)
				&& WAITING_ROOM_MAP.get(leaveID) == joinID) {
			if (RoleHelper.isTeam(event.getEntity()))
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
