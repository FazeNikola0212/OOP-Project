package TicketCommands;

import FileCommands.FileManager;
import Interfaces.TicketCommands;
import Models.Event;
import Models.Hall;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ticketService implements TicketCommands {
    private FileManager fileManager;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ticketService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void addEvent(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        String[] parts = data.split("\\s+", 3);
        if (parts.length < 3) {
            System.out.println("Invalid input format. Expected: <date> <hall> <event name>");
            return;
        }

        String dateString = parts[0];
        String hallName = parts[1];
        String eventName = parts[2];

        LocalDate eventDate;
        try {
            eventDate = LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use dd-MM-yyyy.");
            return;
        }

        Hall hall = fileManager.findHallByName(hallName);
        if (hall == null) {
            System.out.println("Hall '" + hallName + "' not found.");
            return;
        }

        if (eventExists(hall, eventDate)) {
            System.out.println("Error: Another event is already scheduled in '" + hallName + "' on " + eventDate);
            return;
        }
        Event event = new Event(eventName, eventDate, hall);
        fileManager.eventObjects.add(event);
        String[] eventData = {dateString, hallName, eventName};
        fileManager.eventsList.add(eventData);
        System.out.println("Event '" + eventName + "' added successfully in '" + hallName + "' on " + dateString);
    }

    private boolean eventExists(Hall hall, LocalDate date) {
        return fileManager.eventsList.stream()
                .anyMatch(e -> e[0].equals(date.format(DATE_FORMATTER)) && e[1].equalsIgnoreCase(hall.getName()));
    }

    @Override
    public void freeSeats(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        String[] parts = data.split(" ", 2);
        if (parts.length < 2) {
            System.out.println("Invalid input format. Expected: <date> <event name>");
            return;
        }

        String dateString = parts[0].trim();
        String eventName = parts[1].trim();

        LocalDate eventDate;
        try {
            eventDate = LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use dd-MM-yyyy.");
            return;
        }

        Optional<String[]> optionalEvent = fileManager.eventsList.stream()
                .filter(e -> e.length > 2 && e[0].equals(dateString) && e[2].equalsIgnoreCase(eventName))
                .findFirst();

        if (optionalEvent.isEmpty()) {
            System.out.println("No event found with name '" + eventName + "' on " + dateString);
            return;
        }

        String hallName = optionalEvent.get()[1];

        Hall hall = fileManager.findHallByName(hallName);
        if (hall == null) {
            System.out.println("Error: Hall '" + hallName + "' not found.");
            return;
        }

        int totalSeats = hall.getCapacity();

        int bookedSeats = (int) fileManager.eventsList.stream()
                .filter(e -> e.length > 4 && e[2].equals(dateString) && e[3].equalsIgnoreCase(eventName)
                        && (e[4].equals("BOOKED") || e[4].equals("BOUGHT")))
                .count();

        int availableSeats = totalSeats - bookedSeats;

        System.out.println("Available seats for '" + eventName + "' on " + dateString + ": " + availableSeats);
    }


    private int getBookedSeats(String date, String eventName, String hallName) {
        return (int) fileManager.eventsList.stream()
                .filter(e -> e[0].equals(date) && e[2].equalsIgnoreCase(eventName)
                        && e[1].equalsIgnoreCase(hallName)
                        && e.length > 3)
                .count();
    }

    @Override
    public void book(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        try {
            String[] parts = data.split(" ", 5);
            if (parts.length < 5) {
                System.out.println("Invalid format. Expected: <row> <seat> <date> <name> <note>");
                return;
            }

            String row = parts[0];
            String seat = parts[1];
            String date = parts[2];
            String eventName = parts[3];
            String note = parts[4];

            Optional<String[]> optionalEvent = fileManager.eventsList.stream()
                    .filter(e -> e[0].equals(date) && e[2].equalsIgnoreCase(eventName))
                    .findFirst();

            if (optionalEvent.isEmpty()) {
                System.out.println("No event found with name '" + eventName + "' on " + date);
                return;
            }

            String hallName = optionalEvent.get()[1];

            boolean seatTaken = fileManager.eventsList.stream()
                    .anyMatch(e -> e.length > 5 && e[0].equals(row) && e[1].equals(seat)
                            && e[2].equals(date) && e[3].equalsIgnoreCase(eventName)
                            && (e[4].equals("BOOKED") || e[4].equals("BOUGHT")));

            if (seatTaken) {
                System.out.println("Seat " + row + "-" + seat + " is already taken for '" + eventName + "' on " + date);
                return;
            }

            String[] bookingEntry = {row, seat, date, eventName, "BOOKED", note,hallName};
            fileManager.eventsList.add(bookingEntry);
            System.out.println("Successfully booked seat " + row + "-" + seat + " for '" + eventName + "' on " + date);

        } catch (Exception e) {
            System.out.println("An error occurred while booking: " + e.getMessage());
        }
    }


    @Override
    public void unbook(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        try {
            String[] parts = data.split(" ", 4);
            if (parts.length < 4) {
                System.out.println("Invalid format. Expected: <row> <seat> <date> <name>");
                return;
            }

            String row = parts[0];
            String seat = parts[1];
            String date = parts[2];
            String eventName = parts[3];

            Optional<String[]> optionalBooking = fileManager.eventsList.stream()
                    .filter(e -> e.length > 4 && e[0].equals(row) && e[1].equals(seat) &&
                            e[2].equals(date) && e[3].equalsIgnoreCase(eventName) &&
                            e[4].equalsIgnoreCase("BOOKED"))
                    .findFirst();

            if (optionalBooking.isEmpty()) {
                System.out.println("No booked (unpaid) ticket found for seat " + row + "-" + seat +
                        " on '" + eventName + "' on " + date);
                return;
            }

            fileManager.eventsList.remove(optionalBooking.get());

            System.out.println("Successfully unbooked seat " + row + "-" + seat + " for '" + eventName + "' on " + date);

        } catch (Exception e) {
            System.out.println("An error occurred while unbooking: " + e.getMessage());
        }
    }

    @Override
    public void buy(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        try {
            String[] parts = data.split(" ", 4);
            if (parts.length < 4) {
                System.out.println("Invalid format. Expected: <row> <seat> <date> <event>");
                return;
            }

            String row = parts[0];
            String seat = parts[1];
            String date = parts[2];
            String eventName = parts[3];

            String hall = null;

            for (String[] event : fileManager.eventsList) {
                if (event.length >= 6 && event[3].equalsIgnoreCase(eventName)) {
                    hall = event[5];
                    break;
                }
            }

            if (hall == null) {
                System.out.println("Event '" + eventName + "' not found. Cannot determine hall.");
                return;
            }

            for (int i = 0; i < fileManager.eventsList.size(); i++) {
                String[] booking = fileManager.eventsList.get(i);

                if (booking.length >= 6 &&
                        booking[0].equals(row) &&
                        booking[1].equals(seat) &&
                        booking[2].equals(date) &&
                        booking[3].equalsIgnoreCase(eventName) &&
                        booking[5].equalsIgnoreCase(hall) &&
                        booking[4].equalsIgnoreCase("BOOKED")) {

                    String ticketCode = generateTicketCode(row, seat, date, eventName);
                    fileManager.eventsList.set(i, new String[]{row, seat, date, eventName, "BOUGHT", ticketCode,hall});

                    System.out.println("Successfully bought ticket for seat " + row + "-" + seat +
                            " in hall '" + hall + "' on '" + eventName + "' on " + date);
                    System.out.println("Ticket Code: " + ticketCode);

                    return;
                }
            }

            System.out.println("No booked ticket found for seat " + row + "-" + seat +
                    " in hall '" + hall + "' on '" + eventName + "' on " + date);
        } catch (Exception e) {
            System.out.println("An error occurred while buying the ticket: " + e.getMessage());
        }
    }

    private String generateTicketCode(String row, String seat, String date, String eventName) {
        String rawData = row + "-" + seat + "-" + date + "-" + eventName;
        return Integer.toHexString(rawData.hashCode()).toUpperCase();
    }

    @Override
    public void bookings(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        String dateFilter;
        String eventNameFilter;

        String[] parts = data.split(" ", 2);
        if (parts.length == 2) {
            dateFilter = parts[0].trim();
            eventNameFilter = parts[1].trim();
        } else if (parts.length == 1) {
            if (parts[0].matches("\\d{2}-\\d{2}-\\d{4}")) {
                eventNameFilter = "";
                dateFilter = parts[0].trim();
            } else {
                dateFilter = "";
                eventNameFilter = parts[0].trim();
            }
        } else {
            eventNameFilter = "";
            dateFilter = "";
        }

        List<String[]> bookedTickets = fileManager.eventsList.stream()
                .filter(e -> e.length >= 5 && e[4].equalsIgnoreCase("BOOKED"))
                .filter(e -> dateFilter.isEmpty() || e[2].equals(dateFilter))
                .filter(e -> eventNameFilter.isEmpty() || e[3].equalsIgnoreCase(eventNameFilter))
                .collect(Collectors.toList());

        if (bookedTickets.isEmpty()) {
            System.out.println("No booked (unpaid) tickets found" +
                    (!dateFilter.isEmpty() ? " for " + dateFilter : "") +
                    (!eventNameFilter.isEmpty() ? " on '" + eventNameFilter + "'" : "") + ".");
            return;
        }

        System.out.println("Booked (unpaid) tickets:");
        for (String[] ticket : bookedTickets) {
            System.out.println("Date: " + ticket[2] + ", Event: " + ticket[3] +
                    ", Row: " + ticket[0] + ", Seat: " + ticket[1] +
                    ", Note: " + (ticket.length > 5 ? ticket[5] : "N/A"));
        }
    }


    @Override
    public void check(String ticketCode) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        Optional<String[]> optionalTicket = fileManager.eventsList.stream()
                .filter(e -> e.length > 6 && e[5].equals(ticketCode))
                .findFirst();

        if (optionalTicket.isEmpty()) {
            System.out.println("Invalid ticket code.");
            return;
        }

        String[] ticketData = optionalTicket.get();
        String row = ticketData[0];
        String seat = ticketData[1];
        String date = ticketData[2];
        String eventName = ticketData[3];
        String hall = ticketData[5];

        System.out.println("Valid ticket! Seat: " + row + "-" + seat +
                " in hall '" + hall + "' for event '" + eventName + "' on " + date);
    }

    @Override
    public void report(String data) {
        if (!fileManager.isFileOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        String[] parts = data.split(" ", 3);
        if (parts.length < 2) {
            System.out.println("Invalid format. Expected: <from> <to> [<hall>]");
            return;
        }

        String fromDateStr = parts[0].trim();
        String toDateStr = parts[1].trim();
        String hallFilter = (parts.length == 3) ? parts[2].trim() : null;

        LocalDate fromDate, toDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][d-M-yyyy]");
            fromDate = LocalDate.parse(fromDateStr, formatter);
            toDate = LocalDate.parse(toDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use dd-MM-yyyy.");
            return;
        }

        Map<String, Map<String, Integer>> hallEventSales = new LinkedHashMap<>();
        boolean foundTickets = false;

        for (String[] entry : fileManager.eventsList) {
            if (entry.length < 6 || !entry[4].equalsIgnoreCase("BOUGHT")) {
                continue;
            }

            String eventDateStr = entry[2];
            String eventName = entry[3];
            String hallName = entry[1];
            String ticketCode = entry[5];

            LocalDate eventDate;
            try {
                eventDate = LocalDate.parse(eventDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                continue;
            }

            if (eventDate.isBefore(fromDate) || eventDate.isAfter(toDate)) {
                continue;
            }

            if (hallFilter != null && !hallName.equalsIgnoreCase(hallFilter)) {
                continue;
            }

            hallEventSales.putIfAbsent(hallName, new LinkedHashMap<>());
            hallEventSales.get(hallName).put(eventName, hallEventSales.get(hallName).getOrDefault(eventName, 0) + 1);
            foundTickets = true;
        }

        if (!foundTickets) {
            System.out.println("No sold tickets found in the given period.");
            return;
        }

        System.out.println("Report from " + fromDateStr + " to " + toDateStr + (hallFilter != null ? " for hall: " + hallFilter : ""));
        for (Map.Entry<String, Map<String, Integer>> hallEntry : hallEventSales.entrySet()) {
            System.out.println("Hall: " + hallEntry.getKey());
            for (Map.Entry<String, Integer> eventEntry : hallEntry.getValue().entrySet()) {
                System.out.println("  Event: " + eventEntry.getKey() + " | Sold Tickets: " + eventEntry.getValue());
            }
        }
    }
}
