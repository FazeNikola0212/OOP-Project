package TicketCommands;

import Interfaces.ExecuteCommands;

public class bookingsCommand implements ExecuteCommands {
    private ticketService ticketManager;
    private String data;

    public bookingsCommand(ticketService ticketManager, String data) {
        this.ticketManager = ticketManager;
        this.data=data;
    }

    @Override
    public void execute() {
        ticketManager.bookings(data);
    }
}
