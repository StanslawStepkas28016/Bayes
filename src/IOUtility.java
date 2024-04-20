import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IOUtility {
    public static List<String> readFromPath(String stringPath) {
        List<String> fileContents = null;

        try {
            fileContents = Files.readAllLines(Path.of(stringPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileContents;
    }
}
