package FileCommands;

import Interfaces.Commands;
import Models.Event;
import Models.Hall;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements Commands {
    private File file;
    private boolean isFileOpen = false;
    private String filePath;

    public List<Hall> halls = new ArrayList<>();
    public List<Event> eventObjects = new ArrayList<>();
    public List<String[]> eventsList = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public FileManager() {
        seedHalls();
    }

    public boolean isFileOpen() {
        return isFileOpen;
    }

    private void seedHalls() {
        halls.add(new Hall("Main", 200,10));
        halls.add(new Hall("Conference", 100,15));
        halls.add(new Hall("Theater", 150,20));
        halls.add(new Hall("Small", 50,20));
        System.out.println("Halls initialized!");
    }

    @Override
    public void open(String path) {
        file = new File(path);
        filePath = path;

        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File not found. A new file has been created: " + file.getName());
            } else {
                System.out.println("Successfully opened file: " + file.getName());
                load();
            }
            isFileOpen = true;
        } catch (IOException e) {
            System.out.println("Error opening the file: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void close() {
        if (!isFileOpen) {
            System.out.println("No file is currently open.");
            return;
        }
        isFileOpen = false;
        System.out.println("Successfully closed " + file.getName());
    }

    @Override
    public void save() {
        if (!isFileOpen) {
            System.out.println("No file is currently open.");
            return;
        }
        System.out.println("Successfully saved " + file.getName());
        writeToFile(file.getName(), eventsList);
    }

    @Override
    public void saveAs(String newPath) {
        if (!isFileOpen) {
            System.out.println("No file is currently open.");
            return;
        }

        File newFile = new File(newPath);
        try {
            if (file.renameTo(newFile)) {
                file = newFile;
                filePath = newPath;
                System.out.println("Successfully saved " + newFile.getName());
                writeToFile(newFile.getName(), eventsList);
            } else {
                System.out.println("Error saving file.");
            }
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public void help() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file> - opens <file>");
        System.out.println("close - closes currently opened file");
        System.out.println("save - saves the currently open file");
        System.out.println("saveas <file> - saves the currently open file in <file>");
        System.out.println("help - prints this information");
        System.out.println("exit - exits the program");
    }

    @Override
    public void exit() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }

    @Override
    public void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] eventData = line.split(" ");
                eventsList.add(eventData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void writeToFile(String filename, List<String[]> event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] e : event) {
                writer.write(String.join(" ", e));
                writer.newLine();
            }
            System.out.println("Data successfully written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public Hall findHallByName(String hallName) {
        return halls.stream()
                .filter(h -> h.getName().equalsIgnoreCase(hallName))
                .findFirst()
                .orElse(null);
    }
    public Event findEventByName(String name) {
        for (Event event : eventObjects) {
            if (event.getName().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }
}
