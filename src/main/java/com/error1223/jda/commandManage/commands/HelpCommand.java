package com.error1223.jda.commandManage.commands;

import com.error1223.jda.CommandManager;
import com.error1223.jda.Config;
import com.error1223.jda.commandManage.CommandContext;
import com.error1223.jda.commandManage.ICommand;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ICommand {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    public void handle(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder();
            StringBuilder builder = new StringBuilder();
            this.manager.getCommands().stream().map(ICommand::getName).forEach((it) -> builder.append('`').append(Config.get("prefix")).append(it).append("`\n"));

            info.setColor(16777215);
            info.setTitle("List of commands\n");
            info.setDescription(builder.toString());
            channel.sendMessage(info.build()).queue();
        }

        String search = args.get(0);
        ICommand command = this.manager.getCommand(search);

        if (command == null) {

            channel.sendMessage("Nothing found for " + search).queue();

        } else {

            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(Config.get("prefix") + command.getName());
            info.setDescription(command.getHelp());
            channel.sendMessage(info.build()).queue();
            info.clear();
        }
    }

    public String getName() {
        return "help";
    }

    public String getHelp() {
        return "Shows the list with commands in the bot\nUsage: !help `null or <command>`";
    }

    public String getUsage() {
        return "!help null or <command>";
    }

    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
