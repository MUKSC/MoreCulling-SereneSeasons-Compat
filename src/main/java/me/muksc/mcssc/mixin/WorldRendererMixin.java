package me.muksc.mcssc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import sereneseasons.season.SeasonHooks;

@Debug(export = true)
@Mixin(value = WorldRenderer.class, priority = 1500)
public abstract class WorldRendererMixin {
    @Unique
    private World world;
    @Unique
    private RegistryEntry<Biome> biome;

    @WrapOperation(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/registry/entry/RegistryEntry;"))
    private RegistryEntry<Biome> storeBiome(World instance, BlockPos blockPos, Operation<RegistryEntry<Biome>> original) {
        RegistryEntry<Biome> result = original.call(instance, blockPos);
        world = instance;
        biome = result;
        return result;
    }

    @ModifyExpressionValue(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;hasPrecipitation()Z"))
    private boolean hasPrecipitation(boolean original) {
        return SeasonHooks.hasPrecipitationSeasonal(world, biome);
    }
}
