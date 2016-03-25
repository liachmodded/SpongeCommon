/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.block;

import net.minecraft.block.BlockTNT;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.entity.explosive.IMixinFusedExplosive;

@Mixin(BlockTNT.class)
public abstract class MixinBlockTNT extends MixinBlock {

    private static final String PRIME_TARGET = "Lnet/minecraft/world/World;spawnEntityInWorld"
            + "(Lnet/minecraft/entity/Entity;)Z";
    private static final String PRIME_SOUND_TARGET = "Lnet/minecraft/world/World;playSoundAtEntity"
            + "(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V";
    private static final String REMOVE_POST_PRIME = "Lnet/minecraft/world/World;setBlockToAir"
            + "(Lnet/minecraft/util/BlockPos;)Z";

    private boolean primeCancelled;

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = PRIME_TARGET))
    public boolean onPrime(World world, Entity tnt) {
        this.primeCancelled = !((IMixinFusedExplosive) tnt).shouldPrime();
        return !primeCancelled && world.spawnEntityInWorld(tnt);
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = PRIME_SOUND_TARGET))
    public void onPrimeSound(World world, Entity tnt, String sound, float volume, float pitch) {
        if (!primeCancelled) {
            world.playSoundAtEntity(tnt, sound, volume, pitch);
        }
    }

    @Redirect(method = "onBlockActivated", at = @At(value = "INVOKE", target = REMOVE_POST_PRIME))
    public boolean onRemovePostPrime(World world, BlockPos pos) {
        return !primeCancelled && world.setBlockToAir(pos);
    }

}
