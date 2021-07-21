package novemberdobby.teamcity.escapedParams;

import org.jetbrains.annotations.NotNull;

public class PythonEscaperImpl implements BaseParamEscaper {

    @Override
    public String getDecodeScript(@NotNull String base64Input) {
        //TODO: add note to import base64 rather than exec/eval
        return String.format("base64.b64decode(\"%s\")", base64Input);
    }
}
