package TicketCommands;

import Interfaces.ExecuteCommands;

public class addEventCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public addEventCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.addEvent(data);
    }
}
