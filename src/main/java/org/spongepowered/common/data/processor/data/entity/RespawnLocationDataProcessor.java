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
package org.spongepowered.common.data.processor.data.entity;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableRespawnLocation;
import org.spongepowered.api.data.manipulator.mutable.entity.RespawnLocationData;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeRespawnLocationData;
import org.spongepowered.common.data.processor.common.AbstractSingleDataSingleTargetProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeMapValue;
import org.spongepowered.common.data.value.mutable.SpongeMapValue;
import org.spongepowered.common.entity.player.ISpongeUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RespawnLocationDataProcessor extends
        AbstractSingleDataSingleTargetProcessor<User, Map<UUID, Tuple<Vector3d, Boolean>>, MapValue<UUID, Tuple<Vector3d, Boolean>>, RespawnLocationData, ImmutableRespawnLocation> {

    public RespawnLocationDataProcessor() {
        super(Keys.RESPAWN_LOCATIONS, User.class);
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        if (container instanceof ISpongeUser) {
            ImmutableMap<UUID, Tuple<Vector3d, Boolean>> removed = ((ISpongeUser) container).removeAllBeds();
            if (!removed.isEmpty()) {
                return DataTransactionResult.successRemove(constructImmutableValue(removed));
            } else {
                return DataTransactionResult.successNoData();
            }
        }
        return DataTransactionResult.failNoData();
    }

    @Override
    protected boolean set(User user, Map<UUID, Tuple<Vector3d, Boolean>> value) {
        if (user instanceof ISpongeUser) {
            return ((ISpongeUser) user).setBedLocations(value);
        }
        return false;
    }

    @Override
    protected Optional<Map<UUID, Tuple<Vector3d, Boolean>>> getVal(User user) {
        if (user instanceof ISpongeUser) {
            return Optional.of(((ISpongeUser) user).getBedlocations());
        }
        return Optional.empty();
    }

    @Override
    protected MapValue<UUID, Tuple<Vector3d, Boolean>> constructValue(Map<UUID, Tuple<Vector3d, Boolean>> actualValue) {
        return new SpongeMapValue<>(Keys.RESPAWN_LOCATIONS, actualValue);
    }

    @Override
    protected ImmutableMapValue<UUID, Tuple<Vector3d, Boolean>> constructImmutableValue(Map<UUID, Tuple<Vector3d, Boolean>> value) {
        return new ImmutableSpongeMapValue<>(Keys.RESPAWN_LOCATIONS, value);
    }

    @Override
    protected RespawnLocationData createManipulator() {
        return new SpongeRespawnLocationData();
    }

}
