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
package org.spongepowered.common.registry.type.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.registry.ExtraClassCatalogRegistryModule;
import org.spongepowered.api.registry.util.CustomCatalogRegistration;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.entity.SpongeEntityType;
import org.spongepowered.common.entity.living.human.HumanEntity;
import org.spongepowered.common.mixin.invalid.api.mcp.entity.effect.EntityWeatherEffectMixin_API;
import org.spongepowered.common.registry.RegistryHelper;
import org.spongepowered.common.registry.SpongeAdditionalCatalogRegistryModule;
import org.spongepowered.common.registry.type.AbstractCatalogRegistryModule;
import org.spongepowered.common.registry.type.data.KeyRegistryModule;
import org.spongepowered.common.text.translation.SpongeTranslation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class EntityTypeRegistryModule extends AbstractCatalogRegistryModule<EntityType> implements ExtraClassCatalogRegistryModule<EntityType, Entity>, SpongeAdditionalCatalogRegistryModule<EntityType> {

    public final Map<Class<? extends Entity>, SpongeEntityType> entityClassToTypeMappings = Maps.newHashMap();
    private final Set<FutureRegistration> customEntities = new HashSet<>();

    public static EntityTypeRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void registerDefaults() {
        this.register("item", this.newEntityTypeFromName("Item"));
        this.register("experience_orb", this.newEntityTypeFromName("xp_orb"));
        this.register("area_effect_cloud", this.newEntityTypeFromName("area_effect_cloud"));
        this.register("dragon_fireball", this.newEntityTypeFromName("dragon_fireball"));
        this.register("leash_hitch", this.newEntityTypeFromName("leash_knot"));
        this.register("painting", this.newEntityTypeFromName("painting"));
        this.register("tipped_arrow", this.newEntityTypeFromName("arrow"));
        this.register("snowball", this.newEntityTypeFromName("snowball"));
        this.register("fireball", this.newEntityTypeFromName("LargeFireball", "fireball"));
        this.register("small_fireball", this.newEntityTypeFromName("small_fireball"));
        this.register("ender_pearl", this.newEntityTypeFromName("ender_pearl"));
        this.register("eye_of_ender", this.newEntityTypeFromName("eye_of_ender_signal"));
        this.register("splash_potion", this.newEntityTypeFromName("potion"));
        this.register("thrown_exp_bottle", this.newEntityTypeFromName("xp_bottle"));
        this.register("item_frame", this.newEntityTypeFromName("item_frame"));
        this.register("wither_skull", this.newEntityTypeFromName("wither_skull"));
        this.register("primed_tnt", this.newEntityTypeFromName("tnt"));
        this.register("falling_block", this.newEntityTypeFromName("falling_block"));
        this.register("firework", this.newEntityTypeFromName("fireworks_rocket"));
        this.register("armor_stand", this.newEntityTypeFromName("armor_stand"));
        this.register("boat", this.newEntityTypeFromName("boat"));
        this.register("rideable_minecart", this.newEntityTypeFromName("minecart"));
        this.register("chested_minecart", this.newEntityTypeFromName("chest_minecart"));
        this.register("furnace_minecart", this.newEntityTypeFromName("furnace_minecart"));
        this.register("tnt_minecart", this.newEntityTypeFromName("tnt_minecart"));
        this.register("hopper_minecart", this.newEntityTypeFromName("hopper_minecart"));
        this.register("mob_spawner_minecart", this.newEntityTypeFromName("spawner_minecart"));
        this.register("commandblock_minecart", this.newEntityTypeFromName("commandblock_minecart"));
        this.register("evocation_fangs", this.newEntityTypeFromName("evocation_fangs"));
        this.register("evocation_illager", this.newEntityTypeFromName("evocation_illager"));
        this.register("vex", this.newEntityTypeFromName("vex"));
        this.register("vindication_illager", this.newEntityTypeFromName("vindication_illager"));
        this.register("creeper", this.newEntityTypeFromName("creeper"));
        this.register("skeleton", this.newEntityTypeFromName("skeleton"));
        this.register("stray", this.newEntityTypeFromName("stray"));
        this.register("wither_skeleton", this.newEntityTypeFromName("wither_skeleton"));
        this.register("spider", this.newEntityTypeFromName("spider"));
        this.register("giant", this.newEntityTypeFromName("giant"));
        this.register("zombie", this.newEntityTypeFromName("zombie"));
        this.register("husk", this.newEntityTypeFromName("husk"));
        this.register("slime", this.newEntityTypeFromName("slime"));
        this.register("ghast", this.newEntityTypeFromName("ghast"));
        this.register("pig_zombie", this.newEntityTypeFromName("zombie_pigman"));
        this.register("enderman", this.newEntityTypeFromName("enderman"));
        this.register("cave_spider", this.newEntityTypeFromName("cave_spider"));
        this.register("silverfish", this.newEntityTypeFromName("silverfish"));
        this.register("blaze", this.newEntityTypeFromName("blaze"));
        this.register("magma_cube", this.newEntityTypeFromName("magma_cube"));
        this.register("ender_dragon", this.newEntityTypeFromName("ender_dragon"));
        this.register("wither", this.newEntityTypeFromName("wither"));
        this.register("bat", this.newEntityTypeFromName("bat"));
        this.register("witch", this.newEntityTypeFromName("witch"));
        this.register("endermite", this.newEntityTypeFromName("endermite"));
        this.register("guardian", this.newEntityTypeFromName("guardian"));
        this.register("elder_guardian", this.newEntityTypeFromName("elder_guardian"));
        this.register("pig", this.newEntityTypeFromName("pig"));
        this.register("sheep", this.newEntityTypeFromName("sheep"));
        this.register("cow", this.newEntityTypeFromName("cow"));
        this.register("chicken", this.newEntityTypeFromName("chicken"));
        this.register("squid", this.newEntityTypeFromName("squid"));
        this.register("wolf", this.newEntityTypeFromName("wolf"));
        this.register("mushroom_cow", this.newEntityTypeFromName("mooshroom"));
        this.register("snowman", this.newEntityTypeFromName("snowman"));
        this.register("ocelot", this.newEntityTypeFromName("Ocelot"));
        this.register("iron_golem", this.newEntityTypeFromName("villager_golem"));

        this.register("horse", this.newEntityTypeFromName("horse"));
        this.register("skeleton_horse", this.newEntityTypeFromName("skeleton_horse"));
        this.register("zombie_horse", this.newEntityTypeFromName("zombie_horse"));
        this.register("donkey", this.newEntityTypeFromName("donkey"));
        this.register("mule", this.newEntityTypeFromName("mule"));
        this.register("llama", this.newEntityTypeFromName("llama"));

        this.register("llama_spit", this.newEntityTypeFromName("llama_spit"));
        this.register("rabbit", this.newEntityTypeFromName("rabbit"));
        this.register("villager", this.newEntityTypeFromName("villager"));
        this.register("zombie_villager", this.newEntityTypeFromName("zombie_villager"));
        this.register("ender_crystal", this.newEntityTypeFromName("ender_crystal"));
        this.register("shulker", this.newEntityTypeFromName("shulker"));
        this.register("shulker_bullet", this.newEntityTypeFromName("shulker_bullet"));
        this.register("spectral_arrow", this.newEntityTypeFromName("spectral_arrow"));
        this.register("polar_bear", this.newEntityTypeFromName("polar_bear"));
        this.register("egg", new SpongeEntityType(-1, "egg", EggEntity.class, new SpongeTranslation("item.egg.name")));
        this.register("fishing_hook", new SpongeEntityType(-2, "FishingHook", FishingBobberEntity.class, new SpongeTranslation("item.fishingRod.name")));
        this.register("lightning", new SpongeEntityType(-3, "lightning", LightningBoltEntity.class, null));
        this.register("player", new SpongeEntityType(-5, "Player", ServerPlayerEntity.class, new SpongeTranslation("soundCategory.player")));
        this.register("complex_part", new SpongeEntityType(-6, "ComplexPart", EnderDragonPartEntity.class, null)); // todo
        this.register("human", this.registerCustomEntity(HumanEntity.class, "human", "Human", 300, null)); // TODO: Figure out what id to use, as negative ids no longer work
        //this.entityClassToTypeMappings.put("human", new SpongeEntityType(-6))

        this.register("parrot", this.newEntityTypeFromName("parrot"));
        this.register("illusion_illager", this.newEntityTypeFromName("illusion_illager"));
    }

    private SpongeEntityType newEntityTypeFromName(String spongeName, String mcName) {
        ResourceLocation resourceLoc = new ResourceLocation(mcName);
        Class<? extends Entity> cls = SpongeImplHooks.getEntityClass(resourceLoc);
        if (cls == null) {
            throw new IllegalArgumentException("No class mapping for entity name " + mcName);
        }
        final SpongeEntityType entityType = new SpongeEntityType(SpongeImplHooks.getEntityId(cls), spongeName, cls,
            new SpongeTranslation("entity." + SpongeImplHooks.getEntityTranslation(resourceLoc) + ".name"));
        KeyRegistryModule.getInstance().registerForEntityClass(cls);
        return entityType;
    }

    private SpongeEntityType newEntityTypeFromName(String name) {
        return this.newEntityTypeFromName(name, name);
    }

    private SpongeEntityType registerCustomEntity(Class<? extends Entity> entityClass, String entityName, String oldName, int entityId, Translation translation) {
        this.customEntities.add(new FutureRegistration(entityId, new ResourceLocation(SpongeImpl.ECOSYSTEM_ID, entityName), entityClass, oldName));
        return new SpongeEntityType(entityId, entityName, SpongeImpl.ECOSYSTEM_NAME, entityClass, translation);
    }

    @CustomCatalogRegistration
    public void registerCatalogs() {
        this.registerDefaults();
        RegistryHelper.mapFields(EntityTypes.class, fieldName -> {
            if (fieldName.equals("UNKNOWN")) {
                return SpongeEntityType.UNKNOWN;
            }
            SpongeEntityType entityType = this.catalogTypeMap.get(fieldName.toLowerCase(Locale.ENGLISH));
            this.entityClassToTypeMappings.put(entityType.entityClass, entityType);
            // remove old mapping
            this.catalogTypeMap.remove(fieldName.toLowerCase(Locale.ENGLISH));
            // add new mapping with minecraft id
            this.register(entityType.getId(), entityType);
            return entityType;
        });
    }

    @Override
    public boolean allowsApiRegistration() {
        return false;
    }

    @Override
    public void registerAdditionalCatalog(EntityType extraCatalog) {
        final SpongeEntityType spongeEntityType = (SpongeEntityType) extraCatalog;
        this.register(spongeEntityType);
        this.entityClassToTypeMappings.put(spongeEntityType.entityClass, spongeEntityType);
    }

    @Override
    public boolean hasRegistrationFor(Class<? extends Entity> mappedClass) {
        return false;
    }

    @Override
    public SpongeEntityType getForClass(Class<? extends Entity> clazz) {
        SpongeEntityType type = this.entityClassToTypeMappings.get(clazz);
        if (type == null) {
            SpongeImpl.getLogger().warn(String.format("No entity type is registered for class %s", clazz.getName()));

            type = (SpongeEntityType) EntityTypes.UNKNOWN;
            this.entityClassToTypeMappings.put(clazz, type);
        }
        return type;
    }

    EntityTypeRegistryModule() {
    }

    private static final class Holder {

        static final EntityTypeRegistryModule INSTANCE = new EntityTypeRegistryModule();
    }

    public Optional<EntityType> getEntity(Class<? extends org.spongepowered.api.entity.Entity> entityClass) {
        for (EntityType type : this.catalogTypeMap.values()) {
            if (entityClass.isAssignableFrom(type.getEntityClass())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public Set<FutureRegistration> getCustomEntities() {
        return ImmutableSet.copyOf(this.customEntities);
    }

    public static final class FutureRegistration {

        public final int id;
        public final ResourceLocation name;
        public final Class<? extends Entity> type;
        public final String oldName;

        FutureRegistration(int id, ResourceLocation name, Class<? extends Entity> type, String oldName) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.oldName = oldName;
        }
    }

}
