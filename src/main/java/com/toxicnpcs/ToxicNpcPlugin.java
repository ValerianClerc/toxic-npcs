package com.toxicnpcs;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "NPCs will now roast you when they kill you"
)
public class ToxicNpcPlugin extends Plugin
{
	private Actor lastInteractedWith = null;

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
		if (source != null && !(source instanceof Player)) {
			lastInteractedWith = source;
		}
	}

	@Subscribe
	public void onActorDeath(ActorDeath event) {
		Actor actor = event.getActor();
        if (actor instanceof Player) {
			lastInteractedWith.setOverheadText("Sit.");
			client.addChatMessage(ChatMessageType.PUBLICCHAT, lastInteractedWith.getName(), "Sit.", null);
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							lastInteractedWith.setOverheadText("");
						}
					},
					3000
			);
		}
	}

	@Provides
	ToxicNpcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ToxicNpcConfig.class);
	}
}
