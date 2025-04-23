package TicketCommands;

import Interfaces.ExecuteCommands;

public class freeseatsCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public freeseatsCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.freeSeats(data);
    }
}
