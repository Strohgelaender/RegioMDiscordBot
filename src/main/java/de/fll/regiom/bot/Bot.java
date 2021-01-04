package de.fll.regiom.bot;

import de.fll.regiom.listeners.ListenerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {

    public static void main(String[] args) throws LoginException {
        ListenerManager listenerManager = new ListenerManager();
        JDA jda = JDABuilder.createDefault("")
                .addEventListeners(listenerManager.createAllEventListeners())
                .setActivity(Activity.playing("Eating Pizza")).build();

    }
}
