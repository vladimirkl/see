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

import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.ImmutableSet.of;
import static java.lang.Integer.valueOf;
import static junit.framework.Assert.assertEquals;

public class AbstractDependencyTest {

    ReactiveFactory reactiveFactory;

    @Before
    public void setUp() throws Exception {
        reactiveFactory = new ReactiveFactory();
    }

    @Test
    public void testInteraction() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("asd");
        Signal<Integer> b = reactiveFactory.bindWithState(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now().length();
            }
        });

        assertEquals(valueOf(3), b.now());
        a.update("omg");
        assertEquals(valueOf(3), b.now());
        a.update("zxcv");
        assertEquals(valueOf(4), b.now());
    }

    @Test
    public void testMultipleDeps() throws Exception {
        final VariableSignal<Integer> a = reactiveFactory.var(1);
        final VariableSignal<Integer> b = reactiveFactory.var(2);

        Signal<Integer> sum = reactiveFactory.bindWithState(of(a, b), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now() + b.now();
            }
        });

        assertEquals(valueOf(3), sum.now());

        a.update(7);
        assertEquals(valueOf(9), sum.now());
        
        b.update(35);
        assertEquals(valueOf(42), sum.now());
    }

    @Test
    public void testStatelessSignal() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("crno");
        final AtomicInteger counter = new AtomicInteger(0);

        Signal<Integer> b = reactiveFactory.bind(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                counter.incrementAndGet();
                return a.now().length();
            }
        });

        assertEquals(0, counter.get());

        assertEquals(valueOf(4), b.now());
        assertEquals(1, counter.get());

        assertEquals(valueOf(4), b.now());
        assertEquals(2, counter.get());
        
        a.update("bka");
        assertEquals(2, counter.get());
        assertEquals(valueOf(3), b.now());
        assertEquals(3, counter.get());

    }
}
