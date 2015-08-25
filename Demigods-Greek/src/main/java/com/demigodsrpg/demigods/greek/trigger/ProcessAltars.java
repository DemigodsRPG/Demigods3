package com.demigodsrpg.demigods.greek.trigger;

import com.demigodsrpg.demigods.engine.trigger.Trigger;
import com.demigodsrpg.demigods.greek.structure.Altar;

public class ProcessAltars implements Trigger {
    @Override
    public void processSync() {
        // Update Atlars
        Altar.Util.generateAltars();
    }

    @Override
    public void processAsync() {
        // Process Atlars
        Altar.Util.processNewChunks();
    }
}
