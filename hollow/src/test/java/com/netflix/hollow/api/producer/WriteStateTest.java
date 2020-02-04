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
package com.netflix.hollow.api.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class WriteStateTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();
    @Mock
    private HollowWriteStateEngine writeStateEngine;
    @Mock
    private HollowObjectMapper objectMapper;

    private CloseableWriteState subject;
    private long version;

    @Before
    public void before() {
        when(objectMapper.getStateEngine()).thenReturn(writeStateEngine);

        version = 13L;
        subject = new CloseableWriteState(version, objectMapper, null);
    }

    @Test
    public void add_delegatesToObjectMapper() {
        subject.add("Yes!");
        verify(objectMapper).add("Yes!");
    }

    @Test
    public void getObjectMapper() {
        assertEquals(objectMapper, subject.getObjectMapper());
    }

    @Test
    public void getStateEngine_delegatesToObjectMapper() throws Exception {
        assertEquals(writeStateEngine, subject.getStateEngine());
    }

    @Test
    public void add_whenClosed() {
        assertThrowsAfterClose(() -> subject.add("Nope!"));
    }

    @Test
    public void getObjectMapper_whenClosed() {
        assertThrowsAfterClose(() -> subject.getObjectMapper());
    }

    @Test
    public void getStateEngine_whenClosed() {
        assertThrowsAfterClose(() -> subject.getStateEngine());
    }

    @Test
    public void getPriorState_whenClosed() {
        assertThrowsAfterClose(() -> subject.getPriorState());
    }

    @Test
    public void getVersion_whenClosed() {
        assertThrowsAfterClose(() -> subject.getVersion());
    }

    private void assertThrowsAfterClose(Runnable code) {
        subject.close();
        try {
            code.run();
            fail("should throw");
        } catch (IllegalStateException e) {
            assertEquals("Write state operated on after the population stage of a cycle; version=" + version, e.getMessage());
        }
    }
}
