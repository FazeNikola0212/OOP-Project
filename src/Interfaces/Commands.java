package Interfaces;

public interface Commands {
    void open(String path);
    void close();
    void save();
    void saveAs(String newPath);
    void help();
    void exit();
    void load();
}
