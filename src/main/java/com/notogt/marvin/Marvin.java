package com.notogt.marvin;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Marvin {

    public static void main(String[] args) throws LoginException, IOException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken("NDE1MzA5Njg2NjY1OTA0MTI4.DW1L1A.ZlEpCBmY-WnVH6qJ3PyEV8AE15Y").buildAsync();

        jda.addEventListener(new RemCommand(jda));

        Iterator<Command> iterator = getCommandList().iterator();

        jda.addEventListener();
        Command command;
        while (iterator.hasNext()) {
            command = iterator.next();
            jda.addEventListener(new NotoListener(command.call, command.response));
        }
    }

    public static List<Command> getCommandList() throws IOException {
        InputStream in = new FileInputStream(new File("commands.json"));

        String commandJson = CharStreams.toString(new InputStreamReader(in));

        in.close();

        Type commandListType = new TypeToken<ArrayList<Command>>(){}.getType();

        return new Gson().fromJson(commandJson, commandListType);
    }
}
