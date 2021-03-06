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
package org.spongepowered.common.data.manipulator.mutable;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.ImmutableColoredData;
import org.spongepowered.api.data.manipulator.mutable.ColoredData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.manipulator.immutable.ImmutableSpongeColorData;
import org.spongepowered.common.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.common.data.value.mutable.SpongeValue;

import java.awt.Color;

public class SpongeColoredData extends AbstractSingleData<Color, ColoredData, ImmutableColoredData> implements ColoredData {

    public SpongeColoredData() {
        this(Color.BLACK);
    }

    public SpongeColoredData(Color value) {
        super(ColoredData.class, value, Keys.COLOR);
    }

    @Override
    public ColoredData copy() {
        return new SpongeColoredData(this.getValue());
    }

    @Override
    protected Value<?> getValueGetter() {
        return color();
    }

    @Override
    public ImmutableColoredData asImmutable() {
        return new ImmutableSpongeColorData(getValue());
    }

    @Override
    public int compareTo(ColoredData o) {
        return o.get(Keys.COLOR).get().getRGB() - this.getValue().getRGB();
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(Keys.COLOR.getQuery(), this.getValue().getRGB());
    }

    @Override
    public Value<Color> color() {
        return new SpongeValue<Color>(Keys.COLOR, Color.BLACK, getValue());
    }
}
