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
package org.spongepowered.common.registry.type.advancement;

import com.google.common.collect.ForwardingMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.common.SpongeImplHooks;

import java.util.HashMap;
import java.util.Map;

public final class AdvancementMap extends ForwardingMap<ResourceLocation, Advancement> {

    private static final long serialVersionUID = -1502089520911684373L;
    
    protected final Map<ResourceLocation, Advancement> delegate;

    protected AdvancementMap() {
        this(new HashMap<>());
    }
    
    protected AdvancementMap(Map<ResourceLocation, Advancement> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Map<ResourceLocation, Advancement> delegate() {
        return this.delegate;
    }

    @Override
    public Advancement put(ResourceLocation key, Advancement value) {
        final Advancement old = super.put(key, value);
        if (SpongeImplHooks.onServerThread() && old != value) {
            AdvancementRegistryModule.getInstance().registerSilently(value);
        }
        return old;
    }

    @Override
    public Advancement remove(Object key) {
        final Advancement old = super.remove(key);
        if (SpongeImplHooks.onServerThread() && old != null) {
            AdvancementRegistryModule.getInstance().remove(old);
        }
        return old;
    }

    @Override
    public void clear() {
        if (SpongeImplHooks.onServerThread()) {
            AdvancementRegistryModule.getInstance().clear();
        }
        super.clear();
    }
}
