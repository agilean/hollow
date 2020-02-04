package com.netflix.hollow.core.read.engine;

import java.util.List;

/**
 * @Author zxg
 * @Note
 * @Date 2020/2/3 12:16
 */
public class EndUpdateThread implements Runnable {
    private List<HollowTypeStateListener> list;

    public EndUpdateThread(List<HollowTypeStateListener> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for(HollowTypeStateListener listener : list) {
            listener.endUpdate();
        }
    }
}
