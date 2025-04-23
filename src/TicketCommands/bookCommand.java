package TicketCommands;

import Interfaces.ExecuteCommands;

public class bookCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public bookCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.book(data);
    }
}
