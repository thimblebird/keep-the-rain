package io.thimblebird;

import java.util.Timer;
import java.util.TimerTask;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;

public class KeepTheRain2 implements ModInitializer {
	@Override
	public void onInitialize() {
		EntitySleepEvents.STOP_SLEEPING.register((LivingEntity entity, BlockPos pos) -> {
			World entityWorld = entity.getWorld();

			if (entityWorld.isClient() || !entity.isPlayer() || !entity.isAlive() || entity.isSpectator()) {
				return;
			}

			if (entityWorld.isRaining()) {
				MinecraftServer server = entityWorld.getServer();
				ServerWorldProperties serverWorldProps = server.getSaveProperties().getMainWorldProperties();
				int serverRainTime = serverWorldProps.getRainTime();

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						serverWorldProps.setRaining(true);
						serverWorldProps.setRainTime(serverRainTime);
					}
				}, 10);
			}
		});
	}
}