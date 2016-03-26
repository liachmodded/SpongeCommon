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
package org.spongepowered.common.interfaces.entity.explosive;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.ExplosiveEntityEvent;
import org.spongepowered.api.world.explosion.Explosion;

import java.util.Optional;

public interface IMixinExplosive {

    Optional<Integer> getExplosionRadius();

    void setExplosionRadius(Optional<Integer> radius);

    default Optional<net.minecraft.world.Explosion> detonate(Explosion.Builder builder) {
        ExplosiveEntityEvent.Detonate event = SpongeEventFactory.createExplosiveEntityEventDetonate(
                Cause.of(NamedCause.source(this)), builder, builder.build(), (Explosive) this
        );
        if (!Sponge.getEventManager().post(event)) {
            Explosion explosion = event.getExplosionBuilder().build();
            ((Explosive) this).getWorld().triggerExplosion(explosion);
            return Optional.of((net.minecraft.world.Explosion) explosion);
        }
        return Optional.empty();
    }

}
