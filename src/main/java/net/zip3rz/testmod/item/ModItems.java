package net.zip3rz.testmod.item;

import net.zip3rz.testmod.TestMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> TROCOLON = ITEMS.register("trocolon",
            () -> new Item(new Item.Properties().)
    );

    public static final RegistryObject<Item> TROCOLON_PRENDIDO = ITEMS.register("trocolon_prendido",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
