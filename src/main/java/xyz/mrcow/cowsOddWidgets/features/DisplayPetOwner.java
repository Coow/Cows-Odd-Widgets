package xyz.mrcow.cowsOddWidgets.features;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.TameableEntity;
import org.jetbrains.annotations.NotNull;
import xyz.mrcow.cowsOddWidgets.CowsOddWidgets;
import xyz.mrcow.cowsOddWidgets.mixin.FoxEntityMixin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//https://github.com/PotatoPresident/PetOwner/blob/c85c07042785b7f87ed68993109e8954e8f225ed/src/main/java/us/potatoboy/petowner/client/PetOwnerClient.java

public class DisplayPetOwner {

    private static final LoadingCache<UUID, Optional<String>> usernameCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Optional<String> load(@NotNull UUID key) {
                    CompletableFuture.runAsync(() -> {
                        GameProfile playerProfile = new GameProfile(key, "null");
                        playerProfile = MinecraftClient.getInstance().getSessionService().fetchProfile(playerProfile.getId(), false).profile();
                        if (playerProfile == null) {
                            usernameCache.put(key, Optional.empty());
                        } else {
                            usernameCache.put(key, Optional.ofNullable(playerProfile.getName()));
                        }


                    });

                    return Optional.of("Waiting...");
                }
            });

    public static Optional<String> getNameFromId(UUID uuid) {
        return usernameCache.getUnchecked(uuid);
    }

    public static List<UUID> getOwnerIds(Entity entity) {
        if (entity instanceof TameableEntity tameableEntity && tameableEntity.isTamed()) {
                return Collections.singletonList(tameableEntity.getOwnerUuid());
        }

        if (entity instanceof HorseEntity horseEntity && horseEntity.isTame()) {
                return Collections.singletonList(horseEntity.getOwnerUuid());
        }

        if (entity instanceof FoxEntity foxEntity) {
            return ((FoxEntityMixin) foxEntity).getTrustedIds();
        }

        return new ArrayList<>();
    }
}
