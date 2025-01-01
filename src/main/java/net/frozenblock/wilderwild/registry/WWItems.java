/*
 * Copyright 2023-2024 FrozenBlock
 * This file is part of Wilder Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.wilderwild.registry;

import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.frozenblock.lib.item.api.DamageOnUseBlockItem;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.entity.variant.firefly.FireflyColor;
import net.frozenblock.wilderwild.entity.variant.firefly.FireflyColors;
import net.frozenblock.wilderwild.item.CoconutItem;
import net.frozenblock.wilderwild.item.CopperHorn;
import net.frozenblock.wilderwild.item.MilkweedPod;
import net.frozenblock.wilderwild.item.MobBottleItem;
import net.frozenblock.wilderwild.tag.WWInstrumentTags;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import static net.minecraft.world.item.Items.registerBlock;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public final class WWItems {

	// BOATS
	public static final BoatItem BAOBAB_BOAT = register("baobab_boat",
		properties -> new BoatItem(WWEntityTypes.BAOBAB_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem BAOBAB_CHEST_BOAT = register("baobab_chest_boat",
		properties -> new BoatItem(WWEntityTypes.BAOBAB_CHEST_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem CYPRESS_BOAT = register("cypress_boat",
		properties -> new BoatItem(WWEntityTypes.CYPRESS_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem CYPRESS_CHEST_BOAT = register("cypress_chest_boat",
		properties -> new BoatItem(WWEntityTypes.CYPRESS_CHEST_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem PALM_BOAT = register("palm_boat",
		properties -> new BoatItem(WWEntityTypes.PALM_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem PALM_CHEST_BOAT = register("palm_chest_boat",
		properties -> new BoatItem(WWEntityTypes.PALM_CHEST_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem MAPLE_BOAT = register("maple_boat",
		properties -> new BoatItem(WWEntityTypes.MAPLE_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);
	public static final BoatItem MAPLE_CHEST_BOAT = register("maple_chest_boat",
		properties -> new BoatItem(WWEntityTypes.MAPLE_CHEST_BOAT, properties),
		new Item.Properties()
			.stacksTo(1)
	);

	// BLOCK ITEMS
	public static final Item BAOBAB_NUT = registerBlock(WWBlocks.BAOBAB_NUT,
		new Item.Properties()
			.food(WWFood.BAOBAB_NUT)
	);
	public static final Item BAOBAB_SIGN = registerBlock(WWBlocks.BAOBAB_SIGN,
		(block, properties) -> new SignItem(block, WWBlocks.BAOBAB_WALL_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item BAOBAB_HANGING_SIGN = registerBlock(WWBlocks.BAOBAB_HANGING_SIGN,
		(block, properties) -> new HangingSignItem(block, WWBlocks.BAOBAB_WALL_HANGING_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item CYPRESS_SIGN = registerBlock(WWBlocks.CYPRESS_SIGN,
		(block, properties) -> new SignItem(block, WWBlocks.CYPRESS_WALL_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item CYPRESS_HANGING_SIGN = registerBlock(WWBlocks.CYPRESS_HANGING_SIGN,
		(block, properties) -> new HangingSignItem(block, WWBlocks.CYPRESS_WALL_HANGING_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item PALM_SIGN = registerBlock(WWBlocks.PALM_SIGN,
		(block, properties) -> new SignItem(block, WWBlocks.PALM_WALL_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item PALM_HANGING_SIGN = registerBlock(WWBlocks.PALM_HANGING_SIGN,
		(block, properties) -> new HangingSignItem(block, WWBlocks.PALM_WALL_HANGING_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item MAPLE_SIGN = registerBlock(WWBlocks.MAPLE_SIGN,
		(block, properties) -> new SignItem(block, WWBlocks.MAPLE_WALL_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item MAPLE_HANGING_SIGN = registerBlock(WWBlocks.MAPLE_HANGING_SIGN,
		(block, properties) -> new HangingSignItem(block, WWBlocks.MAPLE_WALL_HANGING_SIGN, properties),
		new Item.Properties().stacksTo(16)
	);
	public static final Item COCONUT = registerBlock(WWBlocks.COCONUT, CoconutItem::new, new Item.Properties());
	public static final Item POLLEN = registerBlock(WWBlocks.POLLEN, BlockItem::new, new Item.Properties());
	public static final Item ALGAE = registerBlock(WWBlocks.ALGAE, PlaceOnWaterBlockItem::new, new Item.Properties());
	public static final Item FLOWERING_LILY_PAD = registerBlock(WWBlocks.FLOWERING_LILY_PAD, PlaceOnWaterBlockItem::new, new Item.Properties());
	public static final Item ECHO_GLASS = registerBlock(WWBlocks.ECHO_GLASS, BlockItem::new, new Item.Properties());
	public static final Item SCORCHED_SAND = registerBlock(WWBlocks.SCORCHED_SAND, BlockItem::new, new Item.Properties());
	public static final Item SCORCHED_RED_SAND = registerBlock(WWBlocks.SCORCHED_RED_SAND, BlockItem::new, new Item.Properties());
	public static final Item DISPLAY_LANTERN = registerBlock(WWBlocks.DISPLAY_LANTERN, BlockItem::new, new Item.Properties().component(WWDataComponents.FIREFLIES, ImmutableList.of()));

	// ITEMS
	public static final MilkweedPod MILKWEED_POD = register("milkweed_pod", MilkweedPod::new, new Item.Properties());
	public static final Item SPLIT_COCONUT = register("split_coconut", Item::new, new Item.Properties().food(WWFood.SPLIT_COCONUT));
	public static final MobBottleItem FIREFLY_BOTTLE = register("firefly_bottle",
		properties -> new MobBottleItem(
			WWEntityTypes.FIREFLY,
			WWSounds.ITEM_BOTTLE_RELEASE_FIREFLY,
			properties
		),
		new Item.Properties()
			.stacksTo(16)
			.component(
				WWDataComponents.BOTTLE_ENTITY_DATA,
				CustomData.EMPTY.update(compoundTag -> compoundTag.putString("FireflyBottleVariantTag", FireflyColors.DEFAULT.location().toString()))
			)
	);
	public static final MobBottleItem BUTTERFLY_BOTTLE = register("butterfly_bottle",
		properties -> new MobBottleItem(
			WWEntityTypes.BUTTERFLY,
			WWSounds.ITEM_BOTTLE_RELEASE_BUTTERFLY,
			properties
		),
		new Item.Properties().stacksTo(1).component(WWDataComponents.BOTTLE_ENTITY_DATA, CustomData.EMPTY)
	);

	// FOOD
	public static final Item PRICKLY_PEAR = registerBlock(WWBlocks.PRICKLY_PEAR_CACTUS, ((block, properties) -> new DamageOnUseBlockItem(block, properties, 2F, WWSounds.PLAYER_HURT_CACTUS, WWDamageTypes.PRICKLY_PEAR)), new Item.Properties().food(WWFood.PRICKLY_PEAR));
	public static final Item PEELED_PRICKLY_PEAR = register("peeled_prickly_pear", Item::new, new Item.Properties().food(Foods.APPLE));
	public static final Item CRAB_CLAW = register("crab_claw", Item::new, new Item.Properties().food(WWFood.CRAB_CLAW));
	public static final Item COOKED_CRAB_CLAW = register("cooked_crab_claw", Item::new, new Item.Properties().food(WWFood.COOKED_CRAB_CLAW));
	public static final Item SCORCHED_EYE = register("scorched_eye", Item::new, new Item.Properties().food(WWFood.SCORCHED_EYE, WWFood.SCORCHED_EYE_CONSUMABLE));
	public static final Item FERMENTED_SCORCHED_EYE = register("fermented_scorched_eye", Item::new, new Item.Properties());

	// SPAWN EGGS & BUCKETS
	public static final SpawnEggItem FIREFLY_SPAWN_EGG = register("firefly_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.FIREFLY, Integer.parseInt("2A2E2B", 16), Integer.parseInt("AAF644", 16), properties), new Item.Properties());
	public static final SpawnEggItem JELLYFISH_SPAWN_EGG = register("jellyfish_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.JELLYFISH, Integer.parseInt("E484E4", 16), Integer.parseInt("DF71DC", 16), properties), new Item.Properties());
	public static final MobBucketItem JELLYFISH_BUCKET = register("jellyfish_bucket", properties -> new MobBucketItem(WWEntityTypes.JELLYFISH, Fluids.WATER, WWSounds.ITEM_BUCKET_EMPTY_JELLYFISH, properties), new Item.Properties().stacksTo(1));
	public static final SpawnEggItem CRAB_SPAWN_EGG = register("crab_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.CRAB, Integer.parseInt("F98334", 16), Integer.parseInt("F9C366", 16), properties), new Item.Properties());
	public static final MobBucketItem CRAB_BUCKET = register("crab_bucket", properties -> new MobBucketItem(WWEntityTypes.CRAB, Fluids.WATER, WWSounds.ITEM_BUCKET_EMPTY_CRAB, properties), new Item.Properties().stacksTo(1));
	public static final SpawnEggItem OSTRICH_SPAWN_EGG = register("ostrich_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.OSTRICH, Integer.parseInt("FAE0D0", 16), Integer.parseInt("5B4024", 16), properties), new Item.Properties());
	public static final SpawnEggItem SCORCHED_SPAWN_EGG = register("scorched_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.SCORCHED, Integer.parseInt("4C2516", 16), Integer.parseInt("FFB800", 16), properties), new Item.Properties());
	public static final SpawnEggItem BUTTERFLY_SPAWN_EGG = register("butterfly_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.BUTTERFLY, Integer.parseInt("542003", 16), Integer.parseInt("FFCF60", 16), properties), new Item.Properties());
	public static final SpawnEggItem MOOBLOOM_SPAWN_EGG = register("moobloom_spawn_egg", properties -> new SpawnEggItem(WWEntityTypes.MOOBLOOM, Integer.parseInt("FED639", 16), Integer.parseInt("F7EDC1", 16), properties), new Item.Properties());

	// INSTRUMENT
	public static final CopperHorn COPPER_HORN = register("copper_horn",
		properties -> new CopperHorn(WWInstrumentTags.COPPER_HORNS, properties),
		new Item.Properties().stacksTo(1)
	);

	public static final ResourceKey<Instrument> SAX_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("sax_copper_horn"));
	public static final ResourceKey<Instrument> TUBA_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("tuba_copper_horn"));
	public static final ResourceKey<Instrument> RECORDER_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("recorder_copper_horn"));
	public static final ResourceKey<Instrument> FLUTE_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("flute_copper_horn"));
	public static final ResourceKey<Instrument> OBOE_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("oboe_copper_horn"));
	public static final ResourceKey<Instrument> CLARINET_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("clarinet_copper_horn"));
	public static final ResourceKey<Instrument> TRUMPET_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("trumpet_copper_horn"));
	public static final ResourceKey<Instrument> TROMBONE_COPPER_HORN = ResourceKey.create(Registries.INSTRUMENT, WWConstants.id("trombone_copper_horn"));

	private WWItems() {
		throw new UnsupportedOperationException("WWItems contains only static declarations.");
	}

	public static void registerItems() {
		WWConstants.logWithModId("Registering Items for", WWConstants.UNSTABLE_LOGGING);
		CompostingChanceRegistry.INSTANCE.add(BAOBAB_NUT, 0.3F);
		CompostingChanceRegistry.INSTANCE.add(MILKWEED_POD, 0.25F);
		CompostingChanceRegistry.INSTANCE.add(SPLIT_COCONUT, 0.15F);
		CompostingChanceRegistry.INSTANCE.add(COCONUT, 0.3F);
		registerDispenses();
	}

	private static void registerDispenses() {
		DispenserBlock.registerBehavior(BAOBAB_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.BAOBAB_BOAT));
		DispenserBlock.registerBehavior(BAOBAB_CHEST_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.BAOBAB_CHEST_BOAT));
		DispenserBlock.registerBehavior(CYPRESS_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.CYPRESS_BOAT));
		DispenserBlock.registerBehavior(CYPRESS_CHEST_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.CYPRESS_CHEST_BOAT));
		DispenserBlock.registerBehavior(PALM_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.PALM_BOAT));
		DispenserBlock.registerBehavior(PALM_CHEST_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.PALM_CHEST_BOAT));
		DispenserBlock.registerBehavior(MAPLE_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.MAPLE_BOAT));
		DispenserBlock.registerBehavior(MAPLE_CHEST_BOAT, new BoatDispenseItemBehavior(WWEntityTypes.MAPLE_CHEST_BOAT));
	}

	private static @NotNull <T extends Item> T register(String name, @NotNull Function<Item.Properties, Item> function, Item.@NotNull Properties properties) {
		return (T) Items.registerItem(ResourceKey.create(Registries.ITEM, WWConstants.id(name)), function, properties);
	}
}
