package com.sandvoxel.generitech.tileentities;

import net.minecraft.item.ItemStack;

public class TileEntityPulverizer extends TileEntityMachineBase {

    public TileEntityPulverizer()
    {
        super(3);
    }


    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

}
