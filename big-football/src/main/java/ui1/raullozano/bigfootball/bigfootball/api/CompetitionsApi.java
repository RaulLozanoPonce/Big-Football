package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;

public class CompetitionsApi {

    private final FileAccessor fileAccessor;

    public CompetitionsApi(FileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;
    }

    public String getResponse() {
        return new Gson().toJson(this.fileAccessor.competitionFolderNames());
    }
}
