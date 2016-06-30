package com.smithsmodding.tinystorage.common.proxy;

import com.smithsmodding.tinystorage.client.proxy.ClientProxy;

/**
 * Created by Tim on 19/06/2016.
 */
public class ServerProxy extends CommonProxy {

    @Override
    public ClientProxy getClientProxy() {
        return null;
    }

    @Override
    public void initItemRendering() {
    }

    @Override
    public void initIileRendering() {

    }

    @Override
    public void registerKeyBindings() {
    }

}
