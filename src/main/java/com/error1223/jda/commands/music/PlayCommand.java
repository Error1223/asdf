package com.error1223.jda.commands.music;

import com.error1223.jda.type.CommandContext;
import com.error1223.jda.type.ICommand;
import com.error1223.jda.commands.music.managers.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayCommand extends ListenerAdapter implements ICommand {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayCommand() {
        // creates hash map
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final User author = ctx.getAuthor();
        final TextChannel chnl = ctx.getChannel();

        if(args.isEmpty()){
            skipTrack(chnl);
        }

        loadAndPlay(chnl, "ytsearch:" + args, author.getAsTag(), author.getAvatarUrl());

    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl, String nameAsTag, String avatarURL) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + "`" + track.getInfo().title + "`").queue();

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                play(channel.getGuild(), musicManager, firstTrack);

                String uri = firstTrack.getInfo().uri;
                String thumbnail = "http://img.youtube.com/vi/" + getYouTubeId(uri) +"/0.jpg";

                EmbedBuilder info = new EmbedBuilder()
                        .setColor(Color.BLUE)
                        .setTitle(firstTrack.getInfo().title, firstTrack.getInfo().uri)
                        .setThumbnail(thumbnail)
                        .setFooter("Added by "+nameAsTag, avatarURL);

                channel.sendMessage("Adding to queue " + "`" + firstTrack.getInfo().title + "`").queue();
                channel.sendMessage(info.build()).setActionRow(Button.primary("skip", "skip")).queue();
                info.clear();
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl.replace("ytsearch:", "")).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }

        });
    }
    private void skipTrack(TextChannel channel2) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel2.getGuild());
        musicManager.scheduler.nextTrack();

        channel2.sendMessage("Skipped to next track.").queue();
    }


    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    @SuppressWarnings("deprecation")
    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
            }
        }
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public void onButtonClick(ButtonClickEvent event){
        if(event.getComponentId().equals("skip")){
            skipTrack(event.getTextChannel());
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a music from Youtube api using ytsearch";
    }

    @Override
    public String getUsage() {
        return "`!play <keyword>`";
    }
}
