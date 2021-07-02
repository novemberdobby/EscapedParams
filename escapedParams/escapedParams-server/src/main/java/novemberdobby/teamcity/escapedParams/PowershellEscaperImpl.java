package novemberdobby.teamcity.escapedParams;

public class PowershellEscaperImpl implements BaseParamEscaper {

    @Override
    public String escape(String input) {
        return input;
    }
}
