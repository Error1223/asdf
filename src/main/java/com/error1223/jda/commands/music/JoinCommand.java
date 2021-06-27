package com.error1223.jda.commands.music;

import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final VoiceChannel connectedChannel = ctx.getMember().getVoiceState().getChannel();

        //check if connected
        if(connectedChannel == null){
            channel.sendMessageFormat("You are not connect to a voice channel!").queue();
            return;
        }
        long start = System.currentTimeMillis();
        // Gets the audio manager.
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        // Connects to the channel.
        audioManager.openAudioConnection(connectedChannel);

        channel.sendMessage("Connected to the voice channel!").queue();

    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Bot joins voice chat";
    }

    @Override
    public String getUsage() {
        return "`!join`";
    }
}
