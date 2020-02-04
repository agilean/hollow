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
package com.netflix.hollow.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.netflix.hollow.core.schema.HollowSchema;
import com.netflix.hollow.core.schema.HollowSchemaParser;
import com.netflix.hollow.core.schema.SimpleHollowDataset;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class HollowDatasetTest {

    @Test
    public void identifiesIdenticalSchemas() throws IOException {
        List<HollowSchema> schemas1 = HollowSchemaParser.parseCollectionOfSchemas("TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>; TypeB { float b1; bytes b2; }");
        List<HollowSchema> schemas2 = HollowSchemaParser.parseCollectionOfSchemas("TypeB { float b1; bytes b2; }  TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>;");
        HollowDataset dataset1 = new SimpleHollowDataset(schemas1);
        HollowDataset dataset2 = new SimpleHollowDataset(schemas2);
        
        assertTrue(dataset1.hasIdenticalSchemas(dataset2));
        assertTrue(dataset2.hasIdenticalSchemas(dataset1));
    }
    
    @Test
    public void identifiesDifferentSchemasWithSameName() throws IOException {
        List<HollowSchema> schemas1 = HollowSchemaParser.parseCollectionOfSchemas("TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; boolean newField; }  MapOfTypeB Map<TypeB, TypeB>; TypeB { float b1; bytes b2; }");
        List<HollowSchema> schemas2 = HollowSchemaParser.parseCollectionOfSchemas("TypeB { float b1; bytes b2; }  TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>;");
        HollowDataset dataset1 = new SimpleHollowDataset(schemas1);
        HollowDataset dataset2 = new SimpleHollowDataset(schemas2);
        
        assertFalse(dataset1.hasIdenticalSchemas(dataset2));
        assertFalse(dataset2.hasIdenticalSchemas(dataset1));
    }
    
    @Test
    public void identifiesDifferentSchemasWithDifferentName() throws IOException {
        List<HollowSchema> schemas1 = HollowSchemaParser.parseCollectionOfSchemas("DifferentTypeName { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>; TypeB { float b1; bytes b2; }");
        List<HollowSchema> schemas2 = HollowSchemaParser.parseCollectionOfSchemas("TypeB { float b1; bytes b2; }  TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>;");
        HollowDataset dataset1 = new SimpleHollowDataset(schemas1);
        HollowDataset dataset2 = new SimpleHollowDataset(schemas2);
        
        assertFalse(dataset1.hasIdenticalSchemas(dataset2));
        assertFalse(dataset2.hasIdenticalSchemas(dataset1));
    }

    @Test
    public void identifiesMissingSchema() throws IOException {
        List<HollowSchema> schemas1 = HollowSchemaParser.parseCollectionOfSchemas("TypeA { int a1; string a2; TypeB a3; MapOfTypeB a4; }  MapOfTypeB Map<TypeB, TypeB>; TypeB { float b1; bytes b2; }");
        List<HollowSchema> schemas2 = HollowSchemaParser.parseCollectionOfSchemas("TypeB { float b1; bytes b2; }  MapOfTypeB Map<TypeB, TypeB>;");
        HollowDataset dataset1 = new SimpleHollowDataset(schemas1);
        HollowDataset dataset2 = new SimpleHollowDataset(schemas2);
        
        assertFalse(dataset1.hasIdenticalSchemas(dataset2));
        assertFalse(dataset2.hasIdenticalSchemas(dataset1));
    }

}
