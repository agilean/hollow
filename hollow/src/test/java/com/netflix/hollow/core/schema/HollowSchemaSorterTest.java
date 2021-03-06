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
package com.netflix.hollow.core.schema;

import com.netflix.hollow.core.util.HollowWriteStateCreator;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class HollowSchemaSorterTest {
    
    @Test
    public void schemasAreSortedBasedOnDependencies() throws IOException {
        String schemasText = "TypeB {"
                           + "    ListOfString str;"
                           + "}"
                           + ""
                           + "String {"
                           + "    string value;"
                           + "}"
                           + ""
                           + "ListOfString List<String>;"
                           + ""
                           + "TypeA {"
                           + "    TypeB b;"
                           + "    String str;"
                           + "}";
        
        List<HollowSchema> schemas = HollowSchemaParser.parseCollectionOfSchemas(schemasText);
        
        List<HollowSchema> sortedSchemas = HollowSchemaSorter.dependencyOrderedSchemaList(schemas);
        
        Assert.assertEquals(4, sortedSchemas.size());
        Assert.assertEquals("String", sortedSchemas.get(0).getName());
        Assert.assertEquals("ListOfString", sortedSchemas.get(1).getName());
        Assert.assertEquals("TypeB", sortedSchemas.get(2).getName());
        Assert.assertEquals("TypeA", sortedSchemas.get(3).getName());
    }
    
    @Test
    public void sortsSchemasEvenIfDependencyTypesNotPresent() throws IOException {
        String schemasText = "TypeA { TypeB b; }"
                           + "TypeB { TypeC c; }";
        
        
        List<HollowSchema> schemas = HollowSchemaParser.parseCollectionOfSchemas(schemasText);
        
        List<HollowSchema> sortedSchemas = HollowSchemaSorter.dependencyOrderedSchemaList(schemas);

        Assert.assertEquals(2, sortedSchemas.size());
        Assert.assertEquals("TypeB", sortedSchemas.get(0).getName());
        Assert.assertEquals("TypeA", sortedSchemas.get(1).getName());
    }
    
    @Test
    public void determinesIfSchemasAreTransitivelyDependent() throws IOException {
        String schemasText = "TypeA { TypeB b; }"
                           + "TypeB { TypeC c; }"
                           + "TypeC { TypeD d; }";
        
        List<HollowSchema> schemas = HollowSchemaParser.parseCollectionOfSchemas(schemasText);
        
        HollowWriteStateEngine stateEngine = new HollowWriteStateEngine();
        HollowWriteStateCreator.populateStateEngineWithTypeWriteStates(stateEngine, schemas);
        
        Assert.assertTrue(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeA", "TypeB"));
        Assert.assertTrue(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeA", "TypeC"));
        Assert.assertTrue(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeB", "TypeC"));
        Assert.assertFalse(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeC", "TypeB"));
        Assert.assertFalse(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeB", "TypeA"));
        Assert.assertFalse(HollowSchemaSorter.typeIsTransitivelyDependent(stateEngine, "TypeC", "TypeA"));
    }

}
