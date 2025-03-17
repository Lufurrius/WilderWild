/*
 * Copyright 2025 FrozenBlock
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

package net.frozenblock.wilderwild.mixin.item.tooltip;

import java.util.function.Consumer;
import net.frozenblock.wilderwild.registry.WWDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	@Final
	public PatchedDataComponentMap components;

	@Inject(
		method = "addDetailsToTooltip",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
			ordinal = 0,
			shift = At.Shift.AFTER
		)
	)
	public void wilderWild$addDetailsToTooltip(
		Item.TooltipContext tooltipContext,
		TooltipDisplay tooltipDisplay,
		@Nullable Player player,
		TooltipFlag tooltipFlag,
		Consumer<Component> consumer,
		CallbackInfo info
	) {
		this.wilderWild$addToTooltipFromHolder(WWDataComponents.FIREFLY_COLOR, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
		this.wilderWild$addToTooltipFromHolder(WWDataComponents.BUTTERFLY_VARIANT, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
	}

	@Unique
	public <T extends TooltipProvider> void wilderWild$addToTooltipFromHolder(
		DataComponentType<Holder<T>> dataComponentType,
		Item.TooltipContext tooltipContext,
		TooltipDisplay tooltipDisplay,
		Consumer<Component> consumer,
		TooltipFlag tooltipFlag
	) {
		Holder<T> possibleVariantHolder = ItemStack.class.cast(this).get(dataComponentType);

		if (possibleVariantHolder != null && possibleVariantHolder.value() instanceof TooltipProvider tooltipProvider) {
			if (tooltipDisplay.shows(dataComponentType)) {
				tooltipProvider.addToTooltip(tooltipContext, consumer, tooltipFlag, this.components);
			}
		}
	}

}
