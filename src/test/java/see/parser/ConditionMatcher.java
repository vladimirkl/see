package see.parser;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.parser.grammar.Expressions;

/**
 * Matcher for expressions
 */
public class ConditionMatcher extends TypeSafeMatcher<String> {
    private final ParseRunner<?> runner;
    private final String description;

    public ConditionMatcher(Rule rule, String description) {
        this.runner = new ReportingParseRunner<Object>(rule);
        this.description = description;
    }

    @Override
    public boolean matchesSafely(String item) {
        ParsingResult<?> result = runner.run(item);
        return result.matched;
    }

    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    @Factory
    public static Matcher<String> condition() {
        return new ConditionMatcher(Parboiled.createParser(Expressions.class).Condition(), "a condition");
    }

    @Factory
    public static Matcher<String> returnExpression() {
        return new ConditionMatcher(Parboiled.createParser(Expressions.class).CalcExpression(), "an expression");
    }
}
