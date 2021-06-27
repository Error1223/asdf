package com.error1223.jda.commands.music;

import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Guild guild = ctx.getGuild();

        // Gets the channel in which the bot is currently connected.
        VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
        // Checks if the bot is connected to a voice channel.
        if (connectedChannel == null) {
            // Get slightly fed up at the user.
            channel.sendMessage("I am not connected to a voice channel!").queue();
            return;
        }
        // Disconnect from the channel.
        guild.getAudioManager().closeAudioConnection();

        channel.sendMessage("Disconnected from the voice channel!").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Leaves voice chat";
    }

    @Override
    public String getUsage() {
        return "`!leave`";
    }
}
