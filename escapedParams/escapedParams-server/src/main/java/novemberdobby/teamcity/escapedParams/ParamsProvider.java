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

        //TODO proper storage
        //TODO constant prefix marker
        for(String paramName : Collections.singleton("message")) {

            String value = "";
            if(!onlyKeys) {
                value = buildParams.get(paramName);
            }

            params.put(String.format("ESCAPED_POWERSHELL_%s", paramName), value);
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