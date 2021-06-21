package com.error1223.jda.commandManage.commands;

import com.error1223.jda.commandManage.CommandContext;
import com.error1223.jda.commandManage.ICommand;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.RestAction;

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
        return "Shows the current ping from the bot to the discord servers\nUsage: `!ping`";
    }

    public String getUsage() {
        return "Usage: `!ping`";
    }

}