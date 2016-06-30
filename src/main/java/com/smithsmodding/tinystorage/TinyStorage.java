package com.smithsmodding.tinystorage;

import com.google.common.base.Stopwatch;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.core.TinyStorageLog;
import com.smithsmodding.tinystorage.common.init.TinyStorageInitialiser;
import com.smithsmodding.tinystorage.common.proxy.IProxy;
import com.smithsmodding.tinystorage.common.registry.GeneralRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tim on 19/06/2016.
 */
@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION, dependencies = "required-after:SmithsCore;")
public class TinyStorage {

    //Instance of the mod used for internal things
    @Mod.Instance(References.MOD_ID)
    public static TinyStorage instance;
    //Proxy registration
    @SidedProxy(clientSide = References.CLIENT_PROXY_CLASS, serverSide = References.SERVER_PROXY_CLASS)
    public static IProxy proxy;
    //Current loaded side
    public static Side side;
    //Logger
    private static TinyStorageLog logger = new TinyStorageLog();
    //Are we in a development environment?
    public boolean developmentEnvironment;

    public static TinyStorageLog getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Staring server init");
        TinyStorageInitialiser.serverStarting(event);
        logger.info("Finished server init after: " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        watch.stop();
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Staring server stopping");
        TinyStorageInitialiser.serverStopping(event);
        logger.info("Finished server stopping after: " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        watch.stop();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Starting pre init - Preparing to store all the things!");
        proxy.preInit();
        TinyStorageInitialiser.preInit(event);
        logger.info("Finished pre init after: " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        watch.stop();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Starting init - Continuing to store all the things!");
        proxy.init();
        TinyStorageInitialiser.init(event);
        logger.info("Finished init after: " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        watch.stop();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Starting post init - Nearly there now!");
        proxy.postInit();
        TinyStorageInitialiser.postInit(event);
        logger.info("Finished post init after: " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        logger.info("Loaded Tiny Storage");
        watch.stop();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        logger.info("FML Load Complete");
        proxy.registerIMCs();
        proxy.onLoadComplete();
    }

    @Mod.EventHandler
    public void IMCEvent(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
            if (!imcMessage.isStringMessage()) {
                continue;
            }
            if (imcMessage.key.equalsIgnoreCase("register")) {
                logger.info(String.format("Receiving registration request from [ %s ] for method %s", imcMessage.getSender(), imcMessage.getStringValue()));
                GeneralRegistry.instance().addIMCRequest(imcMessage.getStringValue(), imcMessage.getSender());
            }
        }
    }
}
