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
package org.spongepowered.common.data.processor.value;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.ValueProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class DisplayNameVisibleValueProcessor extends AbstractSpongeProcessor<Boolean, Value<Boolean>> {

    public DisplayNameVisibleValueProcessor() {
        super(Keys.SHOWS_DISPLAY_NAME);
    }

    @Override
    public Optional<Boolean> getValueFromContainer(ValueContainer<?> container) {
        if (container instanceof Entity) {
            return Optional.of(((Entity) container).getAlwaysRenderNameTag());
        } else if (container instanceof ItemStack || container instanceof IWorldNameable) {
            return Optional.of(true);
        }
        return Optional.absent();
    }

    @Override
    public Optional<Value<Boolean>> getApiValueFromContainer(ValueContainer<?> container) {
        final Optional<Boolean> optional = getValueFromContainer(container);
        if (optional.isPresent()) {
            return Optional.<Value<Boolean>>of(new SpongeValue<Boolean>(Keys.SHOWS_DISPLAY_NAME, true, optional.get()));
        }
        return Optional.absent();
    }

    @Override
    public boolean supports(ValueContainer<?> container) {
        return container instanceof Entity || container instanceof ItemStack || container instanceof IWorldNameable;
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<Boolean, Boolean> function) {
        final Optional<Boolean> optional = getValueFromContainer(container);
        if (optional.isPresent()) {
            final boolean newDisplays = checkNotNull(function.apply(optional.get()));
            return offerToStore(container, newDisplays);
        }
        return DataTransactionBuilder.failNoData();
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<?> value) {
        return offerToStore(container, ((Boolean) value.get()));
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, Boolean value) {
        if (container instanceof Entity) {
            final boolean old = ((Entity) container).getAlwaysRenderNameTag();
            try {
                ((Entity) container).setAlwaysRenderNameTag(value);
                return DataTransactionBuilder.builder()
                        .replace(ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, Keys.SHOWS_DISPLAY_NAME,
                                true, old))
                        .success(ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, Keys.SHOWS_DISPLAY_NAME,
                                true, value))
                        .result(DataTransactionResult.Type.SUCCESS)
                        .build();
            } catch (Exception e) {
                return DataTransactionBuilder.errorResult(ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class,
                        Keys.SHOWS_DISPLAY_NAME, true, value));
            }

        }
        return DataTransactionBuilder.failResult(ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class,
                Keys.SHOWS_DISPLAY_NAME, true, value));
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionBuilder.failNoData();
    }
}
