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
package com.netflix.hollow.core.util;

import static com.netflix.hollow.core.util.Threads.daemonThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

// TODO(timt): tag as MEDIUM test
public class ThreadsTest {
    @Test
    public void named() {
        Thread thread = daemonThread(() -> {}, "Thready McThreadson");

        assertEquals("Thready McThreadson", thread.getName());
        assertTrue(thread.isDaemon()); // TODO(timt): invariant
    }

    @Test
    public void described() {
        Thread thread = daemonThread(() -> {}, getClass(), "howdy");

        assertEquals("hollow | ThreadsTest | howdy", thread.getName());
        assertTrue(thread.isDaemon());
    }

    @Test
    public void described_customPlatform() {
        Thread thread = daemonThread(() -> {}, "solid", getClass(), "howdy");

        assertEquals("solid | ThreadsTest | howdy", thread.getName());
        assertTrue(thread.isDaemon());
    }

    @Test
    public void nullName() {
        try {
            daemonThread(() -> {}, null);
            fail("expected an exception");
        } catch (NullPointerException e) {
            assertEquals("name required", e.getMessage());
        }
    }

    @Test
    public void nullPlatform() {
        try {
            daemonThread(() -> {}, null, getClass(), "boom");
            fail("expected an exception");
        } catch (NullPointerException e) {
            assertEquals("platform required", e.getMessage());
        }
    }

    @Test
    public void nullRunnable() {
        try {
            daemonThread(null, getClass(), "boom");
            fail("expected an exception");
        } catch (NullPointerException e) {
            assertEquals("runnable required", e.getMessage());
        }
    }

    @Test
    public void nullContext() {
        try {
            daemonThread(() -> {}, null, "boom");
            fail("expected an exception");
        } catch (NullPointerException e) {
            assertEquals("context required", e.getMessage());
        }
    }

    @Test
    public void nullDescription() {
        try {
            daemonThread(() -> {}, getClass(), null);
            fail("expected an exception");
        } catch (NullPointerException e) {
            assertEquals("description required", e.getMessage());
        }
    }
}
