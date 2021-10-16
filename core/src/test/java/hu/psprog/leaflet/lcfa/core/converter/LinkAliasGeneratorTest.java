package hu.psprog.leaflet.lcfa.core.converter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LinkAliasGenerator}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class LinkAliasGeneratorTest {

    @InjectMocks
    private LinkAliasGenerator linkAliasGenerator;

    @ParameterizedTest
    @MethodSource("aliasDataProvider")
    public void shouldGenerateAlias(String input, String expectedResult) {

        // when
        String result = linkAliasGenerator.generateAlias(input);

        // then
        assertThat(result, equalTo(expectedResult));
    }

    private static Stream<Arguments> aliasDataProvider() {

        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("simple", "simple"),
                Arguments.of(" unnecessary spaces   ", "unnecessary-spaces"),
                Arguments.of("VaRiAbLe Case", "variable-case"),
                Arguments.of("Árvíztűrő Tükörfúrógép", "arvizturo-tukorfurogep")
        );
    }
}
