package it.rattly.bestemmiometro;

import it.rattly.bestemmiometro.commands.Command;
import it.rattly.bestemmiometro.commands.impl.BestemmieCommand;
import it.rattly.bestemmiometro.commands.impl.BestemmieTop;
import it.rattly.bestemmiometro.commands.impl.HelpCommand;
import it.rattly.bestemmiometro.commands.impl.ResetBestemmie;
import it.rattly.bestemmiometro.listeners.ChatListener;
import it.rattly.bestemmiometro.utils.Database;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Bestemmiometro {
    @Getter
    private static JDA jda;
    @Getter
    private static Database database;
    @Getter
    private static List<String> bestemmie;

    public static final List<Command> commands = Arrays.asList(
            new BestemmieCommand("bestemmie"),
            new HelpCommand("help"),
            new ResetBestemmie("resetbestemmie"),
            new BestemmieTop("top")
    );

    public static void main(String[] args) {
        try {
            jda = JDABuilder
                    .createDefault(args[0])
                    .addEventListeners(new ChatListener())
                    .setActivity(Activity.watching("le tue bestemmie | https://github.com/Rattlyy"))
                    .build();

            jda.awaitReady();

            System.out.println("[BOT] Bot avviato!");
        } catch (LoginException | InterruptedException e) {
            System.out.println("[BOT] Token errata.");
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        try {
            bestemmie = Files.readAllLines(Paths.get("bestemmie.txt"));
        } catch (IOException e) {
            System.out.println("[FILE] Non sono riuscito a caricare le bestemmie!");
            e.printStackTrace();
        }

        database = new Database("data.db");
        System.out.println("[ALL] Bot avviato correttamente.");
    }
}
