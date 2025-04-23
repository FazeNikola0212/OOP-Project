package TicketCommands;

import Interfaces.ExecuteCommands;

public class checkCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public checkCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.check(data);
    }
}
