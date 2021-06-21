package com.error1223.jda.commandManage.commands;

import com.error1223.jda.commandManage.CommandContext;
import com.error1223.jda.commandManage.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Random;
public class RollCommand implements ICommand {

    public RollCommand(){
    }
    public void handle(CommandContext ctx){

        EmbedBuilder info = new EmbedBuilder();
        MessageChannel channel = ctx.getChannel();
        Random rand = new Random();
        int roll = rand.nextInt(6) + 1; //This results in 1 - 6 (instead of 0 - 5)

        //result interface
        info.setTitle("Your roll: `"+ roll+"`");
        info.setColor(0x00ff99);
        switch (roll) {
            case 1 -> info.setDescription("wasn't very good... Must be bad luck!");
            case 2 -> info.setDescription("not that good, try again");
            case 3 -> info.setDescription("decent, but can be improved");
            case 4 -> info.setDescription("good, little above average");
            case 5 -> info.setDescription("great, you have some luck today!");
            case 6 -> info.setDescription("perfect, lucky day!");
            default -> throw new IllegalArgumentException();
        }
        channel.sendMessage(info.build()).queue();
        info.clear();
    }

    public String getName(){
        return "roll";
    }
    public String getHelp(){
        return "randomly rolls a dice(1-6)\nUsage: `!roll`";
    }
    public String getUsage(){
        return "!roll";
    }

}
