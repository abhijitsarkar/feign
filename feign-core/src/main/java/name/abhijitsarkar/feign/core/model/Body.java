package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Getter
@Setter
public class Body extends IgnorableRequestProperties {
    private String raw;
    private String url;
    private String classpath;

    public Body() {
        long count = Stream.of(raw, url, classpath)
                .filter(Objects::nonNull)
                .count();

        checkState(count != 1, "Ambiguous request body declaration.");
    }

    public String getContent() {
        if (!isEmpty(raw)) {
            return raw;
        } else if (!isEmpty(url) || !isEmpty(classpath)) {
            String resource = Stream.of(url, classpath)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .get();
            try (InputStream io = new ClassPathResource(resource).getInputStream()) {
                return copyToString(io, UTF_8);
            } catch (IOException e) {
                try (InputStream io = new UrlResource(resource).getInputStream()) {
                    return copyToString(io, UTF_8);
                } catch (IOException ex) {
                    throw new UncheckedIOException(
                            String.format("Failed to read body from resource: %s.", resource), ex);
                }
            }
        }

        return "";
    }
}
