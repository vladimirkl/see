package see.parser;

import see.functions.ContextCurriedFunction;

import java.util.Map;

public interface UserFunctionResolver {

    Map<String, ContextCurriedFunction<Object, Object>> getFunctions() throws Exception;

}
