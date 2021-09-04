package com.toxicnpcs;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@PluginDescriptor(
	name = "NPCs will now roast you when they kill you"
)
public class ToxicNpcPlugin extends Plugin
{
	private static final int HEAVY_DAMAGE_THRESHOLD = 40;
	private static final int OVERHEAD_TEXT_TIMEOUT = 3000;

	private NPC lastInteractedWith = null;

	@Inject
	private Client client;

	@Inject
	private ToxicNpcConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("ToxicNpc started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("ToxicNpc stopped!");
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged event) {
		Actor source = event.getSource();
		if (source instanceof NPC && source.getName() != null) {
			lastInteractedWith = (NPC)source;
		}
	}

	@Subscribe
	public void onActorDeath(ActorDeath event) {
		Actor actor = event.getActor();
        if (actor instanceof Player) {
			Map<RoastType, List<String>> roasts = BossInteractions.find(lastInteractedWith.getId()).getInCombatRoasts();
			String roast = chooseRoast(roasts, CombatRoastType.DEATH);

			deliverRoast(client, lastInteractedWith, roast);
		}
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event) {
		if (event.getHitsplat().getAmount() >= HEAVY_DAMAGE_THRESHOLD) {
			Map<RoastType, List<String>> roasts = BossInteractions.find(lastInteractedWith.getId()).getInCombatRoasts();
			String roast = chooseRoast(roasts, CombatRoastType.HEAVY_DAMAGE);

			deliverRoast(client, lastInteractedWith, roast);
		}
	}

	private static String chooseRoast(Map<RoastType, List<String>> roasts, RoastType roastType) {
		Random random = new Random(System.currentTimeMillis());
		List<String> roastList = roasts.get(roastType);
		return roastList.get(random.nextInt(roastList.size()));
	}

	private static void deliverRoast(Client client, NPC bully, String roast) {
		client.addChatMessage(ChatMessageType.PUBLICCHAT, bully.getName(), roast, null);
		bully.setOverheadText(roast);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						bully.setOverheadText("");
					}
				},
				OVERHEAD_TEXT_TIMEOUT
		);
	}

	@Provides
	ToxicNpcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ToxicNpcConfig.class);
	}
}
