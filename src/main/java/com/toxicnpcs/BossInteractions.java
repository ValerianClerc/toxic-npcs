package com.toxicnpcs;

import com.google.common.collect.ImmutableMap;
import net.runelite.api.NpcID;

import java.util.*;

// TODO: move roast strings to config file

enum BossInteractions {
    // Bosses with unique interactions
    CORPOREAL_BEAST(NpcID.CORPOREAL_BEAST, new HashMap<RoastType, List<String>>() {{
        put(CombatRoastType.DEATH, new ArrayList<>(Arrays.asList("????", "u good?", "l0000000000l")));
    }}),
    DEFAULT(-1, new HashMap<>());

    private static final Map<Integer, BossInteractions> bosses;
    private final int id;
    private final Map<RoastType, List<String>> inCombatRoasts;
    private static final Map<RoastType, List<String>> DEFAULT_IN_COMBAT_ROASTS = new HashMap<RoastType, List<String>>(){{
        put(CombatRoastType.DEATH, new ArrayList<>(Collections.singletonList("Sit.")));
        put(CombatRoastType.HEAVY_DAMAGE, new ArrayList<>(Collections.singletonList("hE nEeD SOmE mIlK")));
    }};

    static {
        ImmutableMap.Builder<Integer, BossInteractions> builder = new ImmutableMap.Builder<>();

        for (BossInteractions boss : values()) {
            boss.getInCombatRoasts().computeIfAbsent(CombatRoastType.DEATH, k -> new ArrayList<>()).addAll(DEFAULT_IN_COMBAT_ROASTS.get(CombatRoastType.DEATH));
            boss.getInCombatRoasts().computeIfAbsent(CombatRoastType.HEAVY_DAMAGE, k -> new ArrayList<>()).addAll(DEFAULT_IN_COMBAT_ROASTS.get(CombatRoastType.HEAVY_DAMAGE));
            builder.put(boss.getId(), boss);
        }

        bosses = builder.build();
    }

    BossInteractions(int id, Map<RoastType, List<String>> inCombatRoasts) {
        this.id = id;
        this.inCombatRoasts = inCombatRoasts;
    };

    public int getId() {
        return this.id;
    }

    public Map<RoastType, List<String>> getInCombatRoasts() {
        return this.inCombatRoasts;
    }

    // find boss interactions by boss ID, otherwise return default
    public static BossInteractions find(int id)
    {
        return bosses.getOrDefault(id, DEFAULT);
    }
}
