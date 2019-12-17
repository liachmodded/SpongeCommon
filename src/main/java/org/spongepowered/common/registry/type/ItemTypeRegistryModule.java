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

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.common.registry.RegistryHelper;
import org.spongepowered.common.registry.SpongeAdditionalCatalogRegistryModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ItemTypeRegistryModule extends AbstractCatalogRegistryModule<ItemType> implements SpongeAdditionalCatalogRegistryModule<ItemType>, AlternateCatalogRegistryModule<ItemType> {

    public final ItemStack NONE = (ItemStack) (Object) net.minecraft.item.ItemStack.EMPTY;
    public final ItemStackSnapshot NONE_SNAPSHOT = this.NONE.createSnapshot();

    public static ItemTypeRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Map<CatalogKey, ItemType> provideCatalogMap() {
        Map<CatalogKey, ItemType> itemTypeMap = super.provideCatalogMap();
        itemTypeMap.put(CatalogKey.minecraft("none"), this.NONE.getType());
        return itemTypeMap;
    }

    public void registerFromGameData(String id, ItemType itemType) {
        // todo
    }

    @Override
    public boolean allowsApiRegistration() {
        return false;
    }

    @Override
    public void registerDefaults() {
        this.setItemNone();
    }

    private void setItemNone() {
        RegistryHelper.setFinalStatic(ItemStackSnapshot.class, "NONE", this.NONE_SNAPSHOT);
    }

    ItemTypeRegistryModule() {
    }

    private static final class Holder {

        static final ItemTypeRegistryModule INSTANCE = new ItemTypeRegistryModule();
    }
}
