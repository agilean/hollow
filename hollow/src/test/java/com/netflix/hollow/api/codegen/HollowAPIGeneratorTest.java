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
package com.netflix.hollow.api.codegen;

import org.junit.Test;

public class HollowAPIGeneratorTest extends AbstractHollowAPIGeneratorTest {

    @Test
    public void generatesFileUsingDestinationPath() throws Exception {
        runGenerator("API", "com.netflix.hollow.example.api.generated", MyClass.class, b -> b);
    }

    @Test
    public void testGenerateWithPostfix() throws Exception {
        runGenerator("MyClassTestAPI", "codegen.api", MyClass.class,
                builder -> builder.withClassPostfix("Generated"));
        assertNonEmptyFileExists("codegen/api/StringGenerated.java");
        assertClassHasHollowTypeName("codegen.api.MyClassGenerated", "MyClass");
    }

    @Test
    public void testGenerateWithPostfixAndPackageGrouping() throws Exception {
        runGenerator("MyClassTestAPI", "codegen.api", MyClass.class,
                builder -> builder.withClassPostfix("Generated").withPackageGrouping());
        assertNonEmptyFileExists("codegen/api/core/StringGenerated.java");
    }

    @Test
    public void testGenerateWithPostfixAndPrimitiveTypes() throws Exception {
        runGenerator("MyClassTestAPI", "codegen.api", MyClass.class,
                builder -> builder.withClassPostfix("Generated").withPackageGrouping()
                .withHollowPrimitiveTypes(true));
        assertFileDoesNotExist("codegen/api/core/StringGenerated.java");
        assertFileDoesNotExist("codegen/api/StringGenerated.java");
    }

    @Test
    public void testGenerateWithPostfixAndAggressiveSubstitutions() throws Exception {
        runGenerator("MyClassTestAPI", "codegen.api", MyClass.class,
                builder -> builder.withClassPostfix("Generated").withPackageGrouping()
                .withHollowPrimitiveTypes(true).withAggressiveSubstitutions(true));
        assertFileDoesNotExist("codegen/api/core/StringGenerated.java");
        assertFileDoesNotExist("codegen/api/StringGenerated.java");
    }

    @SuppressWarnings("unused")
    private static class MyClass {
        int id;
        String foo;

        public MyClass(int id, String foo) {
            this.id = id;
            this.foo = foo;
        }
    }
}
