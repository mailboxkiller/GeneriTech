/*
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 *
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the AlgorithmicsAnonymous Team ("AlgorithmicsAnonymous"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 *
 * AlgorithmicsAnonymous SOFTWARE LICENSE
 *
 * 1. Grant of License. AlgorithmicsAnonymous grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 *
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 *
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 *
 * Rent;
 *
 * Sell;
 *
 * Lease;
 *
 * Offer on a pay-per-play basis;
 *
 * Distribute for money or any other consideration; or
 *
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 *
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 *
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by AlgorithmicsAnonymous and is protected by United States copyright laws and international treaty provisions. AlgorithmicsAnonymous shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 *
 * 5. NO WARRANTIES. AlgorithmicsAnonymous DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. AlgorithmicsAnonymous DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF AlgorithmicsAnonymous ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF AlgorithmicsAnonymous.
 *
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against AlgorithmicsAnonymous, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, AlgorithmicsAnonymous SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF AlgorithmicsAnonymous OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package xyz.aadev.generitech.common.tileentities.machines;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.aadev.aalib.common.inventory.InternalInventory;
import xyz.aadev.aalib.common.inventory.InventoryOperation;
import xyz.aadev.aalib.common.util.InventoryHelper;
import xyz.aadev.generitech.api.util.MachineTier;
import xyz.aadev.generitech.client.gui.machines.GuiFurnace;
import xyz.aadev.generitech.client.gui.upgrade.GuiUpgradeScreen;
import xyz.aadev.generitech.common.container.machines.ContainerFurnace;
import xyz.aadev.generitech.common.container.upgrade.ContanierUpgradeStorage;
import xyz.aadev.generitech.common.tileentities.TileEntityMachineBase;
import xyz.aadev.generitech.common.util.DistributePowerToFace;

public class TileEntityFurnace extends TileEntityMachineBase implements ITickable {

    private InternalInventory internalInventory = new InternalInventory(this, 7);
    private BaseTeslaContainer container = new BaseTeslaContainer(0,50000,10000,10000);
    private boolean machineActive = false;
    private int smeltProgress = 0;
    private float internalTemp = 0f;
    private boolean canIdle = false;
    private boolean isSmeltPaused = false;
    private long powerUsage = 50;
    private MachineTier machineTier;
    private boolean ActiveTexture=false;

    @Override
    public long getPower() {
        return container.getStoredPower();
    }

    @Override
    public long getMaxPower() {
        return container.getCapacity();
    }

    @Override
    public void markForUpdate() {
        super.markForUpdate();
        this.markForLightUpdate();
    }

    @Override
    protected void syncDataTo(NBTTagCompound nbtTagCompound, SyncReason syncReason) {
        super.syncDataTo(nbtTagCompound, syncReason);
        nbtTagCompound.setFloat("internalTemp", internalTemp);
        nbtTagCompound.setInteger("smeltProgress", smeltProgress);
        nbtTagCompound.setTag("TeslaContainer", this.container.serializeNBT());

    }

    @Override
    protected void syncDataFrom(NBTTagCompound nbtTagCompound, SyncReason syncReason) {
        super.syncDataFrom(nbtTagCompound, syncReason);
        internalTemp = nbtTagCompound.getFloat("internalTemp");
        smeltProgress = nbtTagCompound.getInteger("smeltProgress");
        this.container = new BaseTeslaContainer(nbtTagCompound.getCompoundTag("TeslaContainer"));
    }
    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {
        return DistributePowerToFace.sidesnicememe(this,side,machineTier);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        int i = 0;
        for (final EnumFacing side : EnumFacing.VALUES) {
            if (direction == side && getSides()[i] == 0 && (index == 2 || index == 3)) {
                return true;
            }
            i++;
        }

        return false;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        int i = 0;
        for (final EnumFacing side : EnumFacing.VALUES) {
            if (direction == side && getSides()[i] == 1 && index == 0 && FurnaceRecipes.instance().getSmeltingResult(itemStackIn)!=ItemStack.EMPTY) {
                return true;
            }
            i++;
        }
        return false;
    }


    public boolean isSmeltPaused() {
        return isSmeltPaused;
    }

    public int getSmeltProgress() {
        return smeltProgress;
    }

    public boolean isMachineActive() {
        return machineActive;
    }

    @Override
    public IInventory getInternalInventory() {
        return internalInventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player) {
        if (guiId==3){
            return new GuiUpgradeScreen(player.inventory,this,getSides(),3,player);
        }
        return new GuiFurnace(player.inventory, this);

    }

    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player) {
        if (guiId==3){
            return new ContanierUpgradeStorage(player.inventory,this,3);
        }
        return new ContainerFurnace(player.inventory, this);
    }


    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeRotated() {
        return true;
    }

    private boolean canSmelt(ItemStack stack) {
        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stack);
        return itemstack != ItemStack.EMPTY;
    }

    public int getTemperature() {
        return (int) internalTemp;
    }

    public int getIdleTemp() {
        return getMaxTemperature() / 2;
    }

    public int getMultiplier() {
        int speed = (int) Math.floor((internalTemp - 100) / 100);
        if (speed < 0) speed = 0;
        return speed;
    }

    public int getMaxTemperature() {
        switch (MachineTier.byMeta(getBlockMetadata())) {
            case TIER_1:

            case TIER_2:
                return 1000;
            case TIER_3:
                return 1250;
            default:
                return 750;
        }
    }

    public float getTempRate() {
        switch (MachineTier.byMeta(getBlockMetadata())) {
            case TIER_1:

            case TIER_2:
                return 0.7f;
            case TIER_3:
                return 0.9f;
            default:
                return 0.5f;
        }
    }
    public boolean isSlotOpen(){
        if (ItemStack.areItemsEqual(FurnaceRecipes.instance().getSmeltingResult(internalInventory.getStackInSlot(0)), internalInventory.getStackInSlot(2)) || ItemStack.areItemsEqual(internalInventory.getStackInSlot(2),ItemStack.EMPTY)){
            return true;
        }
        return false;
    }

    public boolean getActiveTexture(){
        return ActiveTexture;
    }



    @Override
    public void update() {
        OverlayState();


        if (machineTier == null) {
            machineTier = MachineTier.byMeta(this.getBlockMetadata());
        }

        if ((container.getStoredPower() >= powerUsage && machineActive && !isSmeltPaused)&&(internalTemp < this.getMaxTemperature())) {

                internalTemp += getTempRate();
                container.takePower(powerUsage, false);
        } else if (container.getStoredPower() >= powerUsage && canIdle && internalTemp <= getIdleTemp()) {
            internalTemp += getTempRate();

            container.takePower(powerUsage, false);
        } else {
            if (internalTemp <= 0) {
                internalTemp = 0;
            } else {
                internalTemp -= getTempRate() / 2;
            }
        }


        if (container.getStoredPower() >= powerUsage && canIdle){
            ActiveTexture = true;
        }else if (container.getStoredPower() <= powerUsage && canIdle){
            ActiveTexture = false;
        }else if (machineActive != ActiveTexture){
            ActiveTexture = machineActive;
        }

        BlockPos pos = getPos();
        World worldIn = getWorld();
        if (worldIn.isBlockPowered(pos) && !canIdle) {
            canIdle = true;
        }
        if (!worldIn.isBlockPowered(pos) && canIdle) {
            canIdle = false;
        }



        if ((!ItemStack.areItemStacksEqual(internalInventory.getStackInSlot(0),ItemStack.EMPTY)&& ItemStack.areItemStacksEqual(internalInventory.getStackInSlot(1),ItemStack.EMPTY)) && isSlotOpen()) {
            ItemStack itemIn = internalInventory.getStackInSlot(0);
            ItemStack itemOut;

            //System.out.println("test");

            if (!canSmelt(itemIn))
                return;

            if (itemIn.getCount() - 1 <= 0) {
                itemOut = itemIn.copy();
                itemIn = ItemStack.EMPTY;
            } else {
                itemOut = itemIn.copy();

                itemOut.setCount(1);
                itemIn.shrink(1);
            }

            if (itemIn != ItemStack.EMPTY && itemIn.getCount() == 0) itemIn = ItemStack.EMPTY;
            if (itemOut.getCount() == 0) itemOut = ItemStack.EMPTY;

            internalInventory.setInventorySlotContents(0, itemIn);
            internalInventory.setInventorySlotContents(1, itemOut);



            machineActive = true;

            this.markForUpdate();
            this.markDirty();
        }

        ItemStack processItem = internalInventory.getStackInSlot(1);


        if (!ItemStack.areItemStacksEqual(processItem,ItemStack.EMPTY)&& getMultiplier() > 0 ) {

            smeltProgress += getMultiplier();

            if (smeltProgress > 1000) {
                smeltProgress = 1000;
                ItemStack outputStack = FurnaceRecipes.instance().getSmeltingResult(processItem.copy()).copy();
                if (InventoryHelper.addItemStackToInventory(outputStack, internalInventory, 2, 2, true) != ItemStack.EMPTY) {
                    isSmeltPaused = true;
                    return;
                }

                if (isSmeltPaused)
                    isSmeltPaused = !isSmeltPaused;

                InventoryHelper.addItemStackToInventory(outputStack, internalInventory, 2, 2);

                if (getStackInSlot(0) == ItemStack.EMPTY || !canSmelt(getStackInSlot(0))) machineActive = false;
                smeltProgress = 0;
                internalInventory.setInventorySlotContents(1, ItemStack.EMPTY);

                this.markForUpdate();
                this.markDirty();
            }
        } else {
            if (processItem != ItemStack.EMPTY && isSmeltPaused() && smeltProgress > 1000) {
                smeltProgress = 1000;
                ItemStack outputStack = FurnaceRecipes.instance().getSmeltingResult(processItem.copy()).copy();
                if (InventoryHelper.addItemStackToInventory(outputStack, internalInventory, 2, 2, true) != ItemStack.EMPTY) {
                    isSmeltPaused = true;
                    return;
                }

                InventoryHelper.addItemStackToInventory(outputStack, internalInventory, 2, 2);

                machineActive = false;
                smeltProgress = 0;
                internalInventory.setInventorySlotContents(1, ItemStack.EMPTY);

                this.markForUpdate();
                this.markDirty();
            }
        }


    }



    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        // This method is where other things will try to access your TileEntity's Tesla
        // capability. In the case of the analyzer, is a consumer, producer and holder so we
        // can allow requests that are looking for any of those things. This example also does
        // not care about which side is being accessed, however if you wanted to restrict which
        // side can be used, for example only allow power input through the back, that could be
        // done here.
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        // This method replaces the instanceof checks that would be used in an interface based
        // system. It can be used by other things to see if the TileEntity uses a capability or
        // not. This example is a Consumer, Producer and Holder, so we return true for all
        // three. This can also be used to restrict access on certain sides, for example if you
        // only accept power input from the bottom of the block, you would only return true for
        // Consumer if the facing parameter was down.
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;

        return super.hasCapability(capability, facing);
    }


}
