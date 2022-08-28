package com.chaosthedude.naturescompass.proxy;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {

    public void registerEvents() {
        FMLCommonHandler.instance().bus().register(new ServerTickHandler());
    }

    public void registerModels() {}
}
