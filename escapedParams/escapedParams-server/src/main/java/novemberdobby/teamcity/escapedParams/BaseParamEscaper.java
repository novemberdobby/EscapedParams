package novemberdobby.teamcity.escapedParams;

import org.jetbrains.annotations.NotNull;

interface BaseParamEscaper {

    /**
     * @param base64Input parameter string pre-escaped into base64
     * @return language-specific function to decode a base64 string into a variable
     */
    String getDecodeScript(@NotNull String base64Input);
}
