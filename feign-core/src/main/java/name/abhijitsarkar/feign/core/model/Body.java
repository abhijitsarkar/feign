package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import lombok.Setter;
import name.abhijitsarkar.feign.core.matcher.BodyMatcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class Body extends AbstractIgnorableRequestPart {
    private Class<? extends Predicate<RequestProperties>> matcher;

    @Setter
    private String raw;
    @Setter
    private String url;
    @Setter
    private String classpath;

    public Body() {
        long count = Stream.of(raw, url, classpath)
                .filter(Objects::nonNull)
                .count();

        checkState(count != 1, "Ambiguous request body declaration.");

        setMatcher(matcher);
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

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? BodyMatcher.class : matcher;
    }
}
