package it.rattly.bestemmiometro.commands.impl;

import it.rattly.bestemmiometro.Bestemmiometro;
import it.rattly.bestemmiometro.commands.Command;
import it.rattly.bestemmiometro.commands.Help;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.OffsetDateTime;

@Help("Numero di tutte le bestemmie registrate.")
public class BestemmieCommand extends Command {
    public BestemmieCommand(String name) {
        super(name);
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        message.getChannel().sendMessage(
                new MessageEmbed(
                        "https://github.com/Rattlyy/bestemmiometro",
                        "Porco dio!",
                        "In totale abbiamo contato circa " + Bestemmiometro.getDatabase().getTotalBestemmie() + " bestemmie!",
                        EmbedType.RICH,
                        OffsetDateTime.now(),
                        Color.GREEN.getRGB(),
                        null,
                        null,
                        null,
                        null,
                        new MessageEmbed.Footer("Creato da Rattly | https://github.com/Rattlyy/bestemmiometro", "", ""),
                        null,
                        null
                )
        ).queue();
    }
}
