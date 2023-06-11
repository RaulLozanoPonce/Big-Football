package ui1.raullozano.bigfootball.etl;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;

public class Main {

    public static void main(String[] args) {

        FileAccessor fileAccessor = new LocalFileAccessor();
        ETLBox box = new ETLBox(fileAccessor);
        box.start();
    }
}
