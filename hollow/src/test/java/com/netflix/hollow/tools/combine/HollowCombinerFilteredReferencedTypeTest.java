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
package com.netflix.hollow.tools.combine;

import com.netflix.hollow.core.read.engine.HollowReadStateEngine;
import com.netflix.hollow.core.read.engine.HollowTypeReadState;
import com.netflix.hollow.core.read.filter.HollowFilterConfig;
import com.netflix.hollow.core.util.StateEngineRoundTripper;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class HollowCombinerFilteredReferencedTypeTest {
    
    @Test
    public void combinesEvenIfReferencedTypesAreFiltered() throws IOException {
        HollowWriteStateEngine writeEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeEngine);
        
        mapper.add(new TypeA(1, 1));
        mapper.add(new TypeA(2, 2));
        mapper.add(new TypeA(3, 3));
        
        HollowReadStateEngine readEngine = new HollowReadStateEngine();
        HollowFilterConfig filter = new HollowFilterConfig(true);
        filter.addType("TypeB");
        
        StateEngineRoundTripper.roundTripSnapshot(writeEngine, readEngine, filter);
        
        HollowCombiner combiner = new HollowCombiner(readEngine);
        combiner.combine();
        
        HollowWriteStateEngine combined = combiner.getCombinedStateEngine();
        
        HollowReadStateEngine combinedReadStateEngine = StateEngineRoundTripper.roundTripSnapshot(combined);
        
        HollowTypeReadState typeState = combinedReadStateEngine.getTypeState("TypeA");
        Assert.assertEquals(3, typeState.getPopulatedOrdinals().cardinality());
        
        Assert.assertNull(combinedReadStateEngine.getTypeState("TypeB"));
    }
    
    
    
    @SuppressWarnings("unused")
    private class TypeA {
        int id;
        TypeB typeB;
        
        TypeA(int id, int bVal) {
            this.id = id;
            this.typeB = new TypeB();
            typeB.val = bVal;
        }
    }
    
    @SuppressWarnings("unused")
    private class TypeB {
        int val;
    }
    
}
