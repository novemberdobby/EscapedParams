package novemberdobby.teamcity.escapedParams;

import jetbrains.buildServer.log.Loggers;
import java.lang.reflect.Constructor;

public enum EscaperType {
    PowerShell("PowerShell", "POWERSHELL", PowershellEscaperImpl.class),
    CSharp("C#", "CSHARP", CSharpEscaperImpl.class),
    Python("Python", "PYTHON", PythonEscaperImpl.class),
    ;

    private final String m_description;
    private final String m_paramPrefix;
    private BaseParamEscaper m_impl = null;

    EscaperType(String description, String paramPrefix, Class<?> impl) {
        m_description = description;
        m_paramPrefix = paramPrefix;

        for(Constructor<?> ctor : impl.getConstructors()) {
            if(ctor.getParameterCount() == 0) {
                try {
                    m_impl = (BaseParamEscaper)ctor.newInstance();
                } catch (Throwable e) {
                    Loggers.SERVER.error(String.format("Failed to instantiate %s parameter escaper:", m_description));
                    Loggers.SERVER.error(e);
                }
                break;
            }
        }
    }

    /**
     * @return human-readable language name
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * @return prefix for resulting added parameters
     */
    public String getPrefix() {
        return m_paramPrefix;
    }

    /**
     * @return escaper implementation
     */
    public BaseParamEscaper getEscaper() {
        return m_impl;
    }
}
