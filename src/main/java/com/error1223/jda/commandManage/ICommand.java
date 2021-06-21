package com.error1223.jda.commandManage;

import java.util.List;

public interface ICommand {

    //command function
    void handle(CommandContext ctx);

    //command name
    String getName();

    //help command description
    //NOT FIXED PREFIX YET, connect the code to .env file(Config.get("prefix"))
    String getHelp();

    //error usage
    //NOT FIXED PREFIX YET, connect the code to .env file(Config.get("prefix"))
    String getUsage();

    //Aliases, still works as command
    default List<String> getAliases() {
        return List.of();
    }
}
