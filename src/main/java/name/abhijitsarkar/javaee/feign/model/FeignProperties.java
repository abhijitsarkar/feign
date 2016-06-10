package name.abhijitsarkar.javaee.feign.model;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Abhijit Sarkar
 */
@Component
@ConfigurationProperties(prefix = "feign")
@Getter
public class FeignProperties {
    private List<FeignMapping> mappings;

    @PostConstruct
    void postConstruct() {
        setMappings(mappings);
    }

    public void setMappings(List<FeignMapping> mappings) {
        this.mappings = (mappings == null) ? emptyList() : mappings;
    }
}
