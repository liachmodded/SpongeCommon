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
package org.spongepowered.common.mixin.core.entity.item;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.entity.explosive.IMixinFusedExplosive;
import org.spongepowered.common.mixin.core.entity.MixinEntity;

import java.util.Optional;

@Mixin(EntityTNTPrimed.class)
public abstract class MixinEntityTNTPrimed extends MixinEntity implements PrimedTNT, IMixinFusedExplosive {

    private static final String EXPLOSION_TARGET = "Lnet/minecraft/world/World;createExplosion"
            + "(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;";
    private static final int DEFAULT_EXPLOSION_RADIUS = 4;

    @Shadow private int fuse;
    @Shadow private EntityLivingBase tntPlacedBy;
    @Shadow public abstract void explode();

    private int explosionRadius = DEFAULT_EXPLOSION_RADIUS;
    private int fuseDuration = 80;

    @Override
    public Optional<Living> getDetonator() {
        return Optional.ofNullable((Living) this.tntPlacedBy);
    }

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
        return this.fuse;
    }

    @Override
    public void setFuseTicksRemaining(int fuseTicks) {
        this.fuse = fuseTicks;
    }

    @Override
    public void prime() {
        // Since this entity is always primed, this just resets the fuse
        if (shouldPrime()) {
            setFuseTicksRemaining(this.fuseDuration);
        }
    }

    @Override
    public boolean isPrimed() {
        return !this.isDead;
    }

    @Override
    public void detonate() {
        setDead();
        explode();
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = EXPLOSION_TARGET))
    protected net.minecraft.world.Explosion onExplode(net.minecraft.world.World worldObj, Entity self, double x,
                                                      double y, double z, float strength, boolean smoking) {
        return detonate(Explosion.builder()
                .location(new Location<>((World) worldObj, new Vector3d(x, y, z)))
                .sourceExplosive(this)
                .radius(this.explosionRadius)
                .shouldPlaySmoke(smoking)
                .shouldBreakBlocks(smoking))
                .orElse(null);
    }

}
