package hu.psprog.leaflet.lcfa.core.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Link alias generator utility.
 *
 * @author Peter Smith
 */
@Component
public class LinkAliasGenerator {

    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_DASH = '-';

    /**
     * Generates an URL-safe link alias from the given input. Steps done:
     *  - converts string to lowercase
     *  - removes accents
     *  - changes spaces to dashes
     *  - removes whitespaces
     *
     * @param input input string
     * @return generated link alias
     */
    public String generateAlias(String input) {

        String linkAlias = input;
        if (Objects.nonNull(linkAlias)) {
            linkAlias = StringUtils.stripAccents(linkAlias)
                    .strip()
                    .toLowerCase()
                    .replace(CHAR_SPACE, CHAR_DASH);
        }

        return linkAlias;
    }
}
