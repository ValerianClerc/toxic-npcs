package com.toxicnpcs;

import java.util.*;

public class RoastData {
    public static final Map<RoastType, List<String>> DEFAULT_IN_COMBAT_ROASTS = new HashMap<RoastType, List<String>>() {{
        put(CombatRoastType.DEATH, new ArrayList<>(Arrays.asList("Sit.", "????", "l0000000000l", "Gf noob", "Cya in lumby", "00f")));
        put(CombatRoastType.HEAVY_DAMAGE, new ArrayList<>(Arrays.asList("u good?", "safe up bro", "Check this fat xp drop")));
    }};

    public static final Map<RoastType, List<String>> ZULRAH_ROASTS = new HashMap<RoastType, List<String>>() {{
        put(CombatRoastType.DEATH, new ArrayList<>(Collections.singletonList("jajajajajaja")));
        put(CombatRoastType.HEAVY_DAMAGE, new ArrayList<>(Collections.singletonList("jajajajajaja")));
    }};
}
