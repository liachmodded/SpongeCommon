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
package org.spongepowered.common.event.tracking.phase;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.ExperienceOrb;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.event.tracking.CauseTracker;
import org.spongepowered.common.event.tracking.IPhaseState;
import org.spongepowered.common.event.tracking.ITickingState;
import org.spongepowered.common.event.tracking.PhaseContext;
import org.spongepowered.common.event.tracking.TrackingHelper;
import org.spongepowered.common.registry.type.event.InternalSpawnTypes;

import java.util.List;
import java.util.stream.Collectors;

public class SpawningPhase extends TrackingPhase {

    public enum State implements IPhaseState {
        DEATH_DROPS_SPAWNING,
        DROP_ITEM,
        CHUNK_SPAWNING,
        ;


        @Override
        public boolean canSwitchTo(IPhaseState state) {
            return this == CHUNK_SPAWNING && state instanceof ITickingState;
        }

        @Override
        public SpawningPhase getPhase() {
            return TrackingPhases.SPAWNING;
        }

    }

    @Override
    public void unwind(CauseTracker causeTracker, IPhaseState state, PhaseContext phaseContext) {
        final List<Entity> spawnedEntities = phaseContext.getCapturedEntitySupplier().get().orEmptyList();
        final List<Entity> spawnedItems = phaseContext.getCapturedItemsSupplier().get().orEmptyList();
        if (spawnedEntities.isEmpty() && spawnedItems.isEmpty()) {
            return;
        }
        if (state == State.DEATH_DROPS_SPAWNING) {
            final Entity dyingEntity = phaseContext.firstNamed(NamedCause.SOURCE, Entity.class).get();
            final DamageSource damageSource = phaseContext.firstNamed(TrackingHelper.DAMAGE_SOURCE, DamageSource.class).get();
            { // Items
                final Cause cause = Cause.source(EntitySpawnCause.builder()
                            .entity(dyingEntity)
                            .type(InternalSpawnTypes.DROPPED_ITEM)
                            .build())
                        .named(TrackingHelper.DAMAGE_SOURCE, damageSource)
                        .build();
                final List<EntitySnapshot> snapshots = spawnedItems.stream().map(Entity::createSnapshot).collect(Collectors.toList());
                DropItemEvent.Destruct
                        destruct =
                        SpongeEventFactory.createDropItemEventDestruct(cause, spawnedItems, snapshots, causeTracker.getWorld());
                if (!SpongeImpl.postEvent(destruct)) {
                    destruct.getEntities().forEach(entity -> causeTracker.getMixinWorld().forceSpawnEntity(entity));
                }
            }
            { // Entities
                final List<Entity> experience = spawnedEntities.stream().filter(entity -> entity instanceof ExperienceOrb).collect(Collectors.toList());
                if (!experience.isEmpty()) {
                    final List<EntitySnapshot> snapshots = experience.stream().map(Entity::createSnapshot).collect(Collectors.toList());
                    final Cause cause = Cause.source(EntitySpawnCause.builder()
                                .entity(dyingEntity)
                                .type(InternalSpawnTypes.EXPERIENCE)
                                .build())
                            .named(TrackingHelper.DAMAGE_SOURCE, damageSource)
                            .build();
                    SpawnEntityEvent event = SpongeEventFactory.createSpawnEntityEvent(cause, experience, snapshots, causeTracker.getWorld());
                    SpongeImpl.postEvent(event, spawnEvent -> spawnEvent.getEntities().forEach(entity -> causeTracker.getMixinWorld().forceSpawnEntity(entity)));
                }

                final List<Entity> other = spawnedEntities.stream().filter(entity -> !(entity instanceof ExperienceOrb)).collect(Collectors.toList());
                if (!other.isEmpty()) {
                    final List<EntitySnapshot> snapshots = other.stream().map(Entity::createSnapshot).collect(Collectors.toList());
                    final Cause cause = Cause.source(EntitySpawnCause.builder()
                                .entity(dyingEntity)
                                .type(InternalSpawnTypes.ENTITY_DEATH)
                                .build())
                            .named(TrackingHelper.DAMAGE_SOURCE, damageSource)
                            .build();
                    SpawnEntityEvent event = SpongeEventFactory.createSpawnEntityEvent(cause, other, snapshots, causeTracker.getWorld());
                    SpongeImpl.postEvent(event, spawnEvent -> spawnEvent.getEntities().forEach(entity -> causeTracker.getMixinWorld().forceSpawnEntity(entity)));
                }
            }

        }



    }

    public SpawningPhase(TrackingPhase parent) {
        super(parent);
    }

    @Override
    public boolean requiresBlockCapturing(IPhaseState currentState) {
        return currentState == State.CHUNK_SPAWNING;
    }

}
