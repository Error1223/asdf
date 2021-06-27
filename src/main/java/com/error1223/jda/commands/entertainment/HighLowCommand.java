package com.error1223.jda.commands.entertainment;

import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class HighLowCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        Random rand = new Random();
        final TextChannel channel = ctx.getChannel();
        final User author = ctx.getAuthor();
        final String search = args.get(0);

        int num = rand.nextInt(100);
        EmbedBuilder info = new EmbedBuilder();

        if((num > 50 && search.equals("high"))||(num < 50 && search.equals("low"))){
            info.setColor(Color.GREEN);
            info.setTitle("Number was: " + num);
            info.setDescription(author.getAsMention() + " you were correct!");
            channel.sendMessage(info.build()).queue();
            info.clear();
        }else if((num > 50 && search.equals("low"))||(num < 50 && search.equals("high"))){
            info.setColor(Color.RED);
            info.setTitle("Number was: " + num);
            info.setDescription(author.getAsMention() + " incorrect, try again...");
            channel.sendMessage(info.build()).queue();
            info.clear();

        }else{
            throw new IllegalArgumentException();
        }

    }

    @Override
    public String getName() {
        return "highlow";
    }

    @Override
    public String getHelp() {
        return "Guessing if a number is high or low(1-100)";
    }

    @Override
    public String getUsage() {
        return "`!highlow <high or low>`";
    }
}
