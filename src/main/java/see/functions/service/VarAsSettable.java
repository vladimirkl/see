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
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.functions.Settable;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Exposes variable assignment as {@link Settable} interface.
 */
public class VarAsSettable implements ContextCurriedFunction<Function<List<String>, Settable<Object>>> {
    @Override
    public Function<List<String>, Settable<Object>> apply(Map<String, ?> context) {
        final Map<String, Object> writableContext = (Map<String, Object>) context;

        return new VarArgFunction<String, Settable<Object>>() {
            @Override
            public Settable<Object> apply(@Nonnull List<String> input) {
                Preconditions.checkArgument(input.size() == 1, "VarAsSettable takes one argument");

                final String varName = input.get(0);
                return new Settable<Object>() {
                    @Override
                    public void set(Object value) {
                        writableContext.put(varName, value);
                    }
                };
            }
        };
    }

    @Override
    public String toString() {
        return "vSettable";
    }
}