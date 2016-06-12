package name.abhijitsarkar.feign.core.model;

import lombok.Data;

/**
 * @author Abhijit Sarkar
 */
@Data
public abstract class IgnorableRequestProperties {
    private Boolean ignoreUnknown;
    private Boolean ignoreEmpty;
    private Boolean ignoreCase;

    public Boolean isIgnoreUnknown() {
        return ignoreUnknown;
    }

    public Boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public Boolean isIgnoreCase() {
        return ignoreCase;
    }
}
