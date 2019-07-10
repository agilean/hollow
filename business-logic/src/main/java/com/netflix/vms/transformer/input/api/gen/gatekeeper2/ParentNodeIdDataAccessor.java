package com.netflix.vms.transformer.input.api.gen.gatekeeper2;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.data.AbstractHollowDataAccessor;
import com.netflix.hollow.core.index.key.PrimaryKey;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;

@SuppressWarnings("all")
public class ParentNodeIdDataAccessor extends AbstractHollowDataAccessor<ParentNodeId> {

    public static final String TYPE = "ParentNodeId";
    private Gk2StatusAPI api;

    public ParentNodeIdDataAccessor(HollowConsumer consumer) {
        super(consumer, TYPE);
        this.api = (Gk2StatusAPI)consumer.getAPI();
    }

    public ParentNodeIdDataAccessor(HollowReadStateEngine rStateEngine, Gk2StatusAPI api) {
        super(rStateEngine, TYPE);
        this.api = api;
    }

    public ParentNodeIdDataAccessor(HollowReadStateEngine rStateEngine, Gk2StatusAPI api, String ... fieldPaths) {
        super(rStateEngine, TYPE, fieldPaths);
        this.api = api;
    }

    public ParentNodeIdDataAccessor(HollowReadStateEngine rStateEngine, Gk2StatusAPI api, PrimaryKey primaryKey) {
        super(rStateEngine, TYPE, primaryKey);
        this.api = api;
    }

    @Override public ParentNodeId getRecord(int ordinal){
        return api.getParentNodeId(ordinal);
    }

}