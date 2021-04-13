package it.rattly.bestemmiometro.listeners;

import it.rattly.bestemmiometro.Bestemmiometro;
import it.rattly.bestemmiometro.commands.Command;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;

public class ChatListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        for (Command cmd : Bestemmiometro.commands) {
            if (event.getMessage().getContentRaw().startsWith("." + cmd.getName())) {
                cmd.execute(event.getMessage());
                return;
            }
        }

        int bestemmieCount = 0;
        boolean hasBestemmiato = false;

        for (String bestemmia : Bestemmiometro.getBestemmie()) {
            if (event.getMessage().getContentRaw().contains(bestemmia)) {
                bestemmieCount += 1;
                hasBestemmiato = true;
            }
        }

        if (hasBestemmiato) {
            Integer bestemmie = Bestemmiometro.getDatabase().getBestemmieById(event.getAuthor().getIdLong());

            if (bestemmie == null) {
                bestemmie = 0;
            }

            Bestemmiometro.getDatabase().setBestemmieOfId(event.getAuthor().getIdLong(), bestemmie + bestemmieCount);

            event.getChannel().sendMessage(
                    new MessageEmbed(
                            "https://github.com/Rattlyy/bestemmiometro",
                            "Porco dio!",
                            "Hai bestemmiato!\n\nTotale delle tue bestemmie: " + (bestemmie + bestemmieCount) + "\nTotale bestemmie del server: " + Bestemmiometro.getDatabase().getTotalBestemmie(),
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
}
