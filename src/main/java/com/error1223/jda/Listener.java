package com.error1223.jda;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private final CommandManager manager = new CommandManager();


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        //if author = bot, does not activate
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        //.env (default prefix = !)
        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "sd")
                && user.getId().equals(Config.get("owner_id"))) {
            System.exit(0);
            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }
    }
}