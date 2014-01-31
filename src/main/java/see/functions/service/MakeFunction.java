/*
 * Copyright 2011 Vasily Shiyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package see.functions.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import see.evaluation.Context;
import see.evaluation.ContextEvaluator;
import see.evaluation.Scope;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.tree.Node;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static see.evaluation.evaluators.SimpleContext.withVariables;
import static see.evaluation.scopes.Scopes.defCapture;
import static see.evaluation.scopes.Scopes.mutableOverride;

/**
 * Function creation. Takes list of argument names and tree, returns function,
 * which takes same number of arguments and evaluates tree against context with these arguments.
 */
public class MakeFunction implements ContextCurriedFunction<Object, VarArgFunction<Object, Object>> {
    @Override
    public VarArgFunction<Object, VarArgFunction<Object, Object>> apply(@Nonnull final Context context) {
        return new VarArgFunction<Object, VarArgFunction<Object, Object>>() {
            @Override
            public VarArgFunction<Object, Object> apply(@Nonnull List<Object> input) {
                final List<String> argNames = (List<String>) input.get(0);
                final Node<?> tree = (Node<?>) input.get(1);

                return new FunctionLiteral(argNames, tree, context);
            }
        };
    }

    @Override
    public String toString() {
        return "def";
    }

    private static class FunctionLiteral implements VarArgFunction<Object, Object> {
        private final Context context;
        private final Node<?> tree;
        private final List<String> argNames;

        public FunctionLiteral(List<String> argNames, Node<?> tree, Context context) {
            this.argNames = argNames;
            this.tree = tree;
            this.context = context;
        }

        @Override
        public Object apply(@Nonnull List<Object> actualArgs) {
            Preconditions.checkArgument(actualArgs.size() == argNames.size(), "Wrong number of arguments");

            ContextEvaluator evaluator = context.getServices().getInstance(ContextEvaluator.class);

            return evaluator.evaluate(tree, overrideArgs(argNames, actualArgs));
        }

        private Context overrideArgs(List<String> argNames, List<Object> actualArgs) {
            Map<String, Object> scopeOverride = Maps.newHashMap();
            Iterator<String> names = argNames.iterator();
            Iterator<Object> args = actualArgs.iterator();

            while (names.hasNext()) {
                scopeOverride.put(names.next(), args.next());
            }

            Scope scope = defCapture(mutableOverride(context.getScope(), scopeOverride));
            return withVariables(context, scope);
        }
    }
}
