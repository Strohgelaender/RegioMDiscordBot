package de.fll.regiom.listeners;

import net.dv8tion.jda.api.hooks.EventListener;

public final class ListenerManager {

    public Object[] createAllEventListeners() {
        return new EventListener[]{
                new InviteManager(),
                new ChatCommandListener()
        };
    }

}
