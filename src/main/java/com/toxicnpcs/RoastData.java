package com.toxicnpcs;

import java.util.*;

public class RoastData {
    public static final Map<RoastType, List<String>> DEFAULT_IN_COMBAT_ROASTS = new HashMap<RoastType, List<String>>() {{
        put(CombatRoastType.DEATH, new ArrayList<>(Arrays.asList("Sit.", "????", "u good?", "l0000000000l")));
        put(CombatRoastType.HEAVY_DAMAGE, new ArrayList<>(Collections.singletonList("hE nEeD SOmE mIlK")));
    }};

    public static final Map<RoastType, List<String>> ZULRAH_ROASTS = new HashMap<RoastType, List<String>>() {{
        put(CombatRoastType.DEATH, new ArrayList<>(Collections.singletonList("jajajajajaja")));
    }};
}
