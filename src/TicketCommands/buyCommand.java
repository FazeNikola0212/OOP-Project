package TicketCommands;

import Interfaces.ExecuteCommands;

public class buyCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public buyCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.buy(data);
    }
}
