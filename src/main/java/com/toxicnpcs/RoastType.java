package com.toxicnpcs;

// Because why not get roasted by everything
interface RoastType {
    boolean isOnCooldown(long startTime);
}

// Roasts for when in combat
enum CombatRoastType implements RoastType {
    HEAVY_DAMAGE(30000),
    DEATH(2000);

    private final int cooldown;

    CombatRoastType(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isOnCooldown(long startTime) {
        return startTime + cooldown > System.currentTimeMillis();
    }
}

// TODO: Roasts for scrub stats
// TODO: Roasts for when wearing certain gear
