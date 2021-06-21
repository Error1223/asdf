package com.error1223.jda;

import com.error1223.jda.commandManage.CommandContext;
import com.error1223.jda.commandManage.ICommand;
import com.error1223.jda.commandManage.commands.HelpCommand;
import com.error1223.jda.commandManage.commands.PingCommand;
import com.error1223.jda.commandManage.commands.RollCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    EmbedBuilder error = new EmbedBuilder();


    public CommandManager() {
        //list all the commands here
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new RollCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }
        //add interface ICommand to all commands
        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    //search the command
    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        //search with lowercase
        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "").split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            try {
                List<String> args = Arrays.asList(split).subList(1, split.length);

                CommandContext ctx = new CommandContext(event, args);

                //handle method = ctx
                cmd.handle(ctx);
            } catch (Exception e) {
                //no exception, therefore good code
                cmdError(cmd.getName(), cmd.getUsage());
                event.getChannel().sendMessage(error.build()).queue();
                error.clear();
            }
        }

    }

    //error message:
    //❌Error please try again
    //Correct usage: !<cmdName> <getUsage>
    //please try again
    void cmdError(String cmdName, String usage) {
        error.setColor(0xff0000);
        error.setTitle("❌Error please try again");
        error.setDescription("Correct usage: `" + usage + "`\nplease try again");

    }
}
