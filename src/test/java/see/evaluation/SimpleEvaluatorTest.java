package see.evaluation;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import see.evaluation.evaluators.SimpleEvaluator;
import see.exceptions.EvaluationException;
import see.exceptions.SeeRuntimeException;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.parser.UserFunctionResolver;
import see.parser.config.ConfigBuilder;
import see.tree.Node;
import see.tree.immutable.ImmutableFunctionNode;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleEvaluatorTest {


    final VarArgFunction<Object, Object> epicFail = new VarArgFunction<Object, Object>() {
        @Override
        public Object apply(@Nonnull List<Object> input) {
            throw new EpicFailException();
        }
    };

    final VarArgFunction<Object, Object> fail = new VarArgFunction<Object, Object>() {
        @Override
        public Object apply(@Nonnull List<Object> input) {
            throw new RuntimeException();
        }
    };

    Evaluator evaluator;

    @Mock
    UserFunctionResolver userFunctionResolver;
    Context evalContext;

    private final ContextCurriedFunction<Object,Object> userFunction = new ContextCurriedFunction<Object, Object>() {
        @Override
        public VarArgFunction<Object, Object> apply(@Nonnull Context context) {
            evalContext = context;
            return new VarArgFunction<Object, Object>() {
                @Override
                public Object apply(@Nonnull List<Object> objects) {
                    return "userResult";
                }
            };
        }
    };

    @Before
    public void setUp() throws Exception {
        evaluator = SimpleEvaluator.fromConfig(ConfigBuilder.defaultConfig()
                .setUserFunctionResolver(userFunctionResolver)
                .addFunction("epicFail", epicFail)
                .addFunction("fail", fail)
                .build());

        when(userFunctionResolver.getFunctions()).thenReturn(ImmutableMap.of("userFunction", userFunction));
    }



    /**
     * Test that all runtime exceptions are wrapped in EvaluationException
     * @throws Exception
     */
    @Test(expected = EvaluationException.class)
    public void testExceptionTranslationForRuntime() throws Exception {
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>("fail");

        evaluator.evaluate(tree, ImmutableMap.<String, Object>of());
    }

    @Test
    public void testUserFunction() throws Exception {
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>("userFunction");

        Object override = new Object();
        assertThat(evaluator.evaluate(tree, ImmutableMap.of("override", override)), is((Object)"userResult"));
        assertThat(evalContext.getScope().get("userFunction"), is((Object) userFunction));
        assertThat(evalContext.getScope().get("fail"), is(notNullValue()));
        assertThat(evalContext.getScope().get("override"), is(override));
    }

    /**
     * Test that subclasses of EvaluationException are not wrapped
     * @throws Exception
     */
    @Test
    public void testExceptionTranslation() throws Exception {
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>("epicFail");

        try {
            evaluator.evaluate(tree, ImmutableMap.<String, Object>of());
            fail("Exception expected");
        } catch (SeeRuntimeException e) {
            assertThat(e.getCause(), instanceOf(EpicFailException.class));
        }
    }

    private static class EpicFailException extends EvaluationException {
        public EpicFailException() {
            super("It failed");
        }
    }
}
