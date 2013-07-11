package see.parser;

import com.google.common.collect.ImmutableMap;
import see.functions.ContextCurriedFunction;

import java.util.Map;

public class NoOpUserFunctionResolver implements UserFunctionResolver {
    @Override
    public Map<String, ContextCurriedFunction<Object, Object>> getFunctions() throws Exception{
        return ImmutableMap.of();
    }

}
