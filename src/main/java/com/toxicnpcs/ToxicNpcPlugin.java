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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@PluginDescriptor(
        name = "Toxic NPCs",
        description = "NPCs will now roast you"
)
public class ToxicNpcPlugin extends Plugin {
    private static final int HEAVY_DAMAGE_THRESHOLD = 40;
    private static final int OVERHEAD_TEXT_TIMEOUT = 3000;

    // String key = Player name
    private final Map<String, NPC> lastInteractions = new HashMap<>();
    private final Map<RoastType, Long> roastHistory = new HashMap<>();
    private volatile long lastRoastTime = 0;

    @Inject
    private Client client;

    @Inject
    private ToxicNpcConfig config;

    @Override
    protected void startUp() throws Exception {
        log.info("ToxicNpc started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("ToxicNpc stopped!");
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        Actor source = event.getSource();
        Actor target = event.getTarget();
        if (source instanceof NPC && target instanceof Player && source.getName() != null && target.getName() != null) {
            lastInteractions.put(target.getName(), (NPC) source);
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath event) {
        Actor actor = event.getActor();
        if (actor instanceof Player && actor.getName() != null) {
            Player player = (Player) actor;
            NPC killer = lastInteractions.get(player.getName());

            NPC lastInteractedWith = lastInteractions.get(player.getName());
            if (lastInteractedWith != null) {
                Map<RoastType, List<String>> roasts = BossInteractions.find(lastInteractedWith.getId()).getInCombatRoasts();
                String roast = chooseRoast(roasts, CombatRoastType.DEATH);

                deliverRoast(client, lastInteractedWith, roast, CombatRoastType.DEATH);
            }
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        if (event.getActor() instanceof Player && event.getActor().getName() != null && event.getHitsplat().getAmount() >= HEAVY_DAMAGE_THRESHOLD) {
            Player player = (Player) event.getActor();
            NPC lastInteractedWith = lastInteractions.get(player.getName());

            if (lastInteractedWith != null) {
                Map<RoastType, List<String>> roasts = BossInteractions.find(lastInteractedWith.getId()).getInCombatRoasts();
                String roast = chooseRoast(roasts, CombatRoastType.HEAVY_DAMAGE);

                deliverRoast(client, lastInteractedWith, roast, CombatRoastType.HEAVY_DAMAGE);
            }
        }
    }

    private String chooseRoast(Map<RoastType, List<String>> roasts, RoastType roastType) {
        Random random = new Random(System.currentTimeMillis());
        List<String> roastList = roasts.get(roastType);
        return roastList.get(random.nextInt(roastList.size()));
    }

    private void deliverRoast(Client client, NPC bully, String roast, RoastType roastType) {
        if (roastHistory.get(roastType) == null || !roastType.isOnCooldown(roastHistory.get(roastType))) {

            int globalCD = config.globalCooldown() * 1000;
            if (lastRoastTime + globalCD > System.currentTimeMillis()) {
                return;
            }

            client.addChatMessage(ChatMessageType.PUBLICCHAT, bully.getName(), roast, null);
            bully.setOverheadText(roast);
            lastRoastTime = System.currentTimeMillis();
            roastHistory.put(roastType, lastRoastTime);

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if (lastRoastTime <= roastHistory.get(roastType)) {
                                bully.setOverheadText("");
                            }
                        }
                    },
                    OVERHEAD_TEXT_TIMEOUT
            );
        }
    }

    @Provides
    ToxicNpcConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ToxicNpcConfig.class);
    }
}
