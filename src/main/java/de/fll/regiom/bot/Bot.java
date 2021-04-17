package de.fll.regiom.bot;

import de.fll.regiom.io.TextImporter;
import de.fll.regiom.listeners.ListenerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Bot {

	public static void main(String[] args) {
		String token = TextImporter.readFile(new File("token.txt"));
		while (token != null) {
			try {
				JDA jda = JDABuilder.createDefault(token)
						.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES)
						.addEventListeners(new ReadyListener())
						.setActivity(Activity.playing("Eating Pizza")).build();
				token = null;
			} catch (LoginException ignore) {
				System.out.println("Enter Bot Token: ");
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
					token = reader.readLine();
				} catch (IOException e) {
					return;
				}
			}
		}
	}

	private static final class ReadyListener extends ListenerAdapter {

		@Override
		public void onReady(@NotNull ReadyEvent event) {
			ListenerManager listenerManager = new ListenerManager(event.getJDA());
			event.getJDA().addEventListener(listenerManager.createAllEventListeners());
		}
	}
}
