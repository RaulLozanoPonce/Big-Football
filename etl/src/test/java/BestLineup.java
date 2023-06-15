import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.etl.transformator.ml.BestLineupModel;

public class BestLineup {

    public static void main(String[] args) {
        LocalFileAccessor fileAccessor = new LocalFileAccessor();
        new BestLineupModel(fileAccessor).get();
    }
}
