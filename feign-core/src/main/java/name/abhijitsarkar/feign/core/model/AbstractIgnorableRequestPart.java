package name.abhijitsarkar.feign.core.model;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @author Abhijit Sarkar
 */
public abstract class AbstractIgnorableRequestPart {
    private Boolean ignoreUnknown;
    private Boolean ignoreEmpty;
    private Boolean ignoreCase;

    public AbstractIgnorableRequestPart() {
        setIgnoreUnknown(ignoreUnknown);
        setIgnoreEmpty(ignoreEmpty);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreUnknown() {
        return ignoreUnknown == null ? TRUE : ignoreUnknown;
    }

    public boolean isIgnoreEmpty() {
        return ignoreEmpty == null ? TRUE : ignoreEmpty;
    }

    public boolean isIgnoreCase() {
        return ignoreCase == null ? FALSE : ignoreCase;
    }

    public void setIgnoreUnknown(Boolean ignoreUnknown) {
        this.ignoreUnknown = (ignoreUnknown == null) ? TRUE : ignoreUnknown;
    }

    public void setIgnoreEmpty(Boolean ignoreEmpty) {
        this.ignoreEmpty = (ignoreEmpty == null) ? TRUE : ignoreEmpty;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
