package net.zip3rz.testmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zip3rz.testmod.TestMod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab TEST_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        TEST_TAB = event.registerCreativeModeTab(new ResourceLocation(TestMod.MOD_ID, "test_tab"),
                builder -> builder.icon( () -> new ItemStack(ModItems.TROCOLON.get()))
                        .title(Component.translatable("creativemodetab.test_tab"))
        );
    }
}
