package de.donxs.minecraftmarket.json;

import lombok.Data;


@Data
public class PendingCommand {

    private int id;
    private int delay;
    private int repeatcycles;
    private boolean repeat;
    private String command;
    private int repeatperiod;
    private int online;
    private int slots;

}
