package novemberdobby.teamcity.escapedParams;

import org.jetbrains.annotations.NotNull;

interface BaseParamEscaper {

    /**
     * @param input raw parameter string with arbitrary text
     * @return language-specific function to decode a base64 string into a variable
     */
    String escape(@NotNull String input);
}
