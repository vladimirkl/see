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

package see.reactive;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.eventbus.EventBus;

import java.util.Collection;

public class StatefulSignal<T> extends AbstractDependency implements Signal<T> {

    private final Supplier<T> evaluation;

    private T currentValue;

    public StatefulSignal(EventBus eventBus, Collection<? extends Dependency> dependencies, Supplier<T> evaluation) {
        super(eventBus, dependencies);
        this.evaluation = evaluation;
        currentValue = evaluation.get();
    }

    @Override
    public T now() {
        return currentValue;
    }

    @Override
    protected void updateInvalidate() {
        T newValue = evaluation.get();
        if (!Objects.equal(currentValue, newValue)) {
            currentValue = newValue;
            invalidate();
        }
    }

}
