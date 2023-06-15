package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;

import java.util.Map;

public class SeasonsApi {

    private final FileAccessor fileAccessor;
    private final Map<String, String> queryParams;

    public SeasonsApi(FileAccessor fileAccessor, Map<String, String> queryParams) {
        this.fileAccessor = fileAccessor;
        this.queryParams = queryParams;
    }

    public String getResponse() {
        return new Gson().toJson(this.fileAccessor.seasonFolderNames(queryParams.get("competition")));
    }
}
