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
package org.spongepowered.common.data.processor.multi.entity;

import static org.spongepowered.common.data.util.DataUtil.getData;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableFuseData;
import org.spongepowered.api.data.manipulator.mutable.entity.FuseData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeFuseData;
import org.spongepowered.common.data.processor.common.AbstractMultiDataSingleTargetProcessor;
import org.spongepowered.common.interfaces.entity.explosive.IMixinFusedExplosive;

import java.util.Map;
import java.util.Optional;

public class FuseDataProcessor extends AbstractMultiDataSingleTargetProcessor<IMixinFusedExplosive, FuseData, ImmutableFuseData> {

    public FuseDataProcessor() {
        super(IMixinFusedExplosive.class);
    }

    @Override
    protected boolean doesDataExist(IMixinFusedExplosive dataHolder) {
        return true;
    }

    @Override
    protected boolean set(IMixinFusedExplosive explosive, Map<Key<?>, Object> keyValues) {
        Integer fuseDuration = (Integer) keyValues.get(Keys.FUSE_DURATION);
        Integer ticksRemaining = (Integer) keyValues.get(Keys.TICKS_REMAINING);

        explosive.setFuseDuration(fuseDuration);
        if (explosive.isPrimed()) {
            explosive.setFuseTicksRemaining(ticksRemaining);
        }
        
        return true;
    }

    @Override
    protected Map<Key<?>, ?> getValues(IMixinFusedExplosive explosive) {
        return ImmutableMap.of(
                Keys.FUSE_DURATION, explosive.getFuseDuration(),
                Keys.TICKS_REMAINING, explosive.getFuseTicksRemaining()
        );
    }

    @Override
    protected FuseData createManipulator() {
        return new SpongeFuseData();
    }

    @Override
    public Optional<FuseData> fill(DataContainer container, FuseData fuseData) {
        fuseData.set(Keys.FUSE_DURATION, getData(container, Keys.FUSE_DURATION));
        fuseData.set(Keys.TICKS_REMAINING, getData(container, Keys.TICKS_REMAINING));
        return Optional.of(fuseData);
    }

    @Override
    public DataTransactionResult remove(DataHolder dataHolder) {
        return DataTransactionResult.failNoData();
    }

}
