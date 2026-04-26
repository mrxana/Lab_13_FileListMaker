import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class FileListMaker {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        boolean done = false;
        boolean needsToBeSaved = false;
        String currentFileName = "";

        do {
            displayList(list);

            String menuChoice = SafeInput.getRegExString(
                    "Menu: A Add | D Delete | I Insert | M Move | O Open | S Save | C Clear | V View | Q Quit",
                    "[AaDdIiMmOoSsCcVvQq]"
            ).toUpperCase();

            try {
                switch (menuChoice) {
                    case "A":
                        String item = SafeInput.getNonZeroLenString("Enter item to add");
                        list.add(item);
                        needsToBeSaved = true;
                        break;

                    case "D":
                        if (list.isEmpty()) {
                            System.out.println("List.txt is empty. Nothing to delete.");
                        } else {
                            displayNumberedList(list);
                            int deleteIndex = SafeInput.getRangedInt("Enter item number to delete", 1, list.size());
                            list.remove(deleteIndex - 1);
                            needsToBeSaved = true;
                        }
                        break;

                    case "I":
                        String newItem = SafeInput.getNonZeroLenString("Enter item to insert");

                        if (list.isEmpty()) {
                            list.add(newItem);
                        } else {
                            displayNumberedList(list);
                            int insertIndex = SafeInput.getRangedInt("Enter position to insert item", 1, list.size() + 1);
                            list.add(insertIndex - 1, newItem);
                        }

                        needsToBeSaved = true;
                        break;

                    case "M":
                        if (list.size() < 2) {
                            System.out.println("You need at least 2 items to move something.");
                        } else {
                            displayNumberedList(list);

                            int fromIndex = SafeInput.getRangedInt("Enter item number to move", 1, list.size());
                            String movedItem = list.remove(fromIndex - 1);

                            displayNumberedList(list);
                            int toIndex = SafeInput.getRangedInt("Enter new position", 1, list.size() + 1);

                            list.add(toIndex - 1, movedItem);
                            needsToBeSaved = true;
                        }
                        break;

                    case "O":
                        if (needsToBeSaved) {
                            boolean saveFirst = SafeInput.getYNConfirm("You have unsaved changes. Save before opening another file?");
                            if (saveFirst) {
                                currentFileName = saveCurrentList(list, currentFileName);
                                needsToBeSaved = false;
                            }
                        }

                        currentFileName = SafeInput.getNonZeroLenString("Enter filename to open, without .txt");
                        currentFileName = ensureTxtExtension(currentFileName);

                        list = openFile(currentFileName);
                        needsToBeSaved = false;
                        System.out.println("File loaded: " + currentFileName);
                        break;

                    case "S":
                        currentFileName = saveCurrentList(list, currentFileName);
                        needsToBeSaved = false;
                        break;

                    case "C":
                        if (SafeInput.getYNConfirm("Are you sure you want to clear the whole list?")) {
                            list.clear();
                            needsToBeSaved = true;
                        }
                        break;

                    case "V":
                        displayNumberedList(list);
                        break;

                    case "Q":
                        if (needsToBeSaved) {
                            boolean saveBeforeQuit = SafeInput.getYNConfirm("You have unsaved changes. Save before quitting?");
                            if (saveBeforeQuit) {
                                currentFileName = saveCurrentList(list, currentFileName);
                                needsToBeSaved = false;
                            }
                        }

                        done = SafeInput.getYNConfirm("Are you sure you want to quit?");
                        break;
                }

            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }

        } while (!done);

        System.out.println("Goodbye!");
    }

    public static void displayList(ArrayList<String> list) {
        System.out.println("\nCurrent List.txt:");
        if (list.isEmpty()) {
            System.out.println("[empty]");
        } else {
            for (String item : list) {
                System.out.println("- " + item);
            }
        }
        System.out.println();
    }

    public static void displayNumberedList(ArrayList<String> list) {
        System.out.println("\nNumbered List.txt:");
        if (list.isEmpty()) {
            System.out.println("[empty]");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
        System.out.println();
    }

    public static String ensureTxtExtension(String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        return fileName;
    }

    public static String saveCurrentList(ArrayList<String> list, String currentFileName) throws IOException {
        if (currentFileName.isEmpty()) {
            currentFileName = SafeInput.getNonZeroLenString("Enter filename to save, without .txt");
            currentFileName = ensureTxtExtension(currentFileName);
        }

        saveFile(list, currentFileName);
        System.out.println("File saved as: " + currentFileName);
        return currentFileName;
    }

    public static void saveFile(ArrayList<String> list, String fileName) throws IOException {
        Path file = Paths.get(fileName);
        Files.write(file, list);
    }

    public static ArrayList<String> openFile(String fileName) throws IOException {
        Path file = Paths.get(fileName);
        return new ArrayList<>(Files.readAllLines(file));
    }
}