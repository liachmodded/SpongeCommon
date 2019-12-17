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
package org.spongepowered.common.registry.type;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.common.registry.util.CatalogMapExposer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCatalogRegistryModule<T extends CatalogType> implements CatalogRegistryModule<T> {

    protected final Map<CatalogKey, T> catalogTypeMap;
    
    protected AbstractCatalogRegistryModule() {
        this(new ConcurrentHashMap<>());
    }
    
    protected AbstractCatalogRegistryModule(Map<CatalogKey, T> map) {
        this.catalogTypeMap = map;
    }

    @Override
    public final Optional<T> get(CatalogKey id) {
        checkNotNull(id);
        return Optional.ofNullable(this.catalogTypeMap.get(id));
    }

    @Override
    public final Collection<T> getAll() {
        return Collections.unmodifiableCollection(this.catalogTypeMap.values());
    }
    
    protected final void register(CatalogKey key, T catalog) {
        this.catalogTypeMap.put(key, catalog);
    }
    
    protected final void register(String key, T catalog) {
        this.catalogTypeMap.put(CatalogKey.minecraft(key), catalog);
    }

    protected final void register(T catalog) {
        register(catalog.getKey(), catalog);
    }
    
    @CatalogMapExposer
    public Map<CatalogKey, T> provideCatalogMap() {
        return new HashMap<>(this.catalogTypeMap);
    }
    
    public void registerAdditionalCatalog(T catalog) {
        this.register(catalog);
    }
}
