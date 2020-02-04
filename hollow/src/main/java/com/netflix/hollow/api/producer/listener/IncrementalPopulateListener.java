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
package com.netflix.hollow.api.producer.listener;

import com.netflix.hollow.api.producer.Status;
import java.time.Duration;

/**
 * A listener of incremental population events associated with the populate cycle stage.
 * <p>
 * A populate listener instance may be registered when building a incremental
 * {@link com.netflix.hollow.api.producer.HollowProducer.Incremental producer}
 * (see {@link com.netflix.hollow.api.producer.HollowProducer.Builder#withListener(HollowProducerEventListener)}} or by
 * registering on the producer itself
 * (see {@link com.netflix.hollow.api.producer.HollowProducer.Incremental#addListener(HollowProducerEventListener)}.
 */
public interface IncrementalPopulateListener extends HollowProducerEventListener {
    /**
     * Called before starting to execute the task to incrementally populate data into Hollow.
     *
     * @param version current version of the cycle
     */
    void onIncrementalPopulateStart(long version);

    /**
     * Called once the incremental populating task stage has finished successfully or failed.
     * Use {@link Status#getType()} to get status of the task.
     *
     * @param status A value of {@code SUCCESS} indicates that all data was successfully populated.
     * {@code FAIL} status indicates populating hollow with data failed.
     * @param removed the number of records removed
     * @param addedOrModified the number of records added or modified
     * @param version current version of the cycle
     * @param elapsed Time taken to populate hollow.
     */
    void onIncrementalPopulateComplete(Status status,
            long removed, long addedOrModified,
            long version, Duration elapsed);
}
