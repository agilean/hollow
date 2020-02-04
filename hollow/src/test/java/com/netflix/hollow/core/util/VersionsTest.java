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

import static com.netflix.hollow.core.HollowConstants.VERSION_LATEST;
import static com.netflix.hollow.core.HollowConstants.VERSION_NONE;

import org.junit.Assert;
import org.junit.Test;

public class VersionsTest {

    @Test
    public void testPrettyPrint() {
        Assert.assertEquals(Versions.PRETTY_VERSION_NONE, Versions.prettyVersion(VERSION_NONE));
        Assert.assertEquals(Versions.PRETTY_VERSION_LATEST, Versions.prettyVersion(VERSION_LATEST));
        Assert.assertEquals("123", Versions.prettyVersion(123l));
    }

}
