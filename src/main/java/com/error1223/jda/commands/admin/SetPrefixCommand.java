package com.error1223.jda.commands.admin;

import com.error1223.jda.Config;
import com.error1223.jda.PrefixManage;
import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if(!(member.hasPermission(Permission.MANAGE_PERMISSIONS))){
            channel.sendMessage("You must have the `Manage Server` permission to use this command").queue();
            return;
        }

        final long guildId = ctx.getGuild().getIdLong();

        if(args.isEmpty()){
            EmbedBuilder info = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("Current prefix: `"+ PrefixManage.PREFIXES.computeIfAbsent(guildId, (id) -> Config.get("prefix")) + "`");
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        final String newPrefix = String.join("", args);
        PrefixManage.PREFIXES.put(guildId, newPrefix);

        EmbedBuilder info = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Successfully changed prefix to: `"+ newPrefix + "`");
        channel.sendMessage(info.build()).queue();
        info.clear();
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for the entire server";
    }

    @Override
    public String getUsage() {
        return "`!prefix <new prefix>`";
    }
}
