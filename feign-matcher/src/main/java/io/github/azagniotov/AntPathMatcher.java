package io.github.azagniotov;

/**
 * Shamelessly copied from https://github.com/azagniotov/ant-style-path-matcher.
 * Package unaltered to give credit where it's due.
 */
public class AntPathMatcher {
    private static final char ASTERISK = '*';
    private static final char QUESTION = '?';
    private static final char BLANK = ' ';
    private static final int ASCII_CASE_DIFFERENCE_VALUE = 32;

    private final char pathSeparator;
    private final boolean ignoreCase;
    private final boolean matchStart;
    private final boolean trimTokens;

    private AntPathMatcher(final char pathSeparator, boolean ignoreCase, boolean matchStart, boolean trimTokens) {
        this.pathSeparator = pathSeparator;
        this.ignoreCase = ignoreCase;
        this.matchStart = matchStart;
        this.trimTokens = trimTokens;
    }

    public boolean isMatch(final String pattern, final String path) {
        return isMatch(pattern.toCharArray(), 0, path.toCharArray(), 0);
    }

    private boolean isMatch(final char[] pattern, final int patternPointer, final char[] path, final int pathPointer) {
        if (empty(pattern, patternPointer)) {
            return empty(path, pathPointer);
        } else if (empty(path, pathPointer) && pattern[patternPointer] == pathSeparator) {
            if (matchStart) {
                return true;
            } else if (lengthOf(pattern, 2, patternPointer) && pattern[patternPointer + 1] == ASTERISK) {
                return false;
            }
            return isMatch(pattern, patternPointer + 1, path, pathPointer);
        }

        final char patternStart = pattern[patternPointer];
        if (patternStart == ASTERISK) {

            if (lengthOf(pattern, 1, patternPointer)) {
                return empty(path, pathPointer) || path[pathPointer] != pathSeparator && isMatch(pattern, patternPointer, path, pathPointer + 1);
            } else if (doubleAsteriskMatch(pattern, patternPointer, path, pathPointer)) {
                return true;
            }

            int start = pathPointer;
            while (start < path.length) {
                if (isMatch(pattern, patternPointer + 1, path, start)) {
                    return true;
                }
                start++;
            }

            return isMatch(pattern, patternPointer + 1, path, start);
        }

        int pointer = skipBlanks(path, pathPointer);

        return (pointer < path.length) && (equal(path[pointer], patternStart) || patternStart == QUESTION)
                && isMatch(pattern, patternPointer + 1, path, pointer + 1);
    }

    private boolean doubleAsteriskMatch(final char[] pattern, final int patternPointer, final char[] path, final int pathPointer) {
        if (pattern[patternPointer + 1] != ASTERISK) {
            return false;
        } else if (pattern.length - patternPointer > 2) {
            return isMatch(pattern, patternPointer + 3, path, pathPointer);
        }

        return false;
    }

    private int skipBlanks(final char[] path, final int pathPointer) {
        int pointer = pathPointer;
        if (trimTokens) {
            while (pointer < path.length && path[pointer] == BLANK) {
                pointer++;
            }
        }
        return pointer;
    }

    private boolean equal(final char pathChar, final char patternChar) {
        if (ignoreCase) {
            return pathChar == patternChar ||
                    ((pathChar > patternChar) ?
                            pathChar == patternChar + ASCII_CASE_DIFFERENCE_VALUE :
                            pathChar == patternChar - ASCII_CASE_DIFFERENCE_VALUE);
        }
        return pathChar == patternChar;
    }

    private boolean empty(final char[] characters, final int pointer) {
        return characters.length == pointer;
    }

    private boolean lengthOf(final char[] characters, final int length, final int pointer) {
        return characters.length - pointer == length;
    }

    public static final class Builder {

        private char pathSeparator = '/';
        private boolean ignoreCase = false;
        private boolean matchStart = false;
        private boolean trimTokens = false;

        public Builder() {

        }

        public Builder withPathSeparator(final char pathSeparator) {
            this.pathSeparator = pathSeparator;
            return this;
        }

        public Builder withIgnoreCase() {
            this.ignoreCase = true;
            return this;
        }

        public Builder withMatchStart() {
            this.matchStart = true;
            return this;
        }

        public Builder withTrimTokens() {
            this.trimTokens = true;
            return this;
        }

        public AntPathMatcher build() {
            return new AntPathMatcher(pathSeparator, ignoreCase, matchStart, trimTokens);
        }
    }
}
