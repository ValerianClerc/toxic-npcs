package com.toxicnpcs;

// Because why not get roasted by everything
interface RoastType {}

// Roasts for when in combat
enum CombatRoastType implements RoastType {
    HEAVY_DAMAGE,
    DEATH;
}

// TODO: Roasts for scrub stats
// TODO: Roasts for when wearing certain gear
