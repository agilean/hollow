package com.netflix.vms.transformer.common;

import com.netflix.vms.logging.TaggingLogger;
import com.netflix.vms.transformer.common.cassandra.TransformerCassandraHelper;
import com.netflix.vms.transformer.common.config.OctoberSkyData;
import com.netflix.vms.transformer.common.config.TransformerConfig;
import com.netflix.vms.transformer.common.cup.CupLibrary;
import com.netflix.vms.transformer.common.publish.workflow.PublicationHistory;
import java.util.Set;
import java.util.function.Consumer;

public interface TransformerContext {

    void setCurrentCycleId(long cycleId);

    long getCurrentCycleId();

    void setNowMillis(long now);

    long getNowMillis();

    void setFastlaneIds(Set<Integer> fastlaneIds);

    Set<Integer> getFastlaneIds();

    void setPinTitleSpecs(Set<String> pinnedTitleSpecs);

    Set<String> getPinTitleSpecs();

    TaggingLogger getLogger();

    TransformerConfig getConfig();

    TransformerMetricRecorder getMetricRecorder();

    TransformerCassandraHelper getCassandraHelper();

    TransformerCycleInterrupter getCycleInterrupter();

    TransformerFiles files();

    OctoberSkyData getOctoberSkyData();

    CupLibrary getCupLibrary();

    Consumer<PublicationHistory> getPublicationHistoryConsumer();
}
