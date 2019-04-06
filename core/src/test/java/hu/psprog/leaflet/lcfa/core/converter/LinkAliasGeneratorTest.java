package hu.psprog.leaflet.lcfa.core.converter;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LinkAliasGenerator}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class LinkAliasGeneratorTest {

    private LinkAliasGenerator linkAliasGenerator;

    @Before
    public void setup() {
        linkAliasGenerator = new LinkAliasGenerator();
    }

    @Test
    @Parameters(source = AliasProvider.class)
    public void shouldGenerateAlias(String input, String expectedResult) {

        // when
        String result = linkAliasGenerator.generateAlias(input);

        // then
        assertThat(result, equalTo(expectedResult));
    }

    public static class AliasProvider {

        public static Object[] provide() {

            return new Object[] {
                    new Object[] {null, null},
                    new Object[] {"simple", "simple"},
                    new Object[] {" unnecessary spaces   ", "unnecessary-spaces"},
                    new Object[] {"VaRiAbLe Case", "variable-case"},
                    new Object[] {"Árvíztűrő Tükörfúrógép", "arvizturo-tukorfurogep"}
            };
        }
    }
}
