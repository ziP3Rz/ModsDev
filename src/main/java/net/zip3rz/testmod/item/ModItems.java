package net.zip3rz.testmod.item;

import net.minecraft.world.food.FoodProperties;
import net.zip3rz.testmod.item.custom.Trocolon;
import net.zip3rz.testmod.TestMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> TROCOLON = ITEMS.register("trocolon",
            // TODO: buildear properties )
            () -> new Trocolon(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).alwaysEat().build()))
    );


    public static final RegistryObject<Item> TROCOLON_PRENDIDO = ITEMS.register("trocolon_prendido",
            () -> new Trocolon(new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
