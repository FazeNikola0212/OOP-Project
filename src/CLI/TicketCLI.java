package CLI;

import FileCommands.*;
import TicketCommands.*;
import Interfaces.ExecuteCommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TicketCLI {

    public static void start(){
        Scanner scanner = new Scanner(System.in);

        Map<String, ExecuteCommands> instructions = new HashMap<>();
        FileManager fileManager = new FileManager();
        ticketService ticketManager = new ticketService(fileManager);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String commandName = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1] : "";

            instructions.put("open", new OpenCommand(fileManager, argument));
            instructions.put("close", new CloseCommand(fileManager));
            instructions.put("save", new SaveCommand(fileManager));
            instructions.put("saveas", new SaveAsCommand(fileManager, argument));
            instructions.put("help", new HelpCommand(fileManager));
            instructions.put("exit", new ExitCommand(fileManager));
            instructions.put("addevent", new addEventCommand(ticketManager,argument));
            instructions.put("freeseats", new freeseatsCommand(ticketManager,argument));
            instructions.put("book", new bookCommand(ticketManager,argument));
            instructions.put("unbook", new unbookCommand(ticketManager,argument));
            instructions.put("buy", new buyCommand(ticketManager,argument));
            instructions.put("bookings", new bookingsCommand(ticketManager,argument));
            instructions.put("check", new checkCommand(ticketManager,argument));
            instructions.put("report", new reportCommand(ticketManager,argument));

            ExecuteCommands command = instructions.get(commandName);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

}
