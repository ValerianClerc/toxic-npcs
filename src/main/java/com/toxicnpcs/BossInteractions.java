package com.toxicnpcs;

import com.google.common.collect.ImmutableMap;
import net.runelite.api.NpcID;

import java.util.*;

enum BossInteractions {
    // God wars
    GENERAL_GRAARDOR_1(NpcID.GENERAL_GRAARDOR, 56, new HashMap<>()),
    GENERAL_GRAARDOR_2(NpcID.GENERAL_GRAARDOR_6494, 56, new HashMap<>()),
    KRIL_TSUTSAROTH_1(NpcID.KRIL_TSUTSAROTH, 45, new HashMap<>()),
    KRIL_TSUTSAROTH_2(NpcID.KRIL_TSUTSAROTH_6495, 45, new HashMap<>()),
    KREEARRA_1(NpcID.KREEARRA, 50, new HashMap<>()),
    KREEARRA_2(NpcID.KREEARRA_6492, 50, new HashMap<>()),
    COMMANDER_ZILYANA_1(NpcID.COMMANDER_ZILYANA, 25, new HashMap<>()),
    COMMANDER_ZILYANA_2(NpcID.COMMANDER_ZILYANA_6493, 25, new HashMap<>()),
    // Wildy bosses
    CALLISTO_1(NpcID.CALLISTO, 50, new HashMap<>()),
    CALLISTO_2(NpcID.CALLISTO_6609, 50, new HashMap<>()),
    VENENATIS_1(NpcID.VENENATIS, 47, new HashMap<>()),
    VENENATIS_2(NpcID.VENENATIS_6610, 47, new HashMap<>()),
    VETION(NpcID.VETION, 42, new HashMap<>()),
    // Other
    ZULRAH_1(NpcID.ZULRAH, 40, RoastData.ZULRAH_ROASTS),
    ZULRAH_2(NpcID.ZULRAH_2043, 40, RoastData.ZULRAH_ROASTS),
    ZULRAH_3(NpcID.ZULRAH_2044, 40, RoastData.ZULRAH_ROASTS),
    CORPOREAL_BEAST(NpcID.CORPOREAL_BEAST, 49, new HashMap<>()),
    // Raids 1
    TEKTON_1(NpcID.TEKTON, 48, new HashMap<>()),
    TEKTON_2(NpcID.TEKTON_7541, 48, new HashMap<>()),
    TEKTON_3(NpcID.TEKTON_7542, 48, new HashMap<>()),
    TEKTON_4(NpcID.TEKTON_7545, 48, new HashMap<>()),
    TEKTON_5(NpcID.TEKTON_ENRAGED, 55, new HashMap<>()),
    TEKTON_6(NpcID.TEKTON_ENRAGED_7544, 55, new HashMap<>()),
    MUTTADILE_1(NpcID.MUTTADILE, 60, new HashMap<>()),
    MUTTADILE_2(NpcID.MUTTADILE_7562, 60, new HashMap<>()),
    MUTTADILE_3(NpcID.MUTTADILE_7563, 60, new HashMap<>()),
    OLM_1(NpcID.GREAT_OLM, 40, new HashMap<>()),
    OLM_2(NpcID.GREAT_OLM_7554, 40, new HashMap<>()),
    // Raids 2
    PESTILENT_BLOAT_1(NpcID.PESTILENT_BLOAT, 60, new HashMap<>()),
    PESTILENT_BLOAT_2(NpcID.PESTILENT_BLOAT_10812, 60, new HashMap<>()),
    PESTILENT_BLOAT_3(NpcID.PESTILENT_BLOAT_10813, 60, new HashMap<>()),
    PESTILENT_BLOAT_4(NpcID.PESTILENT_BLOAT_11184, 60, new HashMap<>()),
    // Default
    DEFAULT(-1, 45, new HashMap<>());

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
