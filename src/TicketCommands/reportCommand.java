package TicketCommands;

import Interfaces.ExecuteCommands;

public class reportCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public reportCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.report(data);
    }
}
