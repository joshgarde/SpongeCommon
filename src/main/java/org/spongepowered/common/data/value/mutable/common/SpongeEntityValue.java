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
package org.spongepowered.common.data.value.mutable.common;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.common.data.value.mutable.SpongeOptionalValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

public class SpongeEntityValue extends SpongeOptionalValue<Entity> {

    private WeakReference<Entity> weakReference = new WeakReference<Entity>(null);

    public SpongeEntityValue(Key<? extends BaseValue<Optional<Entity>>> key) {
        super(key);
    }

    public SpongeEntityValue(Key<? extends BaseValue<Optional<Entity>>> key, Optional<Entity> actualValue) {
        super(key, Optional.<Entity>absent());
        if (actualValue.isPresent()) {
            this.weakReference = new WeakReference<Entity>(actualValue.get());
        } else {
            this.weakReference.clear();
        }
    }

    @Override
    public Optional<Entity> get() {
        return fromNullable(this.weakReference.get());
    }

    @Override
    public boolean exists() {
        return this.weakReference.get() != null;
    }

    @Override
    public Optional<Optional<Entity>> getDirect() {
        return super.getDirect();
    }

    @Override
    public OptionalValue<Entity> set(Optional<Entity> value) {
        if (value.isPresent()) {
            this.weakReference = new WeakReference<Entity>(value.get());
        } else {
            this.weakReference.clear();
        }
        return this;
    }

    @Override
    public OptionalValue<Entity> transform(Function<Optional<Entity>, Optional<Entity>> function) {
        final Optional<Entity> optional = checkNotNull(checkNotNull(function).apply(fromNullable(this.weakReference.get())));
        if (optional.isPresent()) {
            this.weakReference = new WeakReference<Entity>(optional.get());
        } else {
            this.weakReference.clear();
        }
        return this;
    }

    @Override
    public ImmutableOptionalValue<Entity> asImmutable() {
        return super.asImmutable();
    }

    @Override
    public OptionalValue<Entity> setTo(@Nullable Entity value) {
        this.weakReference = new WeakReference<Entity>(value);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Value<Entity> or(Entity defaultValue) {
        final Key<Value<Entity>> key = KeyFactory.makeSingleKey(Entity.class, (Class<Value<Entity>>) (Class) Value.class, this.getKey().getQuery());
        return exists() ? new SpongeValue<Entity>(key, this.weakReference.get()) : new SpongeValue<Entity>(key, checkNotNull(defaultValue));
    }
}
