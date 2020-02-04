/**
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hollow.api.producer.enforcer;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

public class BasicSingleProducerEnforcerTest {

    @Test
    public void testEnableDisable() {
        BasicSingleProducerEnforcer se = new BasicSingleProducerEnforcer();
        Assert.assertTrue(se.isPrimary());

        se.disable();
        Assert.assertFalse(se.isPrimary());

        se.enable();
        Assert.assertTrue(se.isPrimary());
    }

    @Test
    public void testEnabledDisabledCyle() {
        BasicSingleProducerEnforcer se = new BasicSingleProducerEnforcer();
        Assert.assertTrue(se.isPrimary());

        se.onCycleStart(1234L);
        se.onCycleComplete(null, 10L, TimeUnit.SECONDS);

        se.disable();
        Assert.assertFalse(se.isPrimary());
    }

    @Test
    public void testMultiCycle() {
        BasicSingleProducerEnforcer se = new BasicSingleProducerEnforcer();

        for (int i = 0; i < 10; i++) {
            se.enable();
            Assert.assertTrue(se.isPrimary());

            se.onCycleStart(1234L);

            se.disable();
            Assert.assertTrue(se.isPrimary());

            se.onCycleComplete(null, 10L, TimeUnit.SECONDS);
            Assert.assertFalse(se.isPrimary());
        }
    }

    @Test
    public void testTransitions() {
        BasicSingleProducerEnforcer se = new BasicSingleProducerEnforcer();
        Assert.assertTrue(se.isPrimary());

        se.onCycleStart(1234L);
        Assert.assertTrue(se.isPrimary());

        se.disable();
        Assert.assertTrue(se.isPrimary());

        se.onCycleComplete(null, 10L, TimeUnit.SECONDS);
        Assert.assertFalse(se.isPrimary());

        se.enable();
        Assert.assertTrue(se.isPrimary());

        se.onCycleStart(1235L);
        Assert.assertTrue(se.isPrimary());

        se.disable();
        Assert.assertTrue(se.isPrimary());

        se.onCycleComplete(null, 10L, TimeUnit.SECONDS);
        Assert.assertFalse(se.isPrimary());
    }

}
