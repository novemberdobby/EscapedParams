package novemberdobby.teamcity.escapedParams.impl;

import novemberdobby.teamcity.escapedParams.BaseParamEscaper;
import org.jetbrains.annotations.NotNull;

public class PowershellEscaperImpl implements BaseParamEscaper {

    @Override
    public String getDecodeScript(@NotNull String base64Input) {
        return String.format("([Text.Encoding]::Utf8.GetString([Convert]::FromBase64String(\"%s\")))", base64Input);
    }
}
