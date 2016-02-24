package de.donxs.minecraftmarket.json;

import java.util.Collection;
import lombok.Data;


@Data
public class PendingResult {

    private int id;
    private String username;
    private Collection<PendingCommand> commands;

}
