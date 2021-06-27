package com.error1223.jda;

import java.awt.*;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import com.error1223.jda.commands.music.PlayCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        WebUtils.setUserAgent("BruhBot/Error1223#8586");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                .setColor(Color.CYAN)
                .setFooter("from Reddit")
        );

        JDA jda = JDABuilder.createDefault(Config.get("token"), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES)
                .disableCache(EnumSet.of(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)) // slash commands don't need any intents
                .addEventListeners(new Listener())
                .addEventListeners(new PlayCommand())
                .setBulkDeleteSplittingEnabled(false)
                .setRawEventsEnabled(true)
                .setActivity(Activity.streaming("!help", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
                .build();
    }

}
