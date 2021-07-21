package novemberdobby.teamcity.escapedParams;

import jetbrains.buildServer.parameters.ParametersProvider;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.parameters.AbstractBuildParametersProvider;
import jetbrains.buildServer.serverSide.parameters.ParameterDescriptionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

public class ParamsProvider
        extends AbstractBuildParametersProvider
        implements ParameterDescriptionProvider {

    //AbstractBuildParametersProvider
    @NotNull
    @Override
    public Map<String, String> getParameters(@NotNull SBuild build, boolean emulationMode) {
        return getEscapedParams(build, false);
    }

    @NotNull
    @Override
    public Collection<String> getParametersAvailableOnAgent(@NotNull SBuild build) {
        return getEscapedParams(build, true).keySet();
    }

    private Map<String, String> getEscapedParams(SBuild build, boolean onlyKeys) {
        HashMap<String, String> params = new HashMap<>();

        ParametersProvider buildParams = build.getParametersProvider();

        /*TODO add note that surrounding quotes will be added automatically,
               and that any quotes in the parameter will be escaped
         */
        //TODO do we need to hook any config updated events? if a param name is changed
        //TODO 'preview' on feature jsp for arbitrary text
        //TODO proper storage (& don't allow escaping already-escaped params)

        for (String paramName : Collections.singleton("message")) {
            String value = "";
            if (!onlyKeys) {
                //get the raw value from the build
                value = buildParams.get(paramName);

                if(value == null) {
                    //TODO log
                    continue;
                }

                //escape before sending it on
                value = Base64.getEncoder().encodeToString(value.getBytes());
            }

            params.put(String.format("%s_%s", Constants.ESCAPED_PARAM_PREFIX, paramName), value);

            for (EscaperType lang : EscaperType.values()) {
                if(!onlyKeys) {
                    value = lang.getEscaper().getDecodeScript(value);
                }

                params.put(String.format("%s_%s_%s", Constants.ESCAPED_PARAM_PREFIX, lang.getPrefix(), paramName), value);
            }
        }

        return params;
    }

    //ParameterDescriptionProvider
    @Override
    public String describe(String paramName) {

        if(paramName.equals("bleh")) {
            return "'' parameter escaped for ";
        }

        return null;
    }

    //TODO do we actually need this?
    @Override
    public boolean isVisible(@NotNull String paramName) {
        return true;
    }
}