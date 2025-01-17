package Duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static Duke.UserInput.filePath;
import static Duke.UserInput.userInput;
import static Duke.UserInput.inputCount;

public class Storage {
    protected static File f = new File(filePath);
    protected static File folder = new File("data");

    /**
     * Adds the tasks in the local file to Duke.
     * If local file does not exist, create one
     * @throws FileNotFoundException if file not exist
     */
    public static void initializeData() throws IOException {
        userInput = new ArrayList<>();
        if (!folder.exists())
            folder.mkdir();
        if (f.exists())
            fileReading(filePath);
    }

    /**
     * Reads the data from local file
     * @param filePath path of the local file
     * @throws FileNotFoundException if file not exist
     */
    public static void fileReading(String filePath) throws IOException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            var str = s.nextLine();
            if (str.charAt(1) == '.' && str.charAt(2) == '[' && str.charAt(4) == ']' && str.charAt(5) == ' '
                    && str.charAt(6) == '[' && str.charAt(8) == ']' && str.charAt(9) == ' '
                    && (str.charAt(3) == 'T' || str.charAt(3) == 'D' || str.charAt(3) == 'E')
                    && (str.charAt(7) == 'X' || str.charAt(7) == ' ')) {
                if (str.charAt(3) == 'T') {
                    UserInput.userInput.add(new Todo(str.substring(10)));
                    if (str.charAt(7) == 'X')
                        UserInput.userInput.get(inputCount).markAsDone();
                } else if (str.charAt(3) == 'D') {
                    UserInput.userInput.add(new Deadline(str.substring(10, str.indexOf("(by: ")),
                            str.substring(str.indexOf("(by: ") + 5, str.indexOf(")"))));
                    if (str.charAt(7) == 'X')
                        UserInput.userInput.get(inputCount).markAsDone();
                } else if (str.charAt(3) == 'E') {
                    UserInput.userInput.add(new Event(str.substring(10, str.indexOf("(at: ")),
                            str.substring(str.indexOf("(at: ") + 5, str.indexOf(")"))));
                    if (str.charAt(7) == 'X')
                        UserInput.userInput.get(inputCount).markAsDone();
                } else {
                    System.out.println("File loaded incorrectly, local file rebuilt.");
                    f.delete();
                    File tempFile = new File("data/data.txt");
                    boolean a = tempFile.createNewFile();
                    return;
                }
            } else {
                System.out.println("File loaded incorrectly, local file rebuilt.");
                f.delete();
                File tempFile = new File("data/data.txt");
                boolean a = tempFile.createNewFile();
                return;
            }
            inputCount++;
        }
        System.out.println("Local data loaded successfully.");
    }

    /**
     * Writes the userInput to local file
     * @throws IOException
     */
    public static void writeToFile() throws IOException {
        File tempFile = new File("data/tempdata.txt");
        boolean a = tempFile.createNewFile();
        FileWriting fileWriting = new FileWriting();
        try {
            for (int i = 0; i < inputCount; i++) {
                fileWriting.writeToFile("data/tempdata.txt", (i + 1)
                        + ".[" + UserInput.userInput.get(i).getIcon() + "] " + "["
                        + UserInput.userInput.get(i).getStatusIcon() + "] "
                        + UserInput.userInput.get(i).description
                        + UserInput.userInput.get(i).showDate()
                        + System.lineSeparator());
            }
            f.delete();
            tempFile.renameTo(f);
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
