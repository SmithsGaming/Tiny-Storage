package com.smithsmodding.tinystorage.common.proxy;

import com.smithsmodding.tinystorage.client.proxy.ClientProxy;

/**
 * Created by Tim on 19/06/2016.
 */
public interface IProxy {

    ClientProxy getClientProxy ();

    CommonProxy getCommonProxy();

    void initItemRendering();

    void initIileRendering();

    void registerEventHandlers ();

    void registerKeyBindings ();

    String getMinecraftVersion ();

    void preInit();

    void init();

    void postInit();

    void onLoadComplete();

    void registerIMCs();
}
