package com.smithsmodding.tinystorage.common.proxy;

import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.ITinyStorageAPI;
import com.smithsmodding.tinystorage.common.registry.GeneralRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;

/**
 * Created by Tim on 19/06/2016.
 */
public abstract class CommonProxy implements IProxy{

    @Override
    public void preInit() {
        TinyStorage.side = Side.SERVER;
    }

    @Override
    public void init() {
        TinyStorage.side = Side.SERVER;
    }

    @Override
    public void postInit() {
        TinyStorage.side = Side.SERVER;
    }

    @Override
    public void registerEventHandlers() {
    }

    @Override
    public void onLoadComplete() {
    }

    @Override
    public void registerIMCs() {
        TinyStorage.getLogger().info("Registering IMC callbacks");
        for (String s : GeneralRegistry.instance().IMCRequests.keySet()) {
            this.callbackRegistration(s, GeneralRegistry.instance().IMCRequests.get(s));
        }
    }

    private void callbackRegistration(String method, String modname) {
        String[] splitName = method.split("\\.");
        String methodName = splitName[splitName.length - 1];
        String className = method.substring(0, method.length() - methodName.length() - 1);
        TinyStorage.getLogger().info(String.format("Trying to reflect %s %s", className, methodName));
        try {
            Class reflectClass = Class.forName(className);
            Method reflectMethod = reflectClass.getDeclaredMethod(methodName, ITinyStorageAPI.class);
            reflectMethod.invoke(null, (ITinyStorageAPI) GeneralRegistry.instance());
            TinyStorage.getLogger().info(String.format("Success in registering %s", modname));
        } catch (ClassNotFoundException e) {
            TinyStorage.getLogger().warn(String.format("Could not find class %s", className));
        } catch (NoSuchMethodException e) {
            TinyStorage.getLogger().warn(String.format("Could not find method %s", methodName));
        } catch (Exception e) {
            TinyStorage.getLogger().warn(String.format("Exception while trying to access the method : %s", e.toString()));
        }
    }

    @Override
    public String getMinecraftVersion() {
        return Loader.instance().getMinecraftModContainer().getVersion();
    }

    @Override
    public CommonProxy getCommonProxy() {
        return this;
    }

}
