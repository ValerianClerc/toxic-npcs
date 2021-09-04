package com.toxicnpcs;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("toxicnpcs")
public interface ToxicNpcConfig extends Config {
    @ConfigItem(
            position = 1,
            keyName = "globalCooldown",
            name = "Global Cooldown (sec)",
            description = "Sets a cooldown between roasts"
    )
    default int globalCooldown() {
        return 30;
    }
}
