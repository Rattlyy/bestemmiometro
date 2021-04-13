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
import java.util.ArrayList;
import java.util.List;

@Help("Lista dei comandi.")
public class HelpCommand extends Command {
    public HelpCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Message message) {
        message.getChannel().sendMessage(new MessageEmbed(
                "https://github.com/Rattlyy/bestemmiometro",
                "Lista comandi",
                "",
                EmbedType.RICH,
                OffsetDateTime.now(),
                Color.GREEN.getRGB(),
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Creato da Rattly | https://github.com/Rattlyy/bestemmiometro", "", ""),
                null,
                genCommands()
        )).queue();
    }

    @SneakyThrows
    private List<MessageEmbed.Field> genCommands() {
        List<MessageEmbed.Field> list = new ArrayList<>();

        for (Command command : Bestemmiometro.commands) {
            MessageEmbed.Field field = new MessageEmbed.Field("." + command.getName(), command.getClass().getAnnotation(Help.class).value(), false);
            list.add(field);
        }

        return list;
    }
}
