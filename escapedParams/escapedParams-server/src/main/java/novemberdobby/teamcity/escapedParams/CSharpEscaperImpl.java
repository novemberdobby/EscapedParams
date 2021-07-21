package novemberdobby.teamcity.escapedParams;

import org.jetbrains.annotations.NotNull;

public class CSharpEscaperImpl implements BaseParamEscaper {

    @Override
    public String getDecodeScript(@NotNull String base64Input) {
        return String.format("Encoding.UTF8.GetString(Convert.FromBase64String(\"%s\"));", base64Input);
    }
}
