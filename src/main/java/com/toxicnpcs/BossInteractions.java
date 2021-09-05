package com.toxicnpcs;

import com.google.common.collect.ImmutableMap;
import net.runelite.api.NpcID;

import java.util.*;

enum BossInteractions {
    // Bosses with unique interactions
    ZULRAH_1(NpcID.ZULRAH, 40, RoastData.ZULRAH_ROASTS),
    ZULRAH_2(NpcID.ZULRAH_2043, 40, RoastData.ZULRAH_ROASTS),
    ZULRAH_3(NpcID.ZULRAH_2044, 40, RoastData.ZULRAH_ROASTS),
    DEFAULT(-1, 80, new HashMap<>());

    private static final Map<Integer, BossInteractions> bosses;
    private final int id;
    private final int heavyDamageThreshold;
    private final Map<RoastType, List<String>> inCombatRoasts;

    static {
        ImmutableMap.Builder<Integer, BossInteractions> builder = new ImmutableMap.Builder<>();

        for (BossInteractions boss : values()) {
            boss.getInCombatRoasts().computeIfAbsent(CombatRoastType.DEATH, k -> new ArrayList<>()).addAll(RoastData.DEFAULT_IN_COMBAT_ROASTS.get(CombatRoastType.DEATH));
            boss.getInCombatRoasts().computeIfAbsent(CombatRoastType.HEAVY_DAMAGE, k -> new ArrayList<>()).addAll(RoastData.DEFAULT_IN_COMBAT_ROASTS.get(CombatRoastType.HEAVY_DAMAGE));
            builder.put(boss.getId(), boss);
        }

        bosses = builder.build();
    }

    BossInteractions(int id, int heavyDamageThreshold, Map<RoastType, List<String>> inCombatRoasts) {
        this.id = id;
        this.heavyDamageThreshold = heavyDamageThreshold;
        this.inCombatRoasts = inCombatRoasts;
    }

    public int getId() {
        return this.id;
    }

    public int getHeavyDamageThreshold() {
        return this.heavyDamageThreshold;
    }

    public Map<RoastType, List<String>> getInCombatRoasts() {
        return this.inCombatRoasts;
    }

    // find boss interactions by boss ID, otherwise return default
    public static BossInteractions find(int id) {
        return bosses.getOrDefault(id, DEFAULT);
    }
}
