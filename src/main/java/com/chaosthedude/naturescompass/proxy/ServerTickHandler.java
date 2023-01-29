package com.chaosthedude.naturescompass.proxy;

import com.chaosthedude.naturescompass.util.WorldWorkerManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class ServerTickHandler {

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        WorldWorkerManager.tick(event.phase == Phase.START);
    }
}
