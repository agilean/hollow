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

import static com.netflix.hollow.api.producer.HollowIncrementalCyclePopulator.AddIfAbsent;
import static com.netflix.hollow.api.producer.HollowIncrementalCyclePopulator.DELETE_RECORD;

import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import com.netflix.hollow.core.write.objectmapper.RecordPrimaryKey;
import com.netflix.hollow.core.write.objectmapper.flatrecords.FlatRecord;
import java.util.concurrent.ConcurrentHashMap;

// @@@ Move into HollowIncrementalCyclePopulator since the event map is a shared resource
//     HollowIncrementalCyclePopulator constructed from the write state instance
final class CloseableIncrementalWriteState implements HollowProducer.Incremental.IncrementalWriteState, AutoCloseable {
    private final ConcurrentHashMap<RecordPrimaryKey, Object> events;
    private final HollowObjectMapper objectMapper;
    private volatile boolean closed;

    public CloseableIncrementalWriteState(
            ConcurrentHashMap<RecordPrimaryKey, Object> events,
            HollowObjectMapper objectMapper) {
        this.events = events;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addOrModify(Object o) {
        ensureNotClosed();

        events.put(getKey(o), o);
    }

    @Override
    public void addIfAbsent(Object o) {
        ensureNotClosed();

        events.putIfAbsent(getKey(o), new AddIfAbsent(o));
    }

    @Override
    public void delete(Object o) {
        delete(getKey(o));
    }

    @Override
    public void delete(RecordPrimaryKey key) {
        ensureNotClosed();

        // @@@ Deletion is silently ignored if no object exists for the key
        events.put(key, DELETE_RECORD);
    }

    private RecordPrimaryKey getKey(Object o) {
        if (o instanceof FlatRecord) {
            FlatRecord fr = (FlatRecord) o;
            return fr.getRecordPrimaryKey();
        } else {
            return objectMapper.extractPrimaryKey(o);
        }
    }

    private void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException(
                    "Write state operated on after the incremental population stage of a cycle");
        }
    }

    @Override public void close() {
        closed = true;
    }
}
