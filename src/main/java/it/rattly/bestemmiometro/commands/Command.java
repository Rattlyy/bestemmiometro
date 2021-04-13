package it.rattly.bestemmiometro.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.Message;

@Data
@AllArgsConstructor
public abstract class Command {
    private String name;
    public abstract void execute(Message message);
}
