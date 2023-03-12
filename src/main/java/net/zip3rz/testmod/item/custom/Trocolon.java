package net.zip3rz.testmod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.NoteBlockEvent;

public class Trocolon extends Item {
    public Trocolon(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.WEAKNESS, 3200), null);
            player.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3200), null);
        }
        return super.use(level, player, hand);
        // TODO: Añadir
    }
}
