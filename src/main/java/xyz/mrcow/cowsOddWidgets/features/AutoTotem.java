package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem {
    //Some of this code is borrowed from WURST Client
    //https://github.com/Wurst-Imperium/Wurst7/blob/master/src/main/java/net/wurstclient/hacks/AutoTotemHack.java

    private static int nextTickSlot;
    private static int totems;

    public static void update(MinecraftClient mc){
        finishMovingTotem(mc);

        PlayerInventory inventory = mc.player.getInventory();

        int nextTotemSlot = searchForTotems(inventory);

        ItemStack offhandStack = inventory.getStack(40);
        if(isTotem(offhandStack))
        {
            totems++;
            return;
        }

        if(mc.currentScreen instanceof HandledScreen
                && !(mc.currentScreen instanceof AbstractInventoryScreen))
            return;



        if(nextTotemSlot != -1)
            moveTotem(mc, nextTotemSlot, offhandStack);
    }

    private static void moveTotem(MinecraftClient mc, int nextTotemSlot, ItemStack offhandStack)
    {
        ScreenHandler container = mc.player.playerScreenHandler;
        boolean offhandEmpty = offhandStack.isEmpty();

        mc.interactionManager.clickSlot(container.syncId, nextTotemSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(container.syncId, 45, 0, SlotActionType.PICKUP, mc.player);

        if(!offhandEmpty)
            nextTickSlot = nextTotemSlot;
    }

    private static void finishMovingTotem(MinecraftClient mc)
    {
        if(nextTickSlot == -1)
            return;

        ScreenHandler container = mc.player.playerScreenHandler;

        mc.interactionManager.clickSlot(container.syncId, nextTickSlot, 0, SlotActionType.PICKUP, mc.player);
        nextTickSlot = -1;
    }

    private static int searchForTotems(PlayerInventory inventory)
    {
        totems = 0;
        int nextTotemSlot = -1;

        for(int slot = 0; slot <= 36; slot++)
        {
            if(!isTotem(inventory.getStack(slot)))
                continue;

            totems++;

            if(nextTotemSlot == -1)
                nextTotemSlot = slot < 9 ? slot + 36 : slot;
        }

        return nextTotemSlot;
    }

    private static boolean isTotem(ItemStack stack)
    {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

}
