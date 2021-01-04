package de.fll.regiom.bot;

import de.fll.regiom.listeners.ListenerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Bot {

	public static void main(String[] args) throws LoginException {
		JDA jda = JDABuilder.createDefault("")
				.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES)
				.addEventListeners(new ReadyListener())
				.setActivity(Activity.playing("Eating Pizza")).build();
	}

	private static final class ReadyListener extends ListenerAdapter {
		@Override
		public void onReady(@NotNull ReadyEvent event) {
			ListenerManager listenerManager = new ListenerManager(event.getJDA());
			event.getJDA().addEventListener(listenerManager.createAllEventListeners());
		}
	}
}
