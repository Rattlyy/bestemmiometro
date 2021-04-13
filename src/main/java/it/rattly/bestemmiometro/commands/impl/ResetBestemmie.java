package it.rattly.bestemmiometro.commands.impl;

import it.rattly.bestemmiometro.Bestemmiometro;
import it.rattly.bestemmiometro.commands.Command;
import it.rattly.bestemmiometro.commands.Help;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

@Help("Cancella tutte le bestemmie di un utente")
public class ResetBestemmie extends Command {
    public ResetBestemmie(String name) {
        super(name);
    }

    @Override
    public void execute(Message message) {
        Member member = message.getGuild().getMember(message.getAuthor());

        if(member == null)
            return;

        if(!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            message.getChannel().sendMessage(new MessageEmbed(
                    "https://github.com/Rattlyy/bestemmiometro",
                    "Porco dio!",
                    "Non hai il permesso `ADMINISTRATOR`!",
                    EmbedType.RICH,
                    OffsetDateTime.now(),
                    Color.RED.getRGB(),
                    null,
                    null,
                    null,
                    null,
                    new MessageEmbed.Footer("Creato da Rattly | https://github.com/Rattlyy/bestemmiometro", "", ""),
                    null,
                    null
            )).queue();
            return;
        }

        List<User> user = message.getMentionedUsers();

        if(user.size() < 1) {
            message.getChannel().sendMessage(new MessageEmbed(
                    "https://github.com/Rattlyy/bestemmiometro",
                    "Porco dio!",
                    "Utilizzo: .resetbestemmie <@utente / @utente1 @utente2...>!",
                    EmbedType.RICH,
                    OffsetDateTime.now(),
                    Color.RED.getRGB(),
                    null,
                    null,
                    null,
                    null,
                    new MessageEmbed.Footer("Creato da Rattly | https://github.com/Rattlyy/bestemmiometro", "", ""),
                    null,
                    null
            )).queue();
            return;
        }

        for (User usr : user) {
            Bestemmiometro.getDatabase().resetBestemmie(usr.getIdLong());
        }

        message.getChannel().sendMessage(new MessageEmbed(
                "https://github.com/Rattlyy/bestemmiometro",
                "Porco dio!",
                "Bestemmie resettate.",
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
        )).queue();
    }
}
