import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Rename {

    public static void main(String[] args) throws IOException {
        for (File folder : new File("./temp/matches/").listFiles()) {
            for (File folder2 : folder.listFiles()) {
                for (File match : folder2.listFiles()) {
                    if(match.getName().contains("&")) {
                        Files.move(match.toPath(), Path.of(match.getParent() + "/" + match.getName().replaceAll("&", "and")));
                    }
                }
            }
        }
    }
}
