package it.rattly.bestemmiometro.commands.impl;

import it.rattly.bestemmiometro.Bestemmiometro;
import it.rattly.bestemmiometro.commands.Command;
import it.rattly.bestemmiometro.commands.Help;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Help("Classifica dei bestemmiatori.")
public class BestemmieTop extends Command {
    public BestemmieTop(String name) {
        super(name);
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        List<Map.Entry<Long, Integer>> list = new ArrayList<>();

        @Cleanup ResultSet rs = Bestemmiometro.getDatabase().getConnection().createStatement().executeQuery("SELECT * FROM users");

        while (rs.next()) {
            long discord_id = rs.getLong("discord_id");
            int bestemmie = rs.getInt("bestemmie");

            list.add(new Map.Entry<Long, Integer>() {
                @SneakyThrows
                @Override
                public Long getKey() {
                    return discord_id;
                }

                @Override
                @SneakyThrows
                public Integer getValue() {
                    return bestemmie;
                }

                @Override
                public Integer setValue(Integer value) {
                    return value;
                }
            });
        }

        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        AtomicInteger cursor = new AtomicInteger();
        list.removeIf(entry -> {
            cursor.addAndGet(1);
            return cursor.get() > 5;
        });

        List<MessageEmbed.Field> fields = new ArrayList<>();

        int ranking = 0;
        for (Map.Entry<Long, Integer> entry : list) {
            Member user = message.getGuild().getMemberById(entry.getKey());

            if (user == null)
                continue;

            ranking += 1;
            MessageEmbed.Field field = new MessageEmbed.Field(
                    ranking + ") " + user.getEffectiveName(),
                    entry.getValue().toString(),
                    false
            );

            fields.add(field);
        }

        message.getChannel().sendMessage(new MessageEmbed(
                "https://github.com/Rattlyy/bestemmiometro",
                "Porco dio!",
                "Ecco le classifiche!",
                EmbedType.RICH,
                OffsetDateTime.now(),
                Color.GREEN.getRGB(),
                null,
                null,
                null,
                null,
                new MessageEmbed.Footer("Creato da Rattly | https://github.com/Rattlyy/bestemmiometro", "", ""),
                null,
                fields
        )).queue();
    }
}
