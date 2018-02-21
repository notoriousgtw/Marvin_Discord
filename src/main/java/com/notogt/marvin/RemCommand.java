package com.notogt.marvin;

import com.google.gson.Gson;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static com.notogt.marvin.Marvin.getCommandList;

public class RemCommand extends ListenerAdapter {
    JDA jda;

    public RemCommand(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();

        List<Command> commandList = null;
        try {
            commandList = getCommandList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String call;
        String response;

        if (content.startsWith("!rem")) {
            String[] contentArray = content.split(" ");
            call = contentArray[1];
            response = contentArray[2];

            Command command = new Command(call, response);
            commandList.add(command);
            jda.addEventListener(new NotoListener(command.call, command.response));

            try {
                Files.delete(new File("commands.json").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintStream out = new PrintStream(new FileOutputStream("commands.json"))) {
                out.print(new Gson().toJson(commandList));
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String emoji = "👌";
            event.getMessage().addReaction(emoji).queue();
        }
    }
}
