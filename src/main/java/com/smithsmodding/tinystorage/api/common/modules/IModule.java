package com.smithsmodding.tinystorage.api.common.modules;

import com.smithsmodding.tinystorage.api.common.chest.IModularChest;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Tim on 22/06/2016.
 */
public interface IModule {

    /**
     * Getter for the ModId of the registering mod.
     * @return The mod Id of the registering mod.
     */
    String getRegisteringModId();

    /**
     * Method to get the ID for a module
     *
     * @return The unique ID
     */
    String getUniqueID();

    /**
     * Get the text to use of the tooltip of the Item
     *
     * @return The text string
     */
    String getDisplayText();

    /**
     * Method called when the host tile entity updates
     *
     * @param tileEntityModularChest The host chest
     */
    void onTileEntityUpdate(IModularChest tileEntityModularChest);

    /**
     * Method called when the module is installed within a chest
     *
     * @param tileEntityModularChest The chest the module was installed under
     */
    void onInstalled(IModularChest tileEntityModularChest);

    /**
     * Method called to check if the module can be installed in a given chest
     *
     * @param tileEntityModularChest The chest trying to have the module installed into
     * @return Whether the module can be installed
     */
    boolean canInstall(IModularChest tileEntityModularChest);

    /**
     * Method called to store and data to NBT
     *
     * @return The NBTTagCompound containing the data or null
     */
    NBTTagCompound writeToNBT();

    /**
     * Method called to load data of the module from NBT
     *
     * @param tag The tag to load the data from
     */
    void loadFromNBT(NBTTagCompound tag);
}
