package com.chaosthedude.naturescompass;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chaosthedude.naturescompass.config.ConfigHandler;
import com.chaosthedude.naturescompass.gui.GuiHandler;
import com.chaosthedude.naturescompass.items.ItemNaturesCompass;
import com.chaosthedude.naturescompass.network.PacketAvailableBiomesSet;
import com.chaosthedude.naturescompass.network.PacketCompassSearch;
import com.chaosthedude.naturescompass.network.PacketRequestSync;
import com.chaosthedude.naturescompass.network.PacketSync;
import com.chaosthedude.naturescompass.network.PacketTeleport;
import com.chaosthedude.naturescompass.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(
        modid = NaturesCompass.MODID,
        name = NaturesCompass.NAME,
        version = NaturesCompass.VERSION,
        acceptedMinecraftVersions = "[1.7.10]")
public class NaturesCompass {

    public static final String MODID = "naturescompass";
    public static final String NAME = "Nature's Compass";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final Logger logger = LogManager.getLogger(MODID);

    public static SimpleNetworkWrapper network;
    public static ItemNaturesCompass naturesCompass;

    public static boolean canTeleport;

    @Instance(MODID)
    public static NaturesCompass instance;

    @SidedProxy(
            clientSide = "com.chaosthedude.naturescompass.client.ClientProxy",
            serverSide = "com.chaosthedude.naturescompass.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

        naturesCompass = new ItemNaturesCompass();
        GameRegistry.registerItem(naturesCompass, ItemNaturesCompass.NAME);

        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketCompassSearch.Handler.class, PacketCompassSearch.class, 0, Side.SERVER);
        network.registerMessage(PacketAvailableBiomesSet.Handler.class, PacketAvailableBiomesSet.class, 1, Side.CLIENT);
        network.registerMessage(PacketTeleport.Handler.class, PacketTeleport.class, 2, Side.SERVER);
        network.registerMessage(PacketRequestSync.Handler.class, PacketRequestSync.class, 3, Side.SERVER);
        network.registerMessage(PacketSync.Handler.class, PacketSync.class, 4, Side.CLIENT);

        proxy.registerEvents();
        proxy.registerModels();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        naturesCompass,
                        "SLS",
                        "LCL",
                        "SLS",
                        'C',
                        Items.compass,
                        'L',
                        Blocks.log,
                        'S',
                        "treeSapling"));
    }
}
