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
import net.minecraft.entity.item.EntityEnderCrystal;
import org.spongepowered.api.entity.EnderCrystal;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.entity.explosive.IMixinExplosive;
import org.spongepowered.common.mixin.core.entity.MixinEntity;

import javax.annotation.Nullable;

@Mixin(EntityEnderCrystal.class)
public abstract class MixinEntityEnderCrystal extends MixinEntity implements EnderCrystal, IMixinExplosive {

    private static final String EXPLODE_TARGET = "Lnet/minecraft/world/World;createExplosion"
            + "(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;";
    private static final float EXPLOSION_STRENGTH = 6;

    // Explosive Impl

    @Override
    public void detonate() {
        setDead();
        onExplode(this.worldObj, null, this.posX, this.posY, this.posZ, EXPLOSION_STRENGTH, true);
    }

    @Redirect(method = "attackEntityFrom", at = @At(value = "INVOKE", target = EXPLODE_TARGET))
    private net.minecraft.world.Explosion onExplode(net.minecraft.world.World worldObj, @Nullable Entity nil, double x,
                                                    double y, double z, float strength, boolean smoking) {
        return detonate(Explosion.builder()
                .location(new Location<>((World) worldObj, new Vector3d(x, y, z)))
                .radius(strength)
                .shouldPlaySmoke(smoking))
                .orElse(null);
    }

}
