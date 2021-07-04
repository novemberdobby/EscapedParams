package novemberdobby.teamcity.escapedParams;

import org.jetbrains.annotations.NotNull;
import java.util.Base64;

public class CSharpEscaperImpl implements BaseParamEscaper {

    @Override
    public String escape(@NotNull String input) {
        String b64 = Base64.getEncoder().encodeToString(input.getBytes());
        return String.format("Encoding.UTF8.GetString(Convert.FromBase64String(\"%s\"));", b64);
    }
}
