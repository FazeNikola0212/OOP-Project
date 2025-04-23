package TicketCommands;

import Interfaces.ExecuteCommands;

public class unbookCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public unbookCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.unbook(data);
    }
}
