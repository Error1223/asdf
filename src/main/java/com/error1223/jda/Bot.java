package com.error1223.jda;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(
                // bot token
                Config.get("token"), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES)
                .disableCache(EnumSet.of(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE));

        builder.addEventListeners(new Listener());
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);

        // Set activity (like "playing Something")
        builder.setActivity(Activity.playing(Config.get("prefix") + "help"));

        builder.build();
    }
}
