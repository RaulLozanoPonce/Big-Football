package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;

import java.util.Map;

public class TeamsApi {

    private final FileAccessor fileAccessor;
    private final Map<String, String> queryParams;

    public TeamsApi(FileAccessor fileAccessor, Map<String, String> queryParams) {
        this.fileAccessor = fileAccessor;
        this.queryParams = queryParams;
    }

    public String getResponse() {
        return new Gson().toJson(this.fileAccessor.teamsFileNames(queryParams.get("competition"), queryParams.get("season")));
    }
}
