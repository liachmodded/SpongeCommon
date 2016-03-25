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
package org.spongepowered.common.mixin.core.entity.projectile;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import org.spongepowered.api.entity.projectile.explosive.fireball.LargeFireball;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.interfaces.entity.IMixinGriefer;
import org.spongepowered.common.interfaces.entity.explosive.IMixinExplosive;

import javax.annotation.Nullable;

@Mixin(EntityLargeFireball.class)
public abstract class MixinEntityLargeFireball extends MixinEntityFireball implements LargeFireball, IMixinExplosive {

    private static final String EXPLOSION_TARGET =
        "Lnet/minecraft/world/World;newExplosion(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;";

    @Shadow public int explosionPower;

    private float damage = 6.0f;

    @ModifyArg(method = "onImpact(Lnet/minecraft/util/MovingObjectPosition;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"))
    protected float onAttackEntityFrom(float amount) {
        return this.damage;
    }

    @Override
    public void readFromNbt(NBTTagCompound compound) {
        super.readFromNbt(compound);
        if (compound.hasKey(NbtDataUtil.PROJECTILE_DAMAGE_AMOUNT)) {
            this.damage = compound.getFloat(NbtDataUtil.PROJECTILE_DAMAGE_AMOUNT);
        }
    }

    @Override
    public void writeToNbt(NBTTagCompound compound) {
        super.writeToNbt(compound);
        compound.setFloat(NbtDataUtil.PROJECTILE_DAMAGE_AMOUNT, this.damage);
    }

    @Redirect(method = "onImpact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Ljava/lang/String;)Z"))
    private boolean onCanGrief(GameRules gameRules, String rule) {
        return gameRules.getBoolean(rule) && ((IMixinGriefer) this).canGrief();
    }

    // Explosive Impl

    @Override
    public void detonate() {
        onExplode(this.worldObj, null, this.posX, this.posY, this.posZ, explosionPower, true, true);
        setDead();
    }

    @Redirect(method = "onImpact", at = @At(value = "INVOKE", target = EXPLOSION_TARGET))
    protected net.minecraft.world.Explosion onExplode(net.minecraft.world.World worldObj, @Nullable Entity nil,
                                                      double x, double y, double z, float strength, boolean flaming,
                                                      boolean smoking) {
        boolean griefer = ((IMixinGriefer) this).canGrief();
        return detonate(Explosion.builder()
                .location(new Location<>((World) worldObj, new Vector3d(x, y, z)))
                .sourceExplosive(this)
                .radius(strength)
                .canCauseFire(flaming && griefer)
                .shouldPlaySmoke(smoking && griefer))
                .orElse(null);
    }

}
