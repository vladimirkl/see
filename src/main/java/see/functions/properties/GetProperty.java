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

package see.functions.properties;

import com.google.common.base.Preconditions;
import see.functions.Property;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

public class GetProperty implements VarArgFunction<Property<Object>, Object> {
    @Override
    public Object apply(@Nonnull List<Property<Object>> input) {
        Preconditions.checkArgument(input.size() == 1, "GetProperty takes one argument");

        Property<Object> property = input.get(0);

        return property.get();
    }
    @Override
    public String toString() {
        return "get";
    }
}
