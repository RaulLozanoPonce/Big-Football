package ui1.raullozano.bigfootball.bigfootball;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;

public class Main {

    public static void main(String[] args) {

        FileAccessor fileAccessor = new LocalFileAccessor();
        UIBox box = new UIBox(fileAccessor);
        box.start();
    }
}
