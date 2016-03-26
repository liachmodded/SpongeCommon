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
package org.spongepowered.common.mixin.core.entity.monster;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import org.spongepowered.api.entity.living.monster.Creeper;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.entity.IMixinGriefer;
import org.spongepowered.common.interfaces.entity.explosive.IMixinFusedExplosive;

import java.util.Optional;

@Mixin(EntityCreeper.class)
public abstract class MixinEntityCreeper extends MixinEntityMob implements Creeper, IMixinFusedExplosive {

    private static final String EXPLOSION_TARGET = "Lnet/minecraft/world/World;createExplosion"
            + "(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;";
    private static final String PRIME_TARGET = "Lnet/minecraft/entity/monster/EntityCreeper;playSound(Ljava/lang/String;FF)V";
    private static final int DEFAULT_EXPLOSION_RADIUS = 3;

    @Shadow private int timeSinceIgnited;
    @Shadow private int fuseTime;
    @Shadow private int explosionRadius;
    @Shadow public abstract void explode();
    @Shadow public abstract int getCreeperState();

    private int fuseDuration = 30;

    // FusedExplosive Impl

    @Override
    public Optional<Integer> getExplosionRadius() {
        return Optional.of(this.explosionRadius);
    }

    @Override
    public void setExplosionRadius(Optional<Integer> radius) {
        this.explosionRadius = radius.orElse(DEFAULT_EXPLOSION_RADIUS);
    }

    @Override
    public int getFuseDuration() {
        return this.fuseDuration;
    }

    @Override
    public void setFuseDuration(int fuseTicks) {
        this.fuseDuration = fuseTicks;
    }

    @Override
    public int getFuseTicksRemaining() {
        return this.fuseTime - this.timeSinceIgnited;
    }

    @Override
    public void setFuseTicksRemaining(int fuseTicks) {
        // Note: The creeper will detonate when timeSinceIgnited >= fuseTime
        // assuming it is within range of a player. Every tick that the creeper
        // is not within a range of a player, timeSinceIgnited is decremented
        // by one until zero.
        this.timeSinceIgnited = 0;
        this.fuseTime = fuseTicks;
    }

    private void prime(String sound, float volume, float pitch) {
        // Note: This has no effect unless a Player is within range
        if (shouldPrime()) {
            playSound(sound, volume, pitch);
            setFuseTicksRemaining(this.fuseDuration);
        } else {
            this.timeSinceIgnited = 0;
        }
    }

    @Override
    public void prime() {
        prime("creeper.primed", 1, 0.5f);
    }

    @Override
    public boolean isPrimed() {
        return getCreeperState() == 1;
    }

    @Override
    public void detonate() {
        explode();
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = EXPLOSION_TARGET))
    protected net.minecraft.world.Explosion onExplode(net.minecraft.world.World worldObj, Entity self, double x,
                                                      double y, double z, float strength, boolean smoking) {
        return detonate(Explosion.builder()
                .location(new Location<>((World) worldObj, new Vector3d(x, y, z)))
                .sourceExplosive(this)
                .radius(strength)
                .shouldPlaySmoke(smoking)
                .shouldBreakBlocks(smoking && ((IMixinGriefer) this).canGrief()))
                .orElse(null);
    }

    /**
     * Called when a Creeper first enters it's "primed" state.
     *
     * @param sound Prime sound
     * @param volume Volume level
     * @param pitch Sound pitch
     */
    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = PRIME_TARGET))
    protected void onPrime(EntityCreeper self, String sound, float volume, float pitch) {
        prime(sound, volume, pitch);
    }

}
