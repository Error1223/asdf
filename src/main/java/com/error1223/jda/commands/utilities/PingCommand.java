package com.error1223.jda.commands.utilities;

import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;

public class PingCommand implements ICommand {
    public PingCommand() {
    }

    public void handle(CommandContext ctx) {

        MessageChannel channel = ctx.getChannel();
        JDA jda = ctx.getJDA();

        EmbedBuilder info = new EmbedBuilder();
        info.setColor(32876);
        info.setTitle("Reset ping: `" + jda.getRestPing() + "`\nWS ping: `" + jda.getGatewayPing() + "ms`");
        channel.sendMessage(info.build()).queue();
        info.clear();
    }

    public String getName() {
        return "ping";
    }

    public String getHelp() {
        return "Shows the current ping from the bot to the discord servers";
    }

    public String getUsage() {
        return "`!ping`";
    }

}