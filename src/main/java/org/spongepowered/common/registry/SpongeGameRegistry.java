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
package org.spongepowered.common.registry;

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeCalculator;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.attribute.Operation;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.tileentity.Banner;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.block.tileentity.DaylightDetector;
import org.spongepowered.api.block.tileentity.EnchantmentTable;
import org.spongepowered.api.block.tileentity.EndPortal;
import org.spongepowered.api.block.tileentity.EnderChest;
import org.spongepowered.api.block.tileentity.MobSpawner;
import org.spongepowered.api.block.tileentity.Note;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.Skull;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.carrier.BrewingStand;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.block.tileentity.carrier.Dispenser;
import org.spongepowered.api.block.tileentity.carrier.Dropper;
import org.spongepowered.api.block.tileentity.carrier.Furnace;
import org.spongepowered.api.block.tileentity.carrier.Hopper;
import org.spongepowered.api.data.ImmutableDataRegistry;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulatorRegistry;
import org.spongepowered.api.data.manipulator.immutable.ImmutableDisplayNameData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableSkullData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableCareerData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableEyeLocationData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableHealthData;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableVelocityData;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.data.manipulator.mutable.SkullData;
import org.spongepowered.api.data.manipulator.mutable.entity.CareerData;
import org.spongepowered.api.data.manipulator.mutable.entity.EyeLocationData;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.meta.PatternLayer;
import org.spongepowered.api.data.type.Art;
import org.spongepowered.api.data.type.Arts;
import org.spongepowered.api.data.type.BannerPatternShape;
import org.spongepowered.api.data.type.BannerPatternShapes;
import org.spongepowered.api.data.type.Career;
import org.spongepowered.api.data.type.Careers;
import org.spongepowered.api.data.type.CoalType;
import org.spongepowered.api.data.type.CoalTypes;
import org.spongepowered.api.data.type.ComparatorType;
import org.spongepowered.api.data.type.CookedFish;
import org.spongepowered.api.data.type.CookedFishes;
import org.spongepowered.api.data.type.DirtType;
import org.spongepowered.api.data.type.DisguisedBlockType;
import org.spongepowered.api.data.type.DoublePlantType;
import org.spongepowered.api.data.type.DoublePlantTypes;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.Fish;
import org.spongepowered.api.data.type.Fishes;
import org.spongepowered.api.data.type.GoldenApple;
import org.spongepowered.api.data.type.Hinge;
import org.spongepowered.api.data.type.HorseColor;
import org.spongepowered.api.data.type.HorseColors;
import org.spongepowered.api.data.type.HorseStyle;
import org.spongepowered.api.data.type.HorseStyles;
import org.spongepowered.api.data.type.HorseVariant;
import org.spongepowered.api.data.type.HorseVariants;
import org.spongepowered.api.data.type.NotePitch;
import org.spongepowered.api.data.type.NotePitches;
import org.spongepowered.api.data.type.OcelotType;
import org.spongepowered.api.data.type.OcelotTypes;
import org.spongepowered.api.data.type.PlantType;
import org.spongepowered.api.data.type.PlantTypes;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.PrismarineType;
import org.spongepowered.api.data.type.Profession;
import org.spongepowered.api.data.type.Professions;
import org.spongepowered.api.data.type.QuartzType;
import org.spongepowered.api.data.type.RabbitType;
import org.spongepowered.api.data.type.RabbitTypes;
import org.spongepowered.api.data.type.RailDirection;
import org.spongepowered.api.data.type.SandstoneType;
import org.spongepowered.api.data.type.ShrubType;
import org.spongepowered.api.data.type.ShrubTypes;
import org.spongepowered.api.data.type.SkeletonType;
import org.spongepowered.api.data.type.SkeletonTypes;
import org.spongepowered.api.data.type.SkullType;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.data.type.SlabType;
import org.spongepowered.api.data.type.StairShape;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.data.type.WallType;
import org.spongepowered.api.data.value.ValueBuilder;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.ScoreboardBuilder;
import org.spongepowered.api.scoreboard.TeamBuilder;
import org.spongepowered.api.scoreboard.Visibilities;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.ObjectiveBuilder;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.service.persistence.SerializationService;
import org.spongepowered.api.statistic.BlockStatistic;
import org.spongepowered.api.statistic.EntityStatistic;
import org.spongepowered.api.statistic.ItemStatistic;
import org.spongepowered.api.statistic.Statistic;
import org.spongepowered.api.statistic.StatisticBuilder;
import org.spongepowered.api.statistic.StatisticFormat;
import org.spongepowered.api.statistic.StatisticGroup;
import org.spongepowered.api.statistic.TeamStatistic;
import org.spongepowered.api.statistic.achievement.Achievement;
import org.spongepowered.api.statistic.achievement.AchievementBuilder;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.selector.ArgumentHolder;
import org.spongepowered.api.text.selector.ArgumentType;
import org.spongepowered.api.text.selector.ArgumentTypes;
import org.spongepowered.api.text.selector.SelectorType;
import org.spongepowered.api.text.selector.SelectorTypes;
import org.spongepowered.api.text.selector.Selectors;
import org.spongepowered.api.text.sink.MessageSinks;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.explosion.ExplosionBuilder;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.extent.ExtentBufferFactory;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.gen.PopulatorFactory;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.configuration.CatalogTypeTypeSerializer;
import org.spongepowered.common.configuration.SpongeConfig;
import org.spongepowered.common.data.SpongeDataRegistry;
import org.spongepowered.common.data.SpongeImmutableRegistry;
import org.spongepowered.common.data.builder.block.data.SpongePatternLayerBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeBannerBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeBrewingStandBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeChestBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeCommandBlockBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeComparatorBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeDaylightBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeDispenserBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeDropperBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeEnchantmentTableBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeEndPortalBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeEnderChestBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeFurnaceBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeHopperBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeMobSpawnerBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeNoteBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeSignBuilder;
import org.spongepowered.common.data.builder.block.tileentity.SpongeSkullBuilder;
import org.spongepowered.common.data.key.KeyRegistry;
import org.spongepowered.common.data.manipulator.immutable.ImmutableSpongeDisplayNameData;
import org.spongepowered.common.data.manipulator.immutable.ImmutableSpongeSkullData;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeCareerData;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeEyeLocationData;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeHealthData;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeVelocityData;
import org.spongepowered.common.data.manipulator.immutable.tileentity.ImmutableSpongeSignData;
import org.spongepowered.common.data.manipulator.mutable.SpongeDisplayNameData;
import org.spongepowered.common.data.manipulator.mutable.SpongeSkullData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeCareerData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeEyeLocationData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeHealthData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeVelocityData;
import org.spongepowered.common.data.manipulator.mutable.tileentity.SpongeSignData;
import org.spongepowered.common.data.processor.data.DisplayNameDataProcessor;
import org.spongepowered.common.data.processor.data.SkullDataProcessor;
import org.spongepowered.common.data.processor.data.entity.CareerDataProcessor;
import org.spongepowered.common.data.processor.data.entity.EyeLocationDataProcessor;
import org.spongepowered.common.data.processor.data.entity.HealthDataProcessor;
import org.spongepowered.common.data.processor.data.entity.VelocityDataProcessor;
import org.spongepowered.common.data.processor.data.tileentity.SignDataProcessor;
import org.spongepowered.common.data.processor.value.DisplayNameValueProcessor;
import org.spongepowered.common.data.processor.value.DisplayNameVisibleValueProcessor;
import org.spongepowered.common.data.processor.value.SkullValueProcessor;
import org.spongepowered.common.data.processor.value.entity.CareerValueProcessor;
import org.spongepowered.common.data.processor.value.entity.EyeHeightValueProcessor;
import org.spongepowered.common.data.processor.value.entity.EyeLocationValueProcessor;
import org.spongepowered.common.data.processor.value.entity.HealthValueProcessor;
import org.spongepowered.common.data.processor.value.entity.MaxHealthValueProcessor;
import org.spongepowered.common.data.processor.value.entity.VelocityValueProcessor;
import org.spongepowered.common.data.processor.value.tileentity.SignLinesValueProcessor;
import org.spongepowered.common.data.type.SpongeCookedFish;
import org.spongepowered.common.data.type.SpongeNotePitch;
import org.spongepowered.common.data.type.SpongeSkullType;
import org.spongepowered.common.data.type.SpongeTreeType;
import org.spongepowered.common.data.value.SpongeValueBuilder;
import org.spongepowered.common.effect.particle.SpongeParticleEffectBuilder;
import org.spongepowered.common.effect.particle.SpongeParticleType;
import org.spongepowered.common.effect.sound.SpongeSound;
import org.spongepowered.common.entity.SpongeCareer;
import org.spongepowered.common.entity.SpongeEntityConstants;
import org.spongepowered.common.entity.SpongeEntityInteractionType;
import org.spongepowered.common.entity.SpongeEntityMeta;
import org.spongepowered.common.entity.SpongeEntityType;
import org.spongepowered.common.entity.SpongeProfession;
import org.spongepowered.common.entity.SpongeTransform;
import org.spongepowered.common.entity.living.human.EntityHuman;
import org.spongepowered.common.item.SpongeCoalType;
import org.spongepowered.common.item.SpongeFireworkBuilder;
import org.spongepowered.common.item.SpongeItemStackBuilder;
import org.spongepowered.common.item.merchant.SpongeTradeOfferBuilder;
import org.spongepowered.common.potion.SpongePotionBuilder;
import org.spongepowered.common.rotation.SpongeRotation;
import org.spongepowered.common.scoreboard.SpongeDisplaySlot;
import org.spongepowered.common.scoreboard.SpongeVisibility;
import org.spongepowered.common.scoreboard.builder.SpongeObjectiveBuilder;
import org.spongepowered.common.scoreboard.builder.SpongeScoreboardBuilder;
import org.spongepowered.common.scoreboard.builder.SpongeTeamBuilder;
import org.spongepowered.common.status.SpongeFavicon;
import org.spongepowered.common.text.SpongeTextFactory;
import org.spongepowered.common.text.chat.SpongeChatType;
import org.spongepowered.common.text.format.SpongeTextColor;
import org.spongepowered.common.text.format.SpongeTextStyle;
import org.spongepowered.common.text.selector.SpongeArgumentHolder;
import org.spongepowered.common.text.selector.SpongeSelectorFactory;
import org.spongepowered.common.text.selector.SpongeSelectorType;
import org.spongepowered.common.text.sink.SpongeMessageSinkFactory;
import org.spongepowered.common.text.translation.SpongeTranslation;
import org.spongepowered.common.weather.SpongeWeather;
import org.spongepowered.common.world.SpongeDimensionType;
import org.spongepowered.common.world.SpongeExplosionBuilder;
import org.spongepowered.common.world.SpongeWorldBuilder;
import org.spongepowered.common.world.extent.SpongeExtentBufferFactory;
import org.spongepowered.common.world.gen.WorldGeneratorRegistry;
import org.spongepowered.common.world.type.SpongeWorldTypeEnd;
import org.spongepowered.common.world.type.SpongeWorldTypeNether;
import org.spongepowered.common.world.type.SpongeWorldTypeOverworld;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SpongeGameRegistry implements GameRegistry {
    static {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(CatalogType.class), new CatalogTypeTypeSerializer());
    }

    private final Map<String, BiomeType> biomeTypeMappings = Maps.newHashMap();

    public static final Map<Class<? extends WorldProvider>, SpongeConfig<SpongeConfig.DimensionConfig>> dimensionConfigs = Maps.newHashMap();

    public static final Map<String, TextColor> textColorMappings = Maps.newHashMap();
    public static final Map<EnumChatFormatting, SpongeTextColor> enumChatColor = Maps.newEnumMap(EnumChatFormatting.class);

    public static final Map<String, Visibility> visibilityMappings = Maps.newHashMap();
    public static final Map<Team.EnumVisible, SpongeVisibility> enumVisible = Maps.newEnumMap(Team.EnumVisible.class);

    public static final ImmutableMap<String, TextStyle.Base> textStyleMappings = new ImmutableMap.Builder<String, TextStyle.Base>()
            .put("bold", SpongeTextStyle.of(EnumChatFormatting.BOLD))
            .put("italic", SpongeTextStyle.of(EnumChatFormatting.ITALIC))
            .put("underline", SpongeTextStyle.of(EnumChatFormatting.UNDERLINE))
            .put("strikethrough", SpongeTextStyle.of(EnumChatFormatting.STRIKETHROUGH))
            .put("obfuscated", SpongeTextStyle.of(EnumChatFormatting.OBFUSCATED))
            .put("reset", SpongeTextStyle.of(EnumChatFormatting.RESET))
            .build();
    private static final ImmutableMap<String, ChatType> chatTypeMappings = new ImmutableMap.Builder<String, ChatType>()
            .put("chat", new SpongeChatType((byte) 0))
            .put("system", new SpongeChatType((byte) 1))
            .put("action_bar", new SpongeChatType((byte) 2))
            .build();

    private static final ImmutableMap<String, Rotation> rotationMappings = new ImmutableMap.Builder<String, Rotation>()
            .put("top", new SpongeRotation(0))
            .put("top_right", new SpongeRotation(45))
            .put("right", new SpongeRotation(90))
            .put("bottom_right", new SpongeRotation(135))
            .put("bottom", new SpongeRotation(180))
            .put("bottom_left", new SpongeRotation(225))
            .put("left", new SpongeRotation(270))
            .put("top_left", new SpongeRotation(315))
            .build();
    public static final ImmutableBiMap<Direction, EnumFacing> directionMap = ImmutableBiMap.<Direction, EnumFacing>builder()
            .put(Direction.NORTH, EnumFacing.NORTH)
            .put(Direction.EAST, EnumFacing.EAST)
            .put(Direction.SOUTH, EnumFacing.SOUTH)
            .put(Direction.WEST, EnumFacing.WEST)
            .put(Direction.UP, EnumFacing.UP)
            .put(Direction.DOWN, EnumFacing.DOWN)
            .build();
    public static final ImmutableMap<String, GameMode> gameModeMappings = new ImmutableMap.Builder<String, GameMode>()
            .put("survival", (GameMode) (Object) WorldSettings.GameType.SURVIVAL)
            .put("creative", (GameMode) (Object) WorldSettings.GameType.CREATIVE)
            .put("adventure", (GameMode) (Object) WorldSettings.GameType.ADVENTURE)
            .put("spectator", (GameMode) (Object) WorldSettings.GameType.SPECTATOR)
            .put("not_set", (GameMode) (Object) WorldSettings.GameType.NOT_SET)
            .build();
    private static final ImmutableMap<String, Difficulty> difficultyMappings = new ImmutableMap.Builder<String, Difficulty>()
            .put("peaceful", (Difficulty) (Object) EnumDifficulty.PEACEFUL)
            .put("easy", (Difficulty) (Object) EnumDifficulty.EASY)
            .put("normal", (Difficulty) (Object) EnumDifficulty.NORMAL)
            .put("hard", (Difficulty) (Object) EnumDifficulty.HARD)
            .build();
    public final Map<String, SpongeEntityType> entityIdToTypeMappings = Maps.newHashMap();
    private static final ImmutableMap<String, ObjectiveDisplayMode> objectiveDisplayModeMappings = new ImmutableMap.Builder<String, ObjectiveDisplayMode>()
            .put("integer", (ObjectiveDisplayMode) (Object) IScoreObjectiveCriteria.EnumRenderType.INTEGER)
            .put("hearts", (ObjectiveDisplayMode) (Object) IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            .build();

    public final Map<Class<? extends Entity>, SpongeEntityType> entityClassToTypeMappings = Maps.newHashMap();
    public final Map<String, Enchantment> enchantmentMappings = Maps.newHashMap();
    private final Map<String, Career> careerMappings = Maps.newHashMap();
    private final Map<String, Profession> professionMappings = Maps.newHashMap();
    private final Map<Integer, List<Career>> professionToCareerMappings = Maps.newHashMap();
    private final Map<String, DimensionType> dimensionTypeMappings = Maps.newHashMap();
    public final Map<Class<? extends Dimension>, DimensionType> dimensionClassMappings = Maps.newHashMap();
    private final Map<String, SpongeParticleType> particleMappings = Maps.newHashMap();
    private final Map<String, ParticleType> particleByName = Maps.newHashMap();
    private final List<PotionEffectType> potionList = new ArrayList<PotionEffectType>();
    private final List<BiomeType> biomeTypes = new ArrayList<BiomeType>();
    private final Map<String, SoundType> soundNames = Maps.newHashMap();
    private final Map<String, CoalType> coaltypeMappings = Maps.newHashMap();
    private final Map<String, GoldenApple> goldenAppleMappings = Maps.newHashMap();
    private final WorldGeneratorRegistry worldGeneratorRegistry = new WorldGeneratorRegistry();
    private final Hashtable<Class<? extends WorldProvider>, Integer> classToProviders = new Hashtable<Class<? extends WorldProvider>, Integer>();
    private final Map<UUID, WorldProperties> worldPropertiesUuidMappings = Maps.newHashMap();
    private final Map<String, WorldProperties> worldPropertiesNameMappings = Maps.newHashMap();
    private final Map<Integer, String> worldFolderDimensionIdMappings = Maps.newHashMap();
    public final Map<UUID, String> worldFolderUniqueIdMappings = Maps.newHashMap();
    public final Map<String, SpongeDisplaySlot> displaySlotMappings = Maps.newLinkedHashMap();
    public final Map<String, Criterion> criteriaMap = Maps.newHashMap();
    private final Map<String, SelectorType> selectorMappings = Maps.newHashMap();

    private final Map<String, NotePitch> notePitchMappings = Maps.newHashMap();
    public final Map<String, SkullType> skullTypeMappings = Maps.newLinkedHashMap();
    private final Map<String, TreeType> treeTypeMappings = Maps.newHashMap();
    private final Map<String, BannerPatternShape> bannerPatternShapeMappings = Maps.newHashMap();
    public final Map<String, BannerPatternShape> idToBannerPatternShapeMappings = Maps.newHashMap();
    private final Map<String, Fish> fishMappings = Maps.newHashMap();
    private final Map<String, CookedFish> cookedFishMappings = Maps.newHashMap();
    private final Map<String, DyeColor> dyeColorMappings = Maps.newHashMap();
    private final Map<String, Art> artMappings = Maps.newHashMap();
    private final Map<String, EntityType> entityTypeMappings = Maps.newHashMap();
    private final Map<String, ShrubType> shrubTypeMappings = new ImmutableMap.Builder<String, ShrubType>()
        .put("dead_bush", (ShrubType) (Object) BlockTallGrass.EnumType.DEAD_BUSH)
        .put("tall_grass", (ShrubType) (Object) BlockTallGrass.EnumType.GRASS)
        .put("fern", (ShrubType) (Object) BlockTallGrass.EnumType.FERN)
        .build();
    private final Map<String, DoublePlantType> doublePlantMappings = new ImmutableMap.Builder<String, DoublePlantType>()
        .put("sunflower", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.SUNFLOWER)
        .put("syringa", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.SYRINGA)
        .put("grass", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.GRASS)
        .put("fern", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.FERN)
        .put("rose", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.ROSE)
        .put("paeonia", (DoublePlantType) (Object) BlockDoublePlant.EnumPlantType.PAEONIA)
        .build();
    private final Map<String, PlantType> plantTypeMappings = new ImmutableMap.Builder<String, PlantType>()
        .put("dandelion", (PlantType) (Object) BlockFlower.EnumFlowerType.DANDELION)
        .put("poppy", (PlantType) (Object) BlockFlower.EnumFlowerType.POPPY)
        .put("blue_orchid", (PlantType) (Object) BlockFlower.EnumFlowerType.BLUE_ORCHID)
        .put("allium", (PlantType) (Object) BlockFlower.EnumFlowerType.ALLIUM)
        .put("houstonia", (PlantType) (Object) BlockFlower.EnumFlowerType.HOUSTONIA)
        .put("red_tulip", (PlantType) (Object) BlockFlower.EnumFlowerType.RED_TULIP)
        .put("orange_tulip", (PlantType) (Object) BlockFlower.EnumFlowerType.ORANGE_TULIP)
        .put("white_tulip", (PlantType) (Object) BlockFlower.EnumFlowerType.WHITE_TULIP)
        .put("pink_tulip", (PlantType) (Object) BlockFlower.EnumFlowerType.PINK_TULIP)
        .put("oxeye_daisy", (PlantType) (Object) BlockFlower.EnumFlowerType.OXEYE_DAISY)
        .build();

    private final Map<String, GeneratorType> generatorTypeMappings = Maps.newHashMap();

    public static final Map<String, BlockType> blockTypeMappings = Maps.newHashMap();
    public static final Map<String, ItemType> itemTypeMappings = Maps.newHashMap();

    private static final ImmutableMap<String, EntityInteractionType> entityInteractionTypeMappings =
            new ImmutableMap.Builder<String, EntityInteractionType>()
                    .put("attack", new SpongeEntityInteractionType("attack"))
                    .put("pick_block", new SpongeEntityInteractionType("pick_block"))
                    .put("use", new SpongeEntityInteractionType("use"))
                    .build();
    protected Map<Class<? extends CatalogType>, Map<String, ? extends CatalogType>> catalogTypeMap =
            ImmutableMap.<Class<? extends CatalogType>, Map<String, ? extends CatalogType>>builder()
                    .put(Achievement.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Art.class, this.artMappings)
                    .put(Attribute.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(BiomeType.class, this.biomeTypeMappings)
                    .put(BlockType.class, blockTypeMappings)
                    .put(Career.class, this.careerMappings)
                    .put(ChatType.class, chatTypeMappings)
                    .put(BannerPatternShape.class, this.bannerPatternShapeMappings)
                    .put(CookedFish.class, this.cookedFishMappings)
                    .put(DyeColor.class, this.dyeColorMappings)
                    .put(EntityInteractionType.class, entityInteractionTypeMappings)
                    .put(CoalType.class, this.coaltypeMappings)
                    .put(NotePitch.class, this.notePitchMappings)
                    .put(ComparatorType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Criterion.class, this.criteriaMap)
                    .put(Difficulty.class, difficultyMappings)
                    .put(DimensionType.class, this.dimensionTypeMappings)
                    .put(DirtType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(DisguisedBlockType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(DisplaySlot.class, this.displaySlotMappings)
                    .put(DoublePlantType.class, this.doublePlantMappings)
                    .put(Enchantment.class, this.enchantmentMappings)
                    .put(EquipmentType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(FireworkShape.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Fish.class, this.fishMappings)
                    .put(GameMode.class, gameModeMappings)
                    .put(GoldenApple.class, this.goldenAppleMappings)
                    .put(EntityType.class, this.entityTypeMappings)
                    .put(Hinge.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(HorseColor.class, SpongeEntityConstants.HORSE_COLORS)
                    .put(HorseStyle.class, SpongeEntityConstants.HORSE_STYLES)
                    .put(HorseVariant.class, SpongeEntityConstants.HORSE_VARIANTS)
                    .put(ItemType.class, itemTypeMappings)
                    .put(ObjectiveDisplayMode.class, objectiveDisplayModeMappings)
                    .put(OcelotType.class, SpongeEntityConstants.OCELOT_TYPES)
                    .put(Operation.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(ParticleType.class, this.particleByName)
                    .put(PlantType.class, this.plantTypeMappings)
                    .put(PotionEffectType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(PortionType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(PrismarineType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Profession.class, this.professionMappings)
                    .put(QuartzType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(RabbitType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(RailDirection.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Rotation.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SandstoneType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(ShrubType.class, this.shrubTypeMappings)
                    .put(SelectorType.class, this.selectorMappings)
                    .put(SkeletonType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SkullType.class, this.skullTypeMappings)
                    .put(SlabType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SoundType.class, this.soundNames)
                    .put(StairShape.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Statistic.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StatisticFormat.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StatisticGroup.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StoneType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(TextColor.class, textColorMappings)
                    .put(TextStyle.Base.class, textStyleMappings)
                    .put(TileEntityType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(TreeType.class, this.treeTypeMappings)
                    .put(Visibility.class, visibilityMappings)
                    .put(WallType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Weather.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(WorldGeneratorModifier.class, this.worldGeneratorRegistry.viewModifiersMap())
                    .put(GeneratorType.class, this.generatorTypeMappings)
                    .build();
    private final Map<Class<?>, Class<?>> builderMap = ImmutableMap.of(); // TODO FIGURE OUT HOW TO DO THIS!!?!

    public com.google.common.base.Optional<PotionEffectType> getPotion(String id) {
        return com.google.common.base.Optional.fromNullable((PotionEffectType) Potion.getPotionFromResourceLocation(id));
    }

    public com.google.common.base.Optional<EntityType> getEntity(String id) {
        if (!id.contains(":")) {
            id = "minecraft:" + id;
        }
        return com.google.common.base.Optional.fromNullable((EntityType) this.entityIdToTypeMappings.get(id));
    }

    public com.google.common.base.Optional<BiomeType> getBiome(String id) {
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && biome.biomeName.equalsIgnoreCase(id)) {
                return com.google.common.base.Optional.of((BiomeType) biome);
            }
        }
        return com.google.common.base.Optional.absent();
    }

    public List<BiomeType> getBiomes() {
        return ImmutableList.copyOf(this.biomeTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CatalogType> com.google.common.base.Optional<T> getType(Class<T> typeClass, String id) {
        Map<String, ? extends CatalogType> tempMap = this.catalogTypeMap.get(checkNotNull(typeClass, "null type class"));
        if (tempMap == null) {
            return com.google.common.base.Optional.absent();
        } else {
            T type = (T) tempMap.get(id.toLowerCase());
            if (type == null) {
                return com.google.common.base.Optional.absent();
            } else {
                return com.google.common.base.Optional.of(type);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CatalogType> Collection<T> getAllOf(Class<T> typeClass) {
        Map<String, ? extends CatalogType> tempMap = this.catalogTypeMap.get(checkNotNull(typeClass, "null type class"));
        if (tempMap == null) {
            return Collections.emptyList();
        } else {
            ImmutableList.Builder<T> builder = ImmutableList.builder();
            for (Map.Entry<String, ? extends CatalogType> entry : tempMap.entrySet()) {
                builder.add((T) entry.getValue());
            }
            return builder.build();
        }
    }

    @Override
    public <T> com.google.common.base.Optional<T> createBuilderOfType(Class<T> builderClass) {
        return null;
    }


    @Override
    public ItemStackBuilder createItemBuilder() {
        return new SpongeItemStackBuilder();
    }

    @Override
    public TradeOfferBuilder createTradeOfferBuilder() {
        return new SpongeTradeOfferBuilder();
    }

    @Override
    public FireworkEffectBuilder createFireworkEffectBuilder() {
        return new SpongeFireworkBuilder();
    }

    @Override
    public PotionEffectBuilder createPotionEffectBuilder() {
        return new SpongePotionBuilder();
    }

    @Override
    public ObjectiveBuilder createObjectiveBuilder() {
        return new SpongeObjectiveBuilder();
    }

    @Override
    public TeamBuilder createTeamBuilder() {
        return new SpongeTeamBuilder();
    }

    @Override
    public ScoreboardBuilder createScoreboardBuilder() {
        return new SpongeScoreboardBuilder();
    }

    @Override
    public StatisticBuilder createStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.EntityStatisticBuilder createEntityStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.BlockStatisticBuilder createBlockStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.ItemStatisticBuilder createItemStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.TeamStatisticBuilder createTeamStatisticBuilder() {
        return null;
    }

    @Override
    public AchievementBuilder createAchievementBuilder() {
        return null;
    }

    @Override
    public AttributeModifierBuilder createAttributeModifierBuilder() {
        return null;
    }

    @Override
    public AttributeBuilder createAttributeBuilder() {
        return null; // TODO
    }

    @Override
    public WorldBuilder createWorldBuilder() {
        return new SpongeWorldBuilder();
    }

    @Override
    public ExplosionBuilder createExplosionBuilder() {
        return new SpongeExplosionBuilder();
    }

    @Override
    public ValueBuilder createValueBuilder() {
        return new SpongeValueBuilder();
    }

    @Override
    public ParticleEffectBuilder createParticleEffectBuilder(ParticleType particle) {
        checkNotNull(particle);

        if (particle instanceof SpongeParticleType.Colorable) {
            return new SpongeParticleEffectBuilder.BuilderColorable((SpongeParticleType.Colorable) particle);
        } else if (particle instanceof SpongeParticleType.Resizable) {
            return new SpongeParticleEffectBuilder.BuilderResizable((SpongeParticleType.Resizable) particle);
        } else if (particle instanceof SpongeParticleType.Note) {
            return new SpongeParticleEffectBuilder.BuilderNote((SpongeParticleType.Note) particle);
        } else if (particle instanceof SpongeParticleType.Material) {
            return new SpongeParticleEffectBuilder.BuilderMaterial((SpongeParticleType.Material) particle);
        } else {
            return new SpongeParticleEffectBuilder((SpongeParticleType) particle);
        }
    }

    @Override
    public List<String> getDefaultGameRules() {

        List<String> gameruleList = new ArrayList<String>();
        for (Field f : DefaultGameRules.class.getFields()) {
            try {
                gameruleList.add((String) f.get(null));
            } catch (Exception e) {
                //Ignoring error
            }
        }
        return gameruleList;
    }

    @Override
    public List<Career> getCareers(Profession profession) {
        return this.professionToCareerMappings.get(((SpongeEntityMeta) profession).type);
    }


    public List<DimensionType> getDimensionTypes() {
        return ImmutableList.copyOf(this.dimensionTypeMappings.values());
    }

    public void registerDimensionType(DimensionType type) {
        this.dimensionTypeMappings.put(type.getName(), type);
        this.dimensionClassMappings.put(type.getDimensionClass(), type);
    }

    public void registerWorldProperties(WorldProperties properties) {
        this.worldPropertiesUuidMappings.put(properties.getUniqueId(), properties);
        this.worldPropertiesNameMappings.put(properties.getWorldName(), properties);
    }

    public void registerWorldDimensionId(int dim, String folderName) {
        this.worldFolderDimensionIdMappings.put(dim, folderName);
    }

    public void registerWorldUniqueId(UUID uuid, String folderName) {
        this.worldFolderUniqueIdMappings.put(uuid, folderName);
    }

    public com.google.common.base.Optional<WorldProperties> getWorldProperties(String worldName) {
        return com.google.common.base.Optional.fromNullable(this.worldPropertiesNameMappings.get(worldName));
    }

    public Collection<WorldProperties> getAllWorldProperties() {
        return Collections.unmodifiableCollection(this.worldPropertiesNameMappings.values());
    }

    public String getWorldFolder(int dim) {
        return this.worldFolderDimensionIdMappings.get(dim);
    }

    public String getWorldFolder(UUID uuid) {
        return this.worldFolderUniqueIdMappings.get(uuid);
    }

    public int getProviderType(Class<? extends WorldProvider> provider) {
        return this.classToProviders.get(provider);
    }

    public WorldSettings.GameType getGameType(GameMode mode) {
        // TODO: This is client-only
        //return WorldSettings.GameType.getByName(mode.getTranslation().getId());
        throw new UnsupportedOperationException();
    }

    public com.google.common.base.Optional<WorldProperties> getWorldProperties(UUID uuid) {
        return com.google.common.base.Optional.fromNullable(this.worldPropertiesUuidMappings.get(uuid));
    }

    @Override
    public void registerWorldGeneratorModifier(WorldGeneratorModifier modifier) {
        this.worldGeneratorRegistry.registerModifier(modifier);
    }

    public WorldGeneratorRegistry getWorldGeneratorRegistry() {
        return this.worldGeneratorRegistry;
    }

    @Override
    public com.google.common.base.Optional<Rotation> getRotationFromDegree(int degrees) {
        for (Rotation rotation : rotationMappings.values()) {
            if (rotation.getAngle() == degrees) {
                return com.google.common.base.Optional.of(rotation);
            }
        }
        return com.google.common.base.Optional.absent();
    }


    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return (GameProfile) new com.mojang.authlib.GameProfile(uuid, name);
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        return SpongeFavicon.load(raw);
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        return SpongeFavicon.load(file);
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        return SpongeFavicon.load(url);
    }

    @Override
    public Favicon loadFavicon(InputStream in) throws IOException {
        return SpongeFavicon.load(in);
    }

    @Override
    public Favicon loadFavicon(BufferedImage image) throws IOException {
        return SpongeFavicon.load(image);
    }


    @Override
    public RecipeRegistry getRecipeRegistry() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public DataManipulatorRegistry getManipulatorRegistry() {
        return SpongeDataRegistry.getInstance();
    }

    @Override
    public ImmutableDataRegistry getImmutableDataRegistry() {
        return SpongeImmutableRegistry.getInstance();
    }

    @Override
    public AttributeCalculator getAttributeCalculator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public com.google.common.base.Optional<Translation> getTranslationById(String id) {
        return com.google.common.base.Optional.<Translation>of(new SpongeTranslation(id));
    }

    private void setParticles() {
        this.addParticleType("explosion_normal", new SpongeParticleType(EnumParticleTypes.EXPLOSION_NORMAL, true));
        this.addParticleType("explosion_large", new SpongeParticleType.Resizable(EnumParticleTypes.EXPLOSION_LARGE, 1f));
        this.addParticleType("explosion_huge", new SpongeParticleType(EnumParticleTypes.EXPLOSION_HUGE, false));
        this.addParticleType("fireworks_spark", new SpongeParticleType(EnumParticleTypes.FIREWORKS_SPARK, true));
        this.addParticleType("water_bubble", new SpongeParticleType(EnumParticleTypes.WATER_BUBBLE, true));
        this.addParticleType("water_splash", new SpongeParticleType(EnumParticleTypes.WATER_SPLASH, true));
        this.addParticleType("water_wake", new SpongeParticleType(EnumParticleTypes.WATER_WAKE, true));
        this.addParticleType("suspended", new SpongeParticleType(EnumParticleTypes.SUSPENDED, false));
        this.addParticleType("suspended_depth", new SpongeParticleType(EnumParticleTypes.SUSPENDED_DEPTH, false));
        this.addParticleType("crit", new SpongeParticleType(EnumParticleTypes.CRIT, true));
        this.addParticleType("crit_magic", new SpongeParticleType(EnumParticleTypes.CRIT_MAGIC, true));
        this.addParticleType("smoke_normal", new SpongeParticleType(EnumParticleTypes.SMOKE_NORMAL, true));
        this.addParticleType("smoke_large", new SpongeParticleType(EnumParticleTypes.SMOKE_LARGE, true));
        this.addParticleType("spell", new SpongeParticleType(EnumParticleTypes.SPELL, false));
        this.addParticleType("spell_instant", new SpongeParticleType(EnumParticleTypes.SPELL_INSTANT, false));
        this.addParticleType("spell_mob", new SpongeParticleType.Colorable(EnumParticleTypes.SPELL_MOB, Color.BLACK));
        this.addParticleType("spell_mob_ambient", new SpongeParticleType.Colorable(EnumParticleTypes.SPELL_MOB_AMBIENT, Color.BLACK));
        this.addParticleType("spell_witch", new SpongeParticleType(EnumParticleTypes.SPELL_WITCH, false));
        this.addParticleType("drip_water", new SpongeParticleType(EnumParticleTypes.DRIP_WATER, false));
        this.addParticleType("drip_lava", new SpongeParticleType(EnumParticleTypes.DRIP_LAVA, false));
        this.addParticleType("villager_angry", new SpongeParticleType(EnumParticleTypes.VILLAGER_ANGRY, false));
        this.addParticleType("villager_happy", new SpongeParticleType(EnumParticleTypes.VILLAGER_HAPPY, true));
        this.addParticleType("town_aura", new SpongeParticleType(EnumParticleTypes.TOWN_AURA, true));
        this.addParticleType("note", new SpongeParticleType.Note(EnumParticleTypes.NOTE, 0f));
        this.addParticleType("portal", new SpongeParticleType(EnumParticleTypes.PORTAL, true));
        this.addParticleType("enchantment_table", new SpongeParticleType(EnumParticleTypes.ENCHANTMENT_TABLE, true));
        this.addParticleType("flame", new SpongeParticleType(EnumParticleTypes.FLAME, true));
        this.addParticleType("lava", new SpongeParticleType(EnumParticleTypes.LAVA, false));
        this.addParticleType("footstep", new SpongeParticleType(EnumParticleTypes.FOOTSTEP, false));
        this.addParticleType("cloud", new SpongeParticleType(EnumParticleTypes.CLOUD, true));
        this.addParticleType("redstone", new SpongeParticleType.Colorable(EnumParticleTypes.REDSTONE, Color.RED));
        this.addParticleType("snowball", new SpongeParticleType(EnumParticleTypes.SNOWBALL, false));
        this.addParticleType("snow_shovel", new SpongeParticleType(EnumParticleTypes.SNOW_SHOVEL, true));
        this.addParticleType("slime", new SpongeParticleType(EnumParticleTypes.SLIME, false));
        this.addParticleType("heart", new SpongeParticleType(EnumParticleTypes.HEART, false));
        this.addParticleType("barrier", new SpongeParticleType(EnumParticleTypes.BARRIER, false));
        this.addParticleType("item_crack",
                new SpongeParticleType.Material(EnumParticleTypes.ITEM_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("block_crack",
                new SpongeParticleType.Material(EnumParticleTypes.BLOCK_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("block_dust",
                new SpongeParticleType.Material(EnumParticleTypes.BLOCK_DUST, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("water_drop", new SpongeParticleType(EnumParticleTypes.WATER_DROP, false));
        // Is this particle available to be spawned? It's not registered on the client though
        this.addParticleType("item_take", new SpongeParticleType(EnumParticleTypes.ITEM_TAKE, false));
        this.addParticleType("mob_appearance", new SpongeParticleType(EnumParticleTypes.MOB_APPEARANCE, false));

        RegistryHelper.mapFields(ParticleTypes.class, this.particleMappings);
    }

    private void addParticleType(String mapping, SpongeParticleType particle) {
        this.particleMappings.put(mapping, particle);
        this.particleByName.put(particle.getName(), particle);
    }

    private void setEnchantments() {
        this.enchantmentMappings.put("protection", (Enchantment) net.minecraft.enchantment.Enchantment.protection);
        this.enchantmentMappings.put("fire_protection", (Enchantment) net.minecraft.enchantment.Enchantment.fireProtection);
        this.enchantmentMappings.put("feather_falling", (Enchantment) net.minecraft.enchantment.Enchantment.featherFalling);
        this.enchantmentMappings.put("blast_protection", (Enchantment) net.minecraft.enchantment.Enchantment.blastProtection);
        this.enchantmentMappings.put("projectile_protection", (Enchantment) net.minecraft.enchantment.Enchantment.projectileProtection);
        this.enchantmentMappings.put("respiration", (Enchantment) net.minecraft.enchantment.Enchantment.respiration);
        this.enchantmentMappings.put("aqua_affinity", (Enchantment) net.minecraft.enchantment.Enchantment.aquaAffinity);
        this.enchantmentMappings.put("thorns", (Enchantment) net.minecraft.enchantment.Enchantment.thorns);
        this.enchantmentMappings.put("depth_strider", (Enchantment) net.minecraft.enchantment.Enchantment.depthStrider);
        this.enchantmentMappings.put("sharpness", (Enchantment) net.minecraft.enchantment.Enchantment.sharpness);
        this.enchantmentMappings.put("smite", (Enchantment) net.minecraft.enchantment.Enchantment.smite);
        this.enchantmentMappings.put("bane_of_arthropods", (Enchantment) net.minecraft.enchantment.Enchantment.baneOfArthropods);
        this.enchantmentMappings.put("knockback", (Enchantment) net.minecraft.enchantment.Enchantment.knockback);
        this.enchantmentMappings.put("fire_aspect", (Enchantment) net.minecraft.enchantment.Enchantment.fireAspect);
        this.enchantmentMappings.put("looting", (Enchantment) net.minecraft.enchantment.Enchantment.looting);
        this.enchantmentMappings.put("efficiency", (Enchantment) net.minecraft.enchantment.Enchantment.efficiency);
        this.enchantmentMappings.put("silk_touch", (Enchantment) net.minecraft.enchantment.Enchantment.silkTouch);
        this.enchantmentMappings.put("unbreaking", (Enchantment) net.minecraft.enchantment.Enchantment.unbreaking);
        this.enchantmentMappings.put("fortune", (Enchantment) net.minecraft.enchantment.Enchantment.fortune);
        this.enchantmentMappings.put("power", (Enchantment) net.minecraft.enchantment.Enchantment.power);
        this.enchantmentMappings.put("punch", (Enchantment) net.minecraft.enchantment.Enchantment.punch);
        this.enchantmentMappings.put("flame", (Enchantment) net.minecraft.enchantment.Enchantment.flame);
        this.enchantmentMappings.put("infinity", (Enchantment) net.minecraft.enchantment.Enchantment.infinity);
        this.enchantmentMappings.put("luck_of_the_sea", (Enchantment) net.minecraft.enchantment.Enchantment.luckOfTheSea);
        this.enchantmentMappings.put("lure", (Enchantment) net.minecraft.enchantment.Enchantment.lure);

        RegistryHelper.mapFields(Enchantments.class, this.enchantmentMappings);
    }

    // Note: This is probably fairly slow, but only needs to be run rarely.
    private void setPotionTypes() {
        for (Potion potion : Potion.potionTypes) {
            if (potion != null) {
                PotionEffectType potionEffectType = (PotionEffectType) potion;
                this.potionList.add(potionEffectType);
            }
        }
        RegistryHelper.mapFields(PotionEffectTypes.class, new Function<String, PotionEffectType>() {

            @Override
            public PotionEffectType apply(String fieldName) {
                return getPotion(fieldName.toLowerCase()).get();
            }
        });
    }

    private void setBiomeTypes() {
        BiomeGenBase[] biomeArray = BiomeGenBase.getBiomeGenArray();
        for (BiomeGenBase biome : biomeArray) {
            if (biome != null) {
                this.biomeTypes.add((BiomeType) biome);
            }
        }

        this.biomeTypeMappings.put("ocean", (BiomeType) BiomeGenBase.ocean);
        this.biomeTypeMappings.put("plains", (BiomeType) BiomeGenBase.plains);
        this.biomeTypeMappings.put("desert", (BiomeType) BiomeGenBase.desert);
        this.biomeTypeMappings.put("extreme_hills", (BiomeType) BiomeGenBase.extremeHills);
        this.biomeTypeMappings.put("forest", (BiomeType) BiomeGenBase.forest);
        this.biomeTypeMappings.put("taiga", (BiomeType) BiomeGenBase.taiga);
        this.biomeTypeMappings.put("swampland", (BiomeType) BiomeGenBase.swampland);
        this.biomeTypeMappings.put("river", (BiomeType) BiomeGenBase.river);
        this.biomeTypeMappings.put("hell", (BiomeType) BiomeGenBase.hell);
        this.biomeTypeMappings.put("sky", (BiomeType) BiomeGenBase.sky);
        this.biomeTypeMappings.put("frozen_ocean", (BiomeType) BiomeGenBase.frozenOcean);
        this.biomeTypeMappings.put("frozen_river", (BiomeType) BiomeGenBase.frozenRiver);
        this.biomeTypeMappings.put("ice_plains", (BiomeType) BiomeGenBase.icePlains);
        this.biomeTypeMappings.put("ice_mountains", (BiomeType) BiomeGenBase.iceMountains);
        this.biomeTypeMappings.put("mushroom_island", (BiomeType) BiomeGenBase.mushroomIsland);
        this.biomeTypeMappings.put("mushroom_island_shore", (BiomeType) BiomeGenBase.mushroomIslandShore);
        this.biomeTypeMappings.put("beach", (BiomeType) BiomeGenBase.beach);
        this.biomeTypeMappings.put("desert_hills", (BiomeType) BiomeGenBase.desertHills);
        this.biomeTypeMappings.put("forest_hills", (BiomeType) BiomeGenBase.forestHills);
        this.biomeTypeMappings.put("taiga_hills", (BiomeType) BiomeGenBase.taigaHills);
        this.biomeTypeMappings.put("extreme_hills_edge", (BiomeType) BiomeGenBase.extremeHillsEdge);
        this.biomeTypeMappings.put("jungle", (BiomeType) BiomeGenBase.jungle);
        this.biomeTypeMappings.put("jungle_hills", (BiomeType) BiomeGenBase.jungleHills);
        this.biomeTypeMappings.put("jungle_edge", (BiomeType) BiomeGenBase.jungleEdge);
        this.biomeTypeMappings.put("deep_ocean", (BiomeType) BiomeGenBase.deepOcean);
        this.biomeTypeMappings.put("stone_beach", (BiomeType) BiomeGenBase.stoneBeach);
        this.biomeTypeMappings.put("cold_beach", (BiomeType) BiomeGenBase.coldBeach);
        this.biomeTypeMappings.put("birch_forest", (BiomeType) BiomeGenBase.birchForest);
        this.biomeTypeMappings.put("birch_forest_hills", (BiomeType) BiomeGenBase.birchForestHills);
        this.biomeTypeMappings.put("roofed_forest", (BiomeType) BiomeGenBase.roofedForest);
        this.biomeTypeMappings.put("cold_taiga", (BiomeType) BiomeGenBase.coldTaiga);
        this.biomeTypeMappings.put("cold_taiga_hills", (BiomeType) BiomeGenBase.coldTaigaHills);
        this.biomeTypeMappings.put("mega_taiga", (BiomeType) BiomeGenBase.megaTaiga);
        this.biomeTypeMappings.put("mega_taiga_hills", (BiomeType) BiomeGenBase.megaTaigaHills);
        this.biomeTypeMappings.put("extreme_hills_plus", (BiomeType) BiomeGenBase.extremeHillsPlus);
        this.biomeTypeMappings.put("savanna", (BiomeType) BiomeGenBase.savanna);
        this.biomeTypeMappings.put("savanna_plateau", (BiomeType) BiomeGenBase.savannaPlateau);
        this.biomeTypeMappings.put("mesa", (BiomeType) BiomeGenBase.mesa);
        this.biomeTypeMappings.put("mesa_plateau_forest", (BiomeType) BiomeGenBase.mesaPlateau_F);
        this.biomeTypeMappings.put("mesa_plateau", (BiomeType) BiomeGenBase.mesaPlateau);
        this.biomeTypeMappings.put("sunflower_plains", (BiomeType) biomeArray[BiomeGenBase.plains.biomeID + 128]);
        this.biomeTypeMappings.put("desert_mountains", (BiomeType) biomeArray[BiomeGenBase.desert.biomeID + 128]);
        this.biomeTypeMappings.put("flower_forest", (BiomeType) biomeArray[BiomeGenBase.forest.biomeID + 128]);
        this.biomeTypeMappings.put("taiga_mountains", (BiomeType) biomeArray[BiomeGenBase.taiga.biomeID + 128]);
        this.biomeTypeMappings.put("swampland_mountains", (BiomeType) biomeArray[BiomeGenBase.swampland.biomeID + 128]);
        this.biomeTypeMappings.put("ice_plains_spikes", (BiomeType) biomeArray[BiomeGenBase.icePlains.biomeID + 128]);
        this.biomeTypeMappings.put("jungle_mountains", (BiomeType) biomeArray[BiomeGenBase.jungle.biomeID + 128]);
        this.biomeTypeMappings.put("jungle_edge_mountains", (BiomeType) biomeArray[BiomeGenBase.jungleEdge.biomeID + 128]);
        this.biomeTypeMappings.put("cold_taiga_mountains", (BiomeType) biomeArray[BiomeGenBase.coldTaiga.biomeID + 128]);
        this.biomeTypeMappings.put("savanna_mountains", (BiomeType) biomeArray[BiomeGenBase.savanna.biomeID + 128]);
        this.biomeTypeMappings.put("savanna_plateau_mountains", (BiomeType) biomeArray[BiomeGenBase.savannaPlateau.biomeID + 128]);
        this.biomeTypeMappings.put("mesa_bryce", (BiomeType) biomeArray[BiomeGenBase.mesa.biomeID + 128]);
        this.biomeTypeMappings.put("mesa_plateau_forest_mountains", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau_F.biomeID + 128]);
        this.biomeTypeMappings.put("mesa_plateau_mountains", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau.biomeID + 128]);
        this.biomeTypeMappings.put("birch_forest_mountains", (BiomeType) biomeArray[BiomeGenBase.birchForest.biomeID + 128]);
        this.biomeTypeMappings.put("birch_forest_hills_mountains", (BiomeType) biomeArray[BiomeGenBase.birchForestHills.biomeID + 128]);
        this.biomeTypeMappings.put("roofed_forest_mountains", (BiomeType) biomeArray[BiomeGenBase.roofedForest.biomeID + 128]);
        this.biomeTypeMappings.put("mega_spruce_taiga", (BiomeType) biomeArray[BiomeGenBase.megaTaiga.biomeID + 128]);
        this.biomeTypeMappings.put("extreme_hills_mountains", (BiomeType) biomeArray[BiomeGenBase.extremeHills.biomeID + 128]);
        this.biomeTypeMappings.put("extreme_hills_plus_mountains", (BiomeType) biomeArray[BiomeGenBase.extremeHillsPlus.biomeID + 128]);
        this.biomeTypeMappings.put("mega_spruce_taiga_hills", (BiomeType) biomeArray[BiomeGenBase.megaTaigaHills.biomeID + 128]);

        RegistryHelper.mapFields(BiomeTypes.class, this.biomeTypeMappings);
    }

    private void setCareersAndProfessions() {
        try {
            Professions.class.getDeclaredField("FARMER").set(null, new SpongeProfession(0, "farmer"));
            Careers.class.getDeclaredField("FARMER").set(null, new SpongeCareer(0, "farmer", Professions.FARMER));
            Careers.class.getDeclaredField("FISHERMAN").set(null, new SpongeCareer(1, "fisherman", Professions.FARMER));
            Careers.class.getDeclaredField("SHEPHERD").set(null, new SpongeCareer(2, "shepherd", Professions.FARMER));
            Careers.class.getDeclaredField("FLETCHER").set(null, new SpongeCareer(3, "fletcher", Professions.FARMER));

            Professions.class.getDeclaredField("LIBRARIAN").set(null, new SpongeProfession(1, "librarian"));
            Careers.class.getDeclaredField("LIBRARIAN").set(null, new SpongeCareer(0, "librarian", Professions.LIBRARIAN));

            Professions.class.getDeclaredField("PRIEST").set(null, new SpongeProfession(2, "priest"));
            Careers.class.getDeclaredField("CLERIC").set(null, new SpongeCareer(0, "cleric", Professions.PRIEST));

            Professions.class.getDeclaredField("BLACKSMITH").set(null, new SpongeProfession(3, "blacksmith"));
            Careers.class.getDeclaredField("ARMORER").set(null, new SpongeCareer(0, "armor", Professions.BLACKSMITH));
            Careers.class.getDeclaredField("WEAPON_SMITH").set(null, new SpongeCareer(1, "weapon", Professions.BLACKSMITH));
            Careers.class.getDeclaredField("TOOL_SMITH").set(null, new SpongeCareer(2, "tool", Professions.BLACKSMITH));

            Professions.class.getDeclaredField("BUTCHER").set(null, new SpongeProfession(4, "butcher"));
            Careers.class.getDeclaredField("BUTCHER").set(null, new SpongeCareer(0, "butcher", Professions.BUTCHER));
            Careers.class.getDeclaredField("LEATHERWORKER").set(null, new SpongeCareer(1, "leatherworker", Professions.BUTCHER));

            this.professionMappings.put(Professions.FARMER.getName().toLowerCase(), Professions.FARMER);
            this.professionMappings.put(Professions.LIBRARIAN.getName().toLowerCase(), Professions.LIBRARIAN);
            this.professionMappings.put(Professions.PRIEST.getName().toLowerCase(), Professions.PRIEST);
            this.professionMappings.put(Professions.BLACKSMITH.getName().toLowerCase(), Professions.BLACKSMITH);
            this.professionMappings.put(Professions.BUTCHER.getName().toLowerCase(), Professions.BUTCHER);
            this.careerMappings.put(Careers.FARMER.getName().toLowerCase(), Careers.FARMER);
            this.careerMappings.put(Careers.FISHERMAN.getName().toLowerCase(), Careers.FISHERMAN);
            this.careerMappings.put(Careers.SHEPHERD.getName().toLowerCase(), Careers.SHEPHERD);
            this.careerMappings.put(Careers.FLETCHER.getName().toLowerCase(), Careers.FLETCHER);
            this.careerMappings.put(Careers.LIBRARIAN.getName().toLowerCase(), Careers.LIBRARIAN);
            this.careerMappings.put(Careers.CLERIC.getName().toLowerCase(), Careers.CLERIC);
            this.careerMappings.put(Careers.ARMORER.getName().toLowerCase(), Careers.ARMORER);
            this.careerMappings.put(Careers.WEAPON_SMITH.getName().toLowerCase(), Careers.WEAPON_SMITH);
            this.careerMappings.put(Careers.TOOL_SMITH.getName().toLowerCase(), Careers.TOOL_SMITH);
            this.careerMappings.put(Careers.BUTCHER.getName().toLowerCase(), Careers.BUTCHER);
            this.careerMappings.put(Careers.LEATHERWORKER.getName().toLowerCase(), Careers.LEATHERWORKER);
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.FARMER).type,
                    Arrays.asList(Careers.FARMER, Careers.FISHERMAN, Careers.SHEPHERD, Careers.FLETCHER));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.LIBRARIAN).type, Arrays.asList(Careers.LIBRARIAN));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.PRIEST).type, Arrays.asList(Careers.CLERIC));
            this.professionToCareerMappings
                    .put(((SpongeEntityMeta) Professions.BLACKSMITH).type, Arrays.asList(Careers.ARMORER, Careers.WEAPON_SMITH, Careers.TOOL_SMITH));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.BUTCHER).type, Arrays.asList(Careers.BUTCHER, Careers.LEATHERWORKER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTextColor(EnumChatFormatting handle, Color color) {
        SpongeTextColor spongeColor = new SpongeTextColor(handle, color);
        textColorMappings.put(handle.name().toLowerCase(), spongeColor);
        enumChatColor.put(handle, spongeColor);
    }

    private void setTextColors() {
        addTextColor(EnumChatFormatting.BLACK, Color.BLACK);
        addTextColor(EnumChatFormatting.DARK_BLUE, new Color(0x0000AA));
        addTextColor(EnumChatFormatting.DARK_GREEN, new Color(0x00AA00));
        addTextColor(EnumChatFormatting.DARK_AQUA, new Color(0x00AAAA));
        addTextColor(EnumChatFormatting.DARK_RED, new Color(0xAA0000));
        addTextColor(EnumChatFormatting.DARK_PURPLE, new Color(0xAA00AA));
        addTextColor(EnumChatFormatting.GOLD, new Color(0xFFAA00));
        addTextColor(EnumChatFormatting.GRAY, new Color(0xAAAAAA));
        addTextColor(EnumChatFormatting.DARK_GRAY, new Color(0x555555));
        addTextColor(EnumChatFormatting.BLUE, new Color(0x5555FF));
        addTextColor(EnumChatFormatting.GREEN, new Color(0x55FF55));
        addTextColor(EnumChatFormatting.AQUA, new Color(0x00FFFF));
        addTextColor(EnumChatFormatting.RED, new Color(0xFF5555));
        addTextColor(EnumChatFormatting.LIGHT_PURPLE, new Color(0xFF55FF));
        addTextColor(EnumChatFormatting.YELLOW, new Color(0xFFFF55));
        addTextColor(EnumChatFormatting.WHITE, Color.WHITE);
        addTextColor(EnumChatFormatting.RESET, Color.WHITE);

        RegistryHelper.mapFields(TextColors.class, textColorMappings);
        RegistryHelper.mapFields(ChatTypes.class, chatTypeMappings);
        RegistryHelper.mapFields(TextStyles.class, textStyleMappings);
    }

    private void setCoal() {
        // Because Minecraft doesn't have any enum stuff for this....
        this.coaltypeMappings.put("coal", new SpongeCoalType(0, "COAL"));
        this.coaltypeMappings.put("charcoal", new SpongeCoalType(1, "CHARCOAL"));
        RegistryHelper.mapFields(CoalTypes.class, this.coaltypeMappings);
    }

    private void setRotations() {
        RegistryHelper.mapFields(Rotations.class, rotationMappings);
    }

    private void setWeathers() {
        RegistryHelper.mapFields(Weathers.class, new Function<String, Weather>() {

            @Override
            public Weather apply(String fieldName) {
                return new SpongeWeather(fieldName);
            }
        });
    }

    private void setTextActionFactory() {
        //RegistryHelper.setFactory(TextActions.class, new SpongeTextActionFactory());
    }

    private void setTextFactory() {
        RegistryHelper.setFactory(Texts.class, new SpongeTextFactory());
        RegistryHelper.setFactory(MessageSinks.class, SpongeMessageSinkFactory.INSTANCE);
    }

    private void setSelectors() {
        this.selectorMappings.put("all_players", new SpongeSelectorType("a"));
        this.selectorMappings.put("all_entities", new SpongeSelectorType("e"));
        this.selectorMappings.put("nearest_player", new SpongeSelectorType("p"));
        this.selectorMappings.put("random", new SpongeSelectorType("r"));
        RegistryHelper.mapFields(SelectorTypes.class, this.selectorMappings);
        SpongeSelectorFactory factory = new SpongeSelectorFactory();
        Map<String, ArgumentHolder<?>> argMappings = Maps.newHashMap();
        // POSITION
        ArgumentType<Integer> x = factory.createArgumentType("x", Integer.class);
        ArgumentType<Integer> y = factory.createArgumentType("y", Integer.class);
        ArgumentType<Integer> z = factory.createArgumentType("z", Integer.class);
        ArgumentHolder.Vector3<Vector3i, Integer> position = new SpongeArgumentHolder.SpongeVector3<Vector3i, Integer>(x, y, z, Vector3i.class);
        argMappings.put("position", position);

        // RADIUS
        ArgumentType<Integer> rmin = factory.createArgumentType("rm", Integer.class);
        ArgumentType<Integer> rmax = factory.createArgumentType("r", Integer.class);
        ArgumentHolder.Limit<ArgumentType<Integer>> radius = new SpongeArgumentHolder.SpongeLimit<ArgumentType<Integer>>(rmin, rmax);
        argMappings.put("radius", radius);

        // GAME_MODE
        argMappings.put("game_mode", factory.createArgumentType("m", GameMode.class));

        // COUNT
        argMappings.put("count", factory.createArgumentType("c", Integer.class));

        // LEVEL
        ArgumentType<Integer> lmin = factory.createArgumentType("lm", Integer.class);
        ArgumentType<Integer> lmax = factory.createArgumentType("l", Integer.class);
        ArgumentHolder.Limit<ArgumentType<Integer>> level = new SpongeArgumentHolder.SpongeLimit<ArgumentType<Integer>>(lmin, lmax);
        argMappings.put("level", level);

        // TEAM
        argMappings.put("team", factory.createInvertibleArgumentType("team", Integer.class,
                org.spongepowered.api.scoreboard.Team.class.getName()));

        // NAME
        argMappings.put("name", factory.createInvertibleArgumentType("name", String.class));

        // DIMENSION
        ArgumentType<Integer> dx = factory.createArgumentType("dx", Integer.class);
        ArgumentType<Integer> dy = factory.createArgumentType("dy", Integer.class);
        ArgumentType<Integer> dz = factory.createArgumentType("dz", Integer.class);
        ArgumentHolder.Vector3<Vector3i, Integer> dimension =
                new SpongeArgumentHolder.SpongeVector3<Vector3i, Integer>(dx, dy, dz, Vector3i.class);
        argMappings.put("dimension", dimension);

        // ROTATION
        ArgumentType<Double> rotxmin = factory.createArgumentType("rxm", Double.class);
        ArgumentType<Double> rotymin = factory.createArgumentType("rym", Double.class);
        ArgumentType<Double> rotzmin = factory.createArgumentType("rzm", Double.class);
        ArgumentHolder.Vector3<Vector3d, Double> rotmin =
                new SpongeArgumentHolder.SpongeVector3<Vector3d, Double>(rotxmin, rotymin, rotzmin, Vector3d.class);
        ArgumentType<Double> rotxmax = factory.createArgumentType("rx", Double.class);
        ArgumentType<Double> rotymax = factory.createArgumentType("ry", Double.class);
        ArgumentType<Double> rotzmax = factory.createArgumentType("rz", Double.class);
        ArgumentHolder.Vector3<Vector3d, Double> rotmax =
                new SpongeArgumentHolder.SpongeVector3<Vector3d, Double>(rotxmax, rotymax, rotzmax, Vector3d.class);
        ArgumentHolder.Limit<ArgumentHolder.Vector3<Vector3d, Double>> rot =
                new SpongeArgumentHolder.SpongeLimit<ArgumentHolder.Vector3<Vector3d, Double>>(rotmin, rotmax);
        argMappings.put("rotation", rot);

        // ENTITY_TYPE
        argMappings.put("entity_type", factory.createInvertibleArgumentType("type", EntityType.class));

        RegistryHelper.mapFields(ArgumentTypes.class, argMappings);
        RegistryHelper.setFactory(Selectors.class, factory);
    }

    private void setTitleFactory() {
        //RegistryHelper.setFactory(Titles.class, new SpongeTitleFactory());
    }

    private void setDimensionTypes() {
        try {
            DimensionTypes.class.getDeclaredField("NETHER").set(null, new SpongeDimensionType("nether", true, WorldProviderHell.class, -1));
            DimensionTypes.class.getDeclaredField("OVERWORLD").set(null, new SpongeDimensionType("overworld", true, WorldProviderSurface.class, 0));
            DimensionTypes.class.getDeclaredField("END").set(null, new SpongeDimensionType("end", false, WorldProviderEnd.class, 1));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setGameModes() {
        RegistryHelper.mapFields(GameModes.class, gameModeMappings);
    }

    private void setSounds() {
        final Map<String, String> soundMappings = Maps.newHashMap();
        soundMappings.put("ambience_cave", "ambient.cave.cave");
        soundMappings.put("ambience_rain", "ambient.weather.rain");
        soundMappings.put("ambience_thunder", "ambient.weather.thunder");
        soundMappings.put("anvil_break", "random.anvil_break");
        soundMappings.put("anvil_land", "random.anvil_land");
        soundMappings.put("anvil_use", "random.anvil_use");
        soundMappings.put("arrow_hit", "random.bowhit");
        soundMappings.put("burp", "random.burp");
        soundMappings.put("chest_close", "random.chestclosed");
        soundMappings.put("chest_open", "random.chestopen");
        soundMappings.put("click", "random.click");
        soundMappings.put("door_close", "random.door_close");
        soundMappings.put("door_open", "random.door_open");
        soundMappings.put("drink", "random.drink");
        soundMappings.put("eat", "random.eat");
        soundMappings.put("explode", "random.explode");
        soundMappings.put("fall_big", "game.player.hurt.fall.big");
        soundMappings.put("fall_small", "game.player.hurt.fall.small");
        soundMappings.put("fire", "fire.fire");
        soundMappings.put("fire_ignite", "fire.ignite");
        soundMappings.put("firecharge_use", "item.fireCharge.use");
        soundMappings.put("fizz", "random.fizz");
        soundMappings.put("fuse", "game.tnt.primed");
        soundMappings.put("glass", "dig.glass");
        soundMappings.put("gui_button", "gui.button.press");
        soundMappings.put("hurt_flesh", "game.player.hurt");
        soundMappings.put("item_break", "random.break");
        soundMappings.put("item_pickup", "random.pop");
        soundMappings.put("lava", "liquid.lava");
        soundMappings.put("lava_pop", "liquid.lavapop");
        soundMappings.put("level_up", "random.levelup");
        soundMappings.put("minecart_base", "minecart.base");
        soundMappings.put("minecart_inside", "minecart.inside");
        soundMappings.put("music_game", "music.game");
        soundMappings.put("music_creative", "music.game.creative");
        soundMappings.put("music_end", "music.game.end");
        soundMappings.put("music_credits", "music.game.end.credits");
        soundMappings.put("music_dragon", "music.game.end.dragon");
        soundMappings.put("music_nether", "music.game.nether");
        soundMappings.put("music_menu", "music.menu");
        soundMappings.put("note_bass", "note.bass");
        soundMappings.put("note_piano", "note.harp");
        soundMappings.put("note_bass_drum", "note.bd");
        soundMappings.put("note_sticks", "note.hat");
        soundMappings.put("note_bass_guitar", "note.bassattack");
        soundMappings.put("note_snare_drum", "note.snare");
        soundMappings.put("note_pling", "note.pling");
        soundMappings.put("orb_pickup", "random.orb");
        soundMappings.put("piston_extend", "tile.piston.out");
        soundMappings.put("piston_retract", "tile.piston.in");
        soundMappings.put("portal", "portal.portal");
        soundMappings.put("portal_travel", "portal.travel");
        soundMappings.put("portal_trigger", "portal.trigger");
        soundMappings.put("potion_smash", "game.potion.smash");
        soundMappings.put("records_11", "records.11");
        soundMappings.put("records_13", "records.13");
        soundMappings.put("records_blocks", "records.blocks");
        soundMappings.put("records_cat", "records.cat");
        soundMappings.put("records_chirp", "records.chirp");
        soundMappings.put("records_far", "records.far");
        soundMappings.put("records_mall", "records.mall");
        soundMappings.put("records_mellohi", "records.mellohi");
        soundMappings.put("records_stal", "records.stal");
        soundMappings.put("records_strad", "records.strad");
        soundMappings.put("records_wait", "records.wait");
        soundMappings.put("records_ward", "records.ward");
        soundMappings.put("shoot_arrow", "random.bow");
        soundMappings.put("splash", "random.splash");
        soundMappings.put("splash2", "game.player.swim.splash");
        soundMappings.put("step_grass", "step.grass");
        soundMappings.put("step_gravel", "step.gravel");
        soundMappings.put("step_ladder", "step.ladder");
        soundMappings.put("step_sand", "step.sand");
        soundMappings.put("step_snow", "step.snow");
        soundMappings.put("step_stone", "step.stone");
        soundMappings.put("step_wood", "step.wood");
        soundMappings.put("step_wool", "step.cloth");
        soundMappings.put("swim", "game.player.swim");
        soundMappings.put("water", "liquid.water");
        soundMappings.put("wood_click", "random.wood_click");
        soundMappings.put("bat_death", "mob.bat.death");
        soundMappings.put("bat_hurt", "mob.bat.hurt");
        soundMappings.put("bat_idle", "mob.bat.idle");
        soundMappings.put("bat_loop", "mob.bat.loop");
        soundMappings.put("bat_takeoff", "mob.bat.takeoff");
        soundMappings.put("blaze_breath", "mob.blaze.breathe");
        soundMappings.put("blaze_death", "mob.blaze.death");
        soundMappings.put("blaze_hit", "mob.blaze.hit");
        soundMappings.put("cat_hiss", "mob.cat.hiss");
        soundMappings.put("cat_hit", "mob.cat.hitt");
        soundMappings.put("cat_meow", "mob.cat.meow");
        soundMappings.put("cat_purr", "mob.cat.purr");
        soundMappings.put("cat_purreow", "mob.cat.purreow");
        soundMappings.put("chicken_idle", "mob.chicken.say");
        soundMappings.put("chicken_hurt", "mob.chicken.hurt");
        soundMappings.put("chicken_egg_pop", "mob.chicken.plop");
        soundMappings.put("chicken_walk", "mob.chicken.step");
        soundMappings.put("cow_idle", "mob.cow.say");
        soundMappings.put("cow_hurt", "mob.cow.hurt");
        soundMappings.put("cow_walk", "mob.cow.step");
        soundMappings.put("creeper_hiss", "creeper.primed");
        soundMappings.put("creeper_hit", "mob.creeper.say");
        soundMappings.put("creeper_death", "mob.creeper.death");
        soundMappings.put("enderdragon_death", "mob.enderdragon.end");
        soundMappings.put("enderdragon_growl", "mob.enderdragon.growl");
        soundMappings.put("enderdragon_hit", "mob.enderdragon.hit");
        soundMappings.put("enderdragon_wings", "mob.enderdragon.wings");
        soundMappings.put("enderman_death", "mob.endermen.death");
        soundMappings.put("enderman_hit", "mob.endermen.hit");
        soundMappings.put("enderman_idle", "mob.endermen.idle");
        soundMappings.put("enderman_teleport", "mob.endermen.portal");
        soundMappings.put("enderman_scream", "mob.endermen.scream");
        soundMappings.put("enderman_stare", "mob.endermen.stare");
        soundMappings.put("ghast_scream", "mob.ghast.scream");
        soundMappings.put("ghast_scream2", "mob.ghast.affectionate_scream");
        soundMappings.put("ghast_charge", "mob.ghast.charge");
        soundMappings.put("ghast_death", "mob.ghast.death");
        soundMappings.put("ghast_fireball", "mob.ghast.fireball");
        soundMappings.put("ghast_moan", "mob.ghast.moan");
        soundMappings.put("guardian_idle", "mob.guardian.idle");
        soundMappings.put("guardian_attack", "mob.guardian.attack");
        soundMappings.put("guardian_curse", "mob.guardian.curse");
        soundMappings.put("guardian_flop", "mob.guardian.flop");
        soundMappings.put("guardian_elder_idle", "mob.guardian.elder.idle");
        soundMappings.put("guardian_land_idle", "mob.guardian.land.idle");
        soundMappings.put("guardian_hit", "mob.guardian.hit");
        soundMappings.put("guardian_elder_hit", "mob.guardian.elder.hit");
        soundMappings.put("guardian_land_hit", "mob.guardian.land.hit");
        soundMappings.put("guardian_death", "mob.guardian.death");
        soundMappings.put("guardian_elder_death", "mob.guardian.elder.death");
        soundMappings.put("guardian_land_death", "mob.guardian.land.death");
        soundMappings.put("hostile_death", "game.hostile.die");
        soundMappings.put("hostile_hurt", "game.hostile.hurt");
        soundMappings.put("hostile_fall_big", "game.hostile.hurt.fall.big");
        soundMappings.put("hostile_fall_small", "game.hostile.hurt.fall.small");
        soundMappings.put("hostile_swim", "game.hostile.swim");
        soundMappings.put("hostile_splash", "game.hostile.swim.splash");
        soundMappings.put("irongolem_death", "mob.irongolem.death");
        soundMappings.put("irongolem_hit", "mob.irongolem.hit");
        soundMappings.put("irongolem_throw", "mob.irongolem.throw");
        soundMappings.put("irongolem_walk", "mob.irongolem.walk");
        soundMappings.put("magmacube_walk", "mob.magmacube.big");
        soundMappings.put("magmacube_walk2", "mob.magmacube.small");
        soundMappings.put("magmacube_jump", "mob.magmacube.jump");
        soundMappings.put("neutral_death", "game.neutral.die");
        soundMappings.put("neutral_hurt", "game.neutral.hurt");
        soundMappings.put("neutral_fall_big", "game.neutral.hurt.fall.big");
        soundMappings.put("neutral_fall_small", "game.neutral.hurt.fall.small");
        soundMappings.put("neutral_swim", "game.neutral.swim");
        soundMappings.put("neutral_splash", "game.neutral.swim.splash");
        soundMappings.put("pig_idle", "mob.pig.say");
        soundMappings.put("pig_death", "mob.pig.death");
        soundMappings.put("pig_walk", "mob.pig.step");
        soundMappings.put("player_death", "game.player.die");
        soundMappings.put("rabbit_idle", "mob.rabbit.idle");
        soundMappings.put("rabbit_hurt", "mob.rabbit.hurt");
        soundMappings.put("rabbit_hop", "mob.rabbit.hop");
        soundMappings.put("rabbit_death", "mob.rabbit.death");
        soundMappings.put("sheep_idle", "mob.sheep.say");
        soundMappings.put("sheep_shear", "mob.sheep.shear");
        soundMappings.put("sheep_walk", "mob.sheep.step");
        soundMappings.put("silverfish_hit", "mob.silverfish.hit");
        soundMappings.put("silverfish_death", "mob.silverfish.kill");
        soundMappings.put("silverfish_idle", "mob.silverfish.say");
        soundMappings.put("silverfish_walk", "mob.silverfish.step");
        soundMappings.put("skeleton_idle", "mob.skeleton.say");
        soundMappings.put("skeleton_death", "mob.skeleton.death");
        soundMappings.put("skeleton_hurt", "mob.skeleton.hurt");
        soundMappings.put("skeleton_walk", "mob.skeleton.step");
        soundMappings.put("slime_attack", "mob.slime.attack");
        soundMappings.put("slime_walk", "mob.slime.big");
        soundMappings.put("slime_walk2", "mob.slime.small");
        soundMappings.put("spider_idle", "mob.spider.say");
        soundMappings.put("spider_death", "mob.spider.death");
        soundMappings.put("spider_walk", "mob.spider.step");
        soundMappings.put("wither_death", "mob.wither.death");
        soundMappings.put("wither_hurt", "mob.wither.hurt");
        soundMappings.put("wither_idle", "mob.wither.idle");
        soundMappings.put("wither_shoot", "mob.wither.shoot");
        soundMappings.put("wither_spawn", "mob.wither.spawn");
        soundMappings.put("wolf_bark", "mob.wolf.bark");
        soundMappings.put("wolf_death", "mob.wolf.death");
        soundMappings.put("wolf_growl", "mob.wolf.growl");
        soundMappings.put("wolf_howl", "mob.wolf.howl");
        soundMappings.put("wolf_hurt", "mob.wolf.hurt");
        soundMappings.put("wolf_pant", "mob.wolf.panting");
        soundMappings.put("wolf_shake", "mob.wolf.shake");
        soundMappings.put("wolf_walk", "mob.wolf.step");
        soundMappings.put("wolf_whine", "mob.wolf.whine");
        soundMappings.put("zombie_metal", "mob.zombie.metal");
        soundMappings.put("zombie_wood", "mob.zombie.wood");
        soundMappings.put("zombie_woodbreak", "mob.zombie.woodbreak");
        soundMappings.put("zombie_idle", "mob.zombie.say");
        soundMappings.put("zombie_death", "mob.zombie.death");
        soundMappings.put("zombie_hurt", "mob.zombie.hurt");
        soundMappings.put("zombie_infect", "mob.zombie.infect");
        soundMappings.put("zombie_unfect", "mob.zombie.unfect");
        soundMappings.put("zombie_remedy", "mob.zombie.remedy");
        soundMappings.put("zombie_walk", "mob.zombie.step");
        soundMappings.put("zombie_pig_idle", "mob.zombiepig.zpig");
        soundMappings.put("zombie_pig_angry", "mob.zombiepig.zpigangry");
        soundMappings.put("zombie_pig_death", "mob.zombiepig.zpigdeath");
        soundMappings.put("zombie_pig_hurt", "mob.zombiepig.zpighurt");
        soundMappings.put("dig_wool", "dig.cloth");
        soundMappings.put("dig_grass", "dig.grass");
        soundMappings.put("dig_gravel", "dig.gravel");
        soundMappings.put("dig_sand", "dig.sand");
        soundMappings.put("dig_snow", "dig.snow");
        soundMappings.put("dig_stone", "dig.stone");
        soundMappings.put("dig_wood", "dig.wood");
        soundMappings.put("firework_blast", "fireworks.blast");
        soundMappings.put("firework_blast2", "fireworks.blast_far");
        soundMappings.put("firework_large_blast", "fireworks.largeblast");
        soundMappings.put("firework_large_blast2", "fireworks.largeblast_far");
        soundMappings.put("firework_twinkle", "fireworks.twinkle");
        soundMappings.put("firework_twinkle2", "fireworks.twinkle_far");
        soundMappings.put("firework_launch", "fireworks.launch");
        soundMappings.put("successful_hit", "random.successful_hit");
        soundMappings.put("horse_angry", "mob.horse.angry");
        soundMappings.put("horse_armor", "mob.horse.armor");
        soundMappings.put("horse_breathe", "mob.horse.breathe");
        soundMappings.put("horse_death", "mob.horse.death");
        soundMappings.put("horse_gallop", "mob.horse.gallop");
        soundMappings.put("horse_hit", "mob.horse.hit");
        soundMappings.put("horse_idle", "mob.horse.idle");
        soundMappings.put("horse_jump", "mob.horse.jump");
        soundMappings.put("horse_land", "mob.horse.land");
        soundMappings.put("horse_saddle", "mob.horse.leather");
        soundMappings.put("horse_soft", "mob.horse.soft");
        soundMappings.put("horse_wood", "mob.horse.wood");
        soundMappings.put("donkey_angry", "mob.horse.donkey.angry");
        soundMappings.put("donkey_death", "mob.horse.donkey.death");
        soundMappings.put("donkey_hit", "mob.horse.donkey.hit");
        soundMappings.put("donkey_idle", "mob.horse.donkey.idle");
        soundMappings.put("horse_skeleton_death", "mob.horse.skeleton.death");
        soundMappings.put("horse_skeleton_hit", "mob.horse.skeleton.hit");
        soundMappings.put("horse_skeleton_idle", "mob.horse.skeleton.idle");
        soundMappings.put("horse_zombie_death", "mob.horse.zombie.death");
        soundMappings.put("horse_zombie_hit", "mob.horse.zombie.hit");
        soundMappings.put("horse_zombie_idle", "mob.horse.zombie.idle");
        soundMappings.put("villager_death", "mob.villager.death");
        soundMappings.put("villager_haggle", "mob.villager.haggle");
        soundMappings.put("villager_hit", "mob.villager.hit");
        soundMappings.put("villager_idle", "mob.villager.idle");
        soundMappings.put("villager_no", "mob.villager.no");
        soundMappings.put("villager_yes", "mob.villager.yes");

        RegistryHelper.mapFields(SoundTypes.class, new Function<String, SoundType>() {

            @Override
            public SoundType apply(String fieldName) {
                String soundName = soundMappings.get(fieldName.toLowerCase());
                SoundType sound = new SpongeSound(soundName);
                SpongeGameRegistry.this.soundNames.put(soundName, sound);
                return sound;
            }
        });
    }

    private void setDifficulties() {
        RegistryHelper.mapFields(Difficulties.class, difficultyMappings);
    }

    private void setDisplaySlots() {
        this.displaySlotMappings.put("list", new SpongeDisplaySlot("list", null, 0));
        this.displaySlotMappings.put("sidebar", new SpongeDisplaySlot("sidebar", null, 1));
        this.displaySlotMappings.put("below_name", new SpongeDisplaySlot("below_name", null, 2));

        RegistryHelper.mapFields(DisplaySlots.class, this.displaySlotMappings);

        for (Map.Entry<EnumChatFormatting, SpongeTextColor> entry: SpongeGameRegistry.enumChatColor.entrySet()) {
            this.displaySlotMappings.put(entry.getValue().getId(), new SpongeDisplaySlot(entry.getValue().getId(), entry.getValue(), entry.getKey().getColorIndex() + 3));
        }
    }

    private void addVisibility(String name, Team.EnumVisible handle) {
        SpongeVisibility visibility = new SpongeVisibility(handle);
        SpongeGameRegistry.visibilityMappings.put(name, visibility);
        SpongeGameRegistry.enumVisible.put(handle, visibility);
    }

    private void setCriteria() {
        this.criteriaMap.put("dummy", (Criterion) IScoreObjectiveCriteria.DUMMY);
        this.criteriaMap.put("trigger", (Criterion) IScoreObjectiveCriteria.TRIGGER);
        this.criteriaMap.put("health", (Criterion) IScoreObjectiveCriteria.health);
        this.criteriaMap.put("player_kills", (Criterion) IScoreObjectiveCriteria.playerKillCount);
        this.criteriaMap.put("total_kills", (Criterion) IScoreObjectiveCriteria.totalKillCount);
        this.criteriaMap.put("deaths", (Criterion) IScoreObjectiveCriteria.deathCount);

        RegistryHelper.mapFields(Criteria.class, this.criteriaMap);
    }

    private void setVisibilities() {
        this.addVisibility("all", Team.EnumVisible.ALWAYS);
        this.addVisibility("own_team", Team.EnumVisible.HIDE_FOR_OTHER_TEAMS);
        this.addVisibility("other_teams", Team.EnumVisible.HIDE_FOR_OWN_TEAM);
        this.addVisibility("none", Team.EnumVisible.NEVER);

        RegistryHelper.mapFields(Visibilities.class, SpongeGameRegistry.visibilityMappings);
    }

    private void setupSerialization() {
        KeyRegistry.registerKeys();
        Game game = Sponge.getGame();
        SerializationService service = game.getServiceManager().provide(SerializationService.class).get();
        SpongeDataRegistry dataRegistry = SpongeDataRegistry.getInstance();
        // TileEntities
        service.registerBuilder(Banner.class, new SpongeBannerBuilder(game));
        service.registerBuilder(PatternLayer.class, new SpongePatternLayerBuilder(game));
        service.registerBuilder(BrewingStand.class, new SpongeBrewingStandBuilder(game));
        service.registerBuilder(Chest.class, new SpongeChestBuilder(game));
        service.registerBuilder(CommandBlock.class, new SpongeCommandBlockBuilder(game));
        service.registerBuilder(org.spongepowered.api.block.tileentity.Comparator.class, new SpongeComparatorBuilder(game));
        service.registerBuilder(DaylightDetector.class, new SpongeDaylightBuilder(game));
        service.registerBuilder(Dispenser.class, new SpongeDispenserBuilder(game));
        service.registerBuilder(Dropper.class, new SpongeDropperBuilder(game));
        service.registerBuilder(EnchantmentTable.class, new SpongeEnchantmentTableBuilder(game));
        service.registerBuilder(EnderChest.class, new SpongeEnderChestBuilder(game));
        service.registerBuilder(EndPortal.class, new SpongeEndPortalBuilder(game));
        service.registerBuilder(Furnace.class, new SpongeFurnaceBuilder(game));
        service.registerBuilder(Hopper.class, new SpongeHopperBuilder(game));
        service.registerBuilder(MobSpawner.class, new SpongeMobSpawnerBuilder(game));
        service.registerBuilder(Note.class, new SpongeNoteBuilder(game));
        service.registerBuilder(Sign.class, new SpongeSignBuilder(game));
        service.registerBuilder(Skull.class, new SpongeSkullBuilder(game));

        final HealthDataProcessor healthProcessor = new HealthDataProcessor();
        service.registerBuilder(HealthData.class, healthProcessor);
        dataRegistry.registerDataProcessorAndImpl(HealthData.class, SpongeHealthData.class, ImmutableHealthData.class,
            ImmutableSpongeHealthData.class, healthProcessor);

        final DisplayNameDataProcessor displayNameDataProcessor = new DisplayNameDataProcessor();
        service.registerBuilder(DisplayNameData.class, displayNameDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(DisplayNameData.class, SpongeDisplayNameData.class,
                                                  ImmutableDisplayNameData.class, ImmutableSpongeDisplayNameData.class, displayNameDataProcessor);

        final CareerDataProcessor careerDataProcessor = new CareerDataProcessor();
        service.registerBuilder(CareerData.class, careerDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(CareerData.class, SpongeCareerData.class, ImmutableCareerData.class,
                                                  ImmutableSpongeCareerData.class, careerDataProcessor);

        final SignDataProcessor signDataProcessor = new SignDataProcessor();
        service.registerBuilder(SignData.class, signDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(SignData.class, SpongeSignData.class,
                                                  ImmutableSignData.class, ImmutableSpongeSignData.class, signDataProcessor);

        final SkullDataProcessor skullDataProcessor = new SkullDataProcessor();
        service.registerBuilder(SkullData.class, skullDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(SkullData.class, SpongeSkullData.class, ImmutableSkullData.class,
                                                  ImmutableSpongeSkullData.class, skullDataProcessor);

        final VelocityDataProcessor velocityDataProcessor = new VelocityDataProcessor();
        service.registerBuilder(VelocityData.class, velocityDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(VelocityData.class, SpongeVelocityData.class, ImmutableVelocityData.class,
                                                  ImmutableSpongeVelocityData.class, velocityDataProcessor);

        final EyeLocationDataProcessor eyeLocationDataProcessor = new EyeLocationDataProcessor();
        service.registerBuilder(EyeLocationData.class, eyeLocationDataProcessor);
        dataRegistry.registerDataProcessorAndImpl(EyeLocationData.class, SpongeEyeLocationData.class, ImmutableEyeLocationData.class,
            ImmutableSpongeEyeLocationData.class, eyeLocationDataProcessor);

        // Values
        dataRegistry.registerValueProcessor(Keys.HEALTH, new HealthValueProcessor());
        dataRegistry.registerValueProcessor(Keys.MAX_HEALTH, new MaxHealthValueProcessor());
        dataRegistry.registerValueProcessor(Keys.DISPLAY_NAME, new DisplayNameValueProcessor());
        dataRegistry.registerValueProcessor(Keys.SHOWS_DISPLAY_NAME, new DisplayNameVisibleValueProcessor());
        dataRegistry.registerValueProcessor(Keys.CAREER, new CareerValueProcessor());
        dataRegistry.registerValueProcessor(Keys.SIGN_LINES, new SignLinesValueProcessor());
        dataRegistry.registerValueProcessor(Keys.SKULL_TYPE, new SkullValueProcessor());
        dataRegistry.registerValueProcessor(Keys.VELOCITY, new VelocityValueProcessor());
        dataRegistry.registerValueProcessor(Keys.EYE_HEIGHT, new EyeHeightValueProcessor());
        dataRegistry.registerValueProcessor(Keys.EYE_LOCATION, new EyeLocationValueProcessor());

    }

    private void setNotePitches() {
        RegistryHelper.mapFields(NotePitches.class, new Function<String, NotePitch>() {

            @Override
            public NotePitch apply(String input) {
                NotePitch pitch = new SpongeNotePitch((byte) SpongeGameRegistry.this.notePitchMappings.size(), input);
                SpongeGameRegistry.this.notePitchMappings.put(input.toLowerCase(), pitch);
                return pitch;
            }

        });
    }

    private void setSkullTypes() {
        RegistryHelper.mapFields(SkullTypes.class, new Function<String, SkullType>() {

            @Override
            public SkullType apply(String input) {
                SkullType skullType = new SpongeSkullType((byte) SpongeGameRegistry.this.skullTypeMappings.size(), input);
                SpongeGameRegistry.this.skullTypeMappings.put(input.toLowerCase(), skullType);
                return skullType;
            }

        });
    }

    private void setTreeTypes() {
        RegistryHelper.mapFields(TreeTypes.class, new Function<String, TreeType>() {

            @Override
            public TreeType apply(String input) {
                TreeType treeType = new SpongeTreeType((byte) SpongeGameRegistry.this.treeTypeMappings.size(), input);
                SpongeGameRegistry.this.treeTypeMappings.put(input.toLowerCase(), treeType);
                return treeType;
            }

        });
    }

    private void setBannerPatternShapes() {
        RegistryHelper.mapFields(BannerPatternShapes.class, new Function<String, BannerPatternShape>() {

            @Override
            public BannerPatternShape apply(String input) {
                BannerPatternShape bannerPattern = (BannerPatternShape) (Object) TileEntityBanner.EnumBannerPattern.valueOf(input);
                SpongeGameRegistry.this.bannerPatternShapeMappings.put(bannerPattern.getName().toLowerCase(), bannerPattern);
                SpongeGameRegistry.this.idToBannerPatternShapeMappings.put(bannerPattern.getId().toLowerCase(), bannerPattern);
                return bannerPattern;
            }

        });
    }

    private void setFishes() {
        RegistryHelper.mapFields(Fishes.class, new Function<String, Fish>() {

            @Override
            public Fish apply(String input) {
                Fish fish = (Fish) (Object) ItemFishFood.FishType.valueOf(input);
                if (fish != null) {
                    SpongeGameRegistry.this.fishMappings.put(fish.getId().toLowerCase(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });

        RegistryHelper.mapFields(CookedFishes.class, new Function<String, CookedFish>() {

            @Override
            public CookedFish apply(String input) {
                CookedFish fish = new SpongeCookedFish(input, input); // TODO
                if (fish != null) {
                    SpongeGameRegistry.this.cookedFishMappings.put(fish.getId().toLowerCase(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });
    }

    private void setDyeColors() {
        RegistryHelper.mapFields(DyeColors.class, new Function<String, DyeColor>() {

            @Override
            public DyeColor apply(String input) {
                DyeColor dyeColor = (DyeColor) (Object) EnumDyeColor.valueOf(input);
                SpongeGameRegistry.this.dyeColorMappings.put(dyeColor.getName().toLowerCase(), dyeColor);
                return dyeColor;
            }

        });
    }

    private void setEntityInteractionTypes() {
        RegistryHelper.mapFields(EntityInteractionTypes.class, SpongeGameRegistry.entityInteractionTypeMappings);
    }

    private void setArts() {
        RegistryHelper.mapFields(Arts.class, new Function<String, Art>() {

            @Override
            public Art apply(String fieldName) {
                Art art = (Art) (Object) EntityPainting.EnumArt.valueOf(fieldName);
                SpongeGameRegistry.this.artMappings.put(art.getName().toLowerCase(), art);
                return art;
            }
        });
    }

    private void setObjectiveDisplayModes() {
        RegistryHelper.mapFields(ObjectiveDisplayModes.class, SpongeGameRegistry.objectiveDisplayModeMappings);
    }

    private void setEntityTypes() {
        // internal mapping of our EntityTypes to actual MC names
        this.entityTypeMappings.put("dropped_item", newEntityTypeFromName("Item"));
        this.entityTypeMappings.put("experience_orb", newEntityTypeFromName("XPOrb"));
        this.entityTypeMappings.put("leash_hitch", newEntityTypeFromName("LeashKnot"));
        this.entityTypeMappings.put("painting", newEntityTypeFromName("Painting"));
        this.entityTypeMappings.put("arrow", newEntityTypeFromName("Arrow"));
        this.entityTypeMappings.put("snowball", newEntityTypeFromName("Snowball"));
        this.entityTypeMappings.put("fireball", newEntityTypeFromName("LargeFireball", "Fireball"));
        this.entityTypeMappings.put("small_fireball", newEntityTypeFromName("SmallFireball"));
        this.entityTypeMappings.put("ender_pearl", newEntityTypeFromName("ThrownEnderpearl"));
        this.entityTypeMappings.put("eye_of_ender", newEntityTypeFromName("EyeOfEnderSignal"));
        this.entityTypeMappings.put("splash_potion", newEntityTypeFromName("ThrownPotion"));
        this.entityTypeMappings.put("thrown_exp_bottle", newEntityTypeFromName("ThrownExpBottle"));
        this.entityTypeMappings.put("item_frame", newEntityTypeFromName("ItemFrame"));
        this.entityTypeMappings.put("wither_skull", newEntityTypeFromName("WitherSkull"));
        this.entityTypeMappings.put("primed_tnt", newEntityTypeFromName("PrimedTnt"));
        this.entityTypeMappings.put("falling_block", newEntityTypeFromName("FallingSand"));
        this.entityTypeMappings.put("firework", newEntityTypeFromName("FireworksRocketEntity"));
        this.entityTypeMappings.put("armor_stand", newEntityTypeFromName("ArmorStand"));
        this.entityTypeMappings.put("boat", newEntityTypeFromName("Boat"));
        this.entityTypeMappings.put("rideable_minecart", newEntityTypeFromName("MinecartRideable"));
        this.entityTypeMappings.put("chested_minecart", newEntityTypeFromName("MinecartChest"));
        this.entityTypeMappings.put("furnace_minecart", newEntityTypeFromName("MinecartFurnace"));
        this.entityTypeMappings.put("tnt_minecart", newEntityTypeFromName("MinecartTnt", "MinecartTNT"));
        this.entityTypeMappings.put("hopper_minecart", newEntityTypeFromName("MinecartHopper"));
        this.entityTypeMappings.put("mob_spawner_minecart", newEntityTypeFromName("MinecartSpawner"));
        this.entityTypeMappings.put("commandblock_minecart", newEntityTypeFromName("MinecartCommandBlock"));
        this.entityTypeMappings.put("creeper", newEntityTypeFromName("Creeper"));
        this.entityTypeMappings.put("skeleton", newEntityTypeFromName("Skeleton"));
        this.entityTypeMappings.put("spider", newEntityTypeFromName("Spider"));
        this.entityTypeMappings.put("giant", newEntityTypeFromName("Giant"));
        this.entityTypeMappings.put("zombie", newEntityTypeFromName("Zombie"));
        this.entityTypeMappings.put("slime", newEntityTypeFromName("Slime"));
        this.entityTypeMappings.put("ghast", newEntityTypeFromName("Ghast"));
        this.entityTypeMappings.put("pig_zombie", newEntityTypeFromName("PigZombie"));
        this.entityTypeMappings.put("enderman", newEntityTypeFromName("Enderman"));
        this.entityTypeMappings.put("cave_spider", newEntityTypeFromName("CaveSpider"));
        this.entityTypeMappings.put("silverfish", newEntityTypeFromName("Silverfish"));
        this.entityTypeMappings.put("blaze", newEntityTypeFromName("Blaze"));
        this.entityTypeMappings.put("magma_cube", newEntityTypeFromName("LavaSlime"));
        this.entityTypeMappings.put("ender_dragon", newEntityTypeFromName("EnderDragon"));
        this.entityTypeMappings.put("wither", newEntityTypeFromName("WitherBoss"));
        this.entityTypeMappings.put("bat", newEntityTypeFromName("Bat"));
        this.entityTypeMappings.put("witch", newEntityTypeFromName("Witch"));
        this.entityTypeMappings.put("endermite", newEntityTypeFromName("Endermite"));
        this.entityTypeMappings.put("guardian", newEntityTypeFromName("Guardian"));
        this.entityTypeMappings.put("pig", newEntityTypeFromName("Pig"));
        this.entityTypeMappings.put("sheep", newEntityTypeFromName("Sheep"));
        this.entityTypeMappings.put("cow", newEntityTypeFromName("Cow"));
        this.entityTypeMappings.put("chicken", newEntityTypeFromName("Chicken"));
        this.entityTypeMappings.put("squid", newEntityTypeFromName("Squid"));
        this.entityTypeMappings.put("wolf", newEntityTypeFromName("Wolf"));
        this.entityTypeMappings.put("mushroom_cow", newEntityTypeFromName("MushroomCow"));
        this.entityTypeMappings.put("snowman", newEntityTypeFromName("SnowMan"));
        this.entityTypeMappings.put("ocelot", newEntityTypeFromName("Ozelot"));
        this.entityTypeMappings.put("iron_golem", newEntityTypeFromName("VillagerGolem"));
        this.entityTypeMappings.put("horse", newEntityTypeFromName("EntityHorse"));
        this.entityTypeMappings.put("rabbit", newEntityTypeFromName("Rabbit"));
        this.entityTypeMappings.put("villager", newEntityTypeFromName("Villager"));
        this.entityTypeMappings.put("ender_crystal", newEntityTypeFromName("EnderCrystal"));
        this.entityTypeMappings.put("egg", new SpongeEntityType(-1, "Egg", EntityEgg.class));
        this.entityTypeMappings.put("fishing_hook", new SpongeEntityType(-2, "FishingHook", EntityFishHook.class));
        this.entityTypeMappings.put("lightning", new SpongeEntityType(-3, "Lightning", EntityLightningBolt.class));
        this.entityTypeMappings.put("weather", new SpongeEntityType(-4, "Weather", EntityWeatherEffect.class));
        this.entityTypeMappings.put("player", new SpongeEntityType(-5, "Player", EntityPlayerMP.class));
        this.entityTypeMappings.put("complex_part", new SpongeEntityType(-6, "ComplexPart", EntityDragonPart.class));
        this.entityTypeMappings.put("human", registerCustomEntity(EntityHuman.class, "Human", -7));

        RegistryHelper.mapFields(EntityTypes.class, new Function<String, EntityType>() {

            @Override
            public EntityType apply(String fieldName) {
                if (fieldName.equals("UNKNOWN")) {
                    // TODO Something for Unknown?
                    return null;
                }
                EntityType entityType = SpongeGameRegistry.this.entityTypeMappings.get(fieldName.toLowerCase());
                SpongeGameRegistry.this.entityClassToTypeMappings
                        .put(((SpongeEntityType) entityType).entityClass, (SpongeEntityType) entityType);
                SpongeGameRegistry.this.entityIdToTypeMappings.put(((SpongeEntityType) entityType).getId(), ((SpongeEntityType) entityType));
                return entityType;
            }
        });

        RegistryHelper.mapFields(SkeletonTypes.class, SpongeEntityConstants.SKELETON_TYPES);
        RegistryHelper.mapFields(HorseColors.class, SpongeEntityConstants.HORSE_COLORS);
        RegistryHelper.mapFields(HorseVariants.class, SpongeEntityConstants.HORSE_VARIANTS);
        RegistryHelper.mapFields(HorseStyles.class, SpongeEntityConstants.HORSE_STYLES);
        RegistryHelper.mapFields(OcelotTypes.class, SpongeEntityConstants.OCELOT_TYPES);
        RegistryHelper.mapFields(RabbitTypes.class, SpongeEntityConstants.RABBIT_TYPES);
    }

    @SuppressWarnings("unchecked")
    private SpongeEntityType newEntityTypeFromName(String spongeName, String mcName) {
        return new SpongeEntityType((Integer) EntityList.stringToIDMapping.get(mcName), spongeName,
                (Class<? extends Entity>) EntityList.stringToClassMapping.get(mcName));
    }

    private SpongeEntityType newEntityTypeFromName(String name) {
        return newEntityTypeFromName(name, name);
    }

    @SuppressWarnings("unchecked")
    private SpongeEntityType registerCustomEntity(Class<? extends Entity> entityClass, String entityName, int entityId) {
        String entityFullName = String.format("%s.%s", Sponge.ECOSYSTEM_NAME, entityName);
        EntityList.classToStringMapping.put(entityClass, entityFullName);
        EntityList.stringToClassMapping.put(entityFullName, entityClass);
        return new SpongeEntityType(entityId, entityName, Sponge.ECOSYSTEM_NAME, entityClass);
    }

    public void setGeneratorTypes() {
        this.generatorTypeMappings.put("default", (GeneratorType) WorldType.DEFAULT);
        this.generatorTypeMappings.put("flat", (GeneratorType) WorldType.FLAT);
        this.generatorTypeMappings.put("debug", (GeneratorType) WorldType.DEBUG_WORLD);
        this.generatorTypeMappings.put("nether", (GeneratorType) new SpongeWorldTypeNether());
        this.generatorTypeMappings.put("end", (GeneratorType) new SpongeWorldTypeEnd());
        this.generatorTypeMappings.put("overworld", (GeneratorType) new SpongeWorldTypeOverworld());
        RegistryHelper.mapFields(GeneratorTypes.class, this.generatorTypeMappings);
    }

    private void setDoublePlantMappings() {
        RegistryHelper.mapFields(DoublePlantTypes.class, this.doublePlantMappings);
    }

    private void setFlowerMappings() {
        RegistryHelper.mapFields(PlantTypes.class, this.plantTypeMappings);
    }

    private void setShrubTypeMappings() {
        RegistryHelper.mapFields(ShrubTypes.class, this.shrubTypeMappings);
    }

    @Override
    public com.google.common.base.Optional<EntityStatistic> getEntityStatistic(StatisticGroup statisticGroup, EntityType entityType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<ItemStatistic> getItemStatistic(StatisticGroup statisticGroup, ItemType itemType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<BlockStatistic> getBlockStatistic(StatisticGroup statisticGroup, BlockType blockType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<TeamStatistic> getTeamStatistic(StatisticGroup statisticGroup, TextColor teamColor) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Collection<Statistic> getStatistics(StatisticGroup statisticGroup) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public void registerStatistic(Statistic stat) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<ResourcePack> getById(String id) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<DisplaySlot> getDisplaySlotForColor(TextColor color) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public PopulatorFactory getPopulatorFactory() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public <E extends Extent> Transform<E> createTransform() {
        return new SpongeTransform<E>();
    }

    @Override
    public <E extends Extent> Transform<E> createTransform(E extent) {
        return new SpongeTransform<E>(extent, Vector3d.ZERO);
    }

    @Override
    public ExtentBufferFactory getExtentBufferFactory() {
        return SpongeExtentBufferFactory.INSTANCE;
    }

    public void preInit() {
        setupSerialization();
    }

    public void init() {
        setDimensionTypes();
        setEnchantments();
        setCareersAndProfessions();
        setTextColors();
        setRotations();
        setWeathers();
        setTextActionFactory();
        setTextFactory();
        setSelectors();
        setTitleFactory();
        setParticles();
        setGameModes();
        setSounds();
        setDifficulties();
        setArts();
        setDyeColors();
        setSkullTypes();
        setTreeTypes();
        setNotePitches();
        setBannerPatternShapes();
        setEntityInteractionTypes();
        setFlowerMappings();
        setDoublePlantMappings();
        setShrubTypeMappings();
        setDisplaySlots();
        setVisibilities();
        setCriteria();
        setObjectiveDisplayModes();
        setGeneratorTypes();
    }

    public void postInit() {
        setPotionTypes();
        setBiomeTypes();
        setCoal();
        setFishes();
        setEntityTypes();
    }
}
