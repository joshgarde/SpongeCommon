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
package org.spongepowered.common.mixin.core.entity.player;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.mutable.entity.AchievementData;
import org.spongepowered.api.data.manipulator.mutable.entity.BanData;
import org.spongepowered.api.data.manipulator.mutable.entity.StatisticData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.entity.player.SpongeUser;
import org.spongepowered.common.interfaces.IMixinSubject;

@Mixin(value = SpongeUser.class, remap = false)
public abstract class MixinSpongeUser implements User, IMixinSubject {

    @Shadow private com.mojang.authlib.GameProfile profile;

    @Override
    public GameProfile getProfile() {
        return (GameProfile) this.profile;
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer().isPresent();
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.fromNullable((Player) MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(this.profile.getId()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<CommandSource> getCommandSource() {
        return (Optional) getPlayer();
    }

    @Override
    public AchievementData getAchievementData() {
        throw new UnsupportedOperationException(); // TODO Achievement Data
    }

    @Override
    public StatisticData getStatisticData() {
        throw new UnsupportedOperationException(); // TODO Statistic Data
    }

    @Override
    public BanData getBanData() {
        throw new UnsupportedOperationException(); // TODO Ban Data
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        throw new UnsupportedOperationException(); // TODO Data API
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {
        throw new UnsupportedOperationException(); // TODO Data API
    }

    @Override
    public String getSubjectCollectionIdentifier() {
        return PermissionService.SUBJECTS_USER;
    }

    @Override
    public Tristate permDefault(String permission) {
        return Tristate.FALSE;
    }

    @Override
    public String getIdentifier() {
        return this.profile.getId().toString();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("isOnline", this.isOnline())
                .add("profile", this.getProfile())
                .toString();
    }
}
