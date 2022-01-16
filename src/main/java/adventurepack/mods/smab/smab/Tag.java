package adventurepack.mods.smab.smab;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Miscellanies Tags to help descript a SmabSpecies.
 * Feel free to add as many as you want
 */
public enum Tag {
    ELEMENTAL,
    LIVING,
    UNDEAD,
    MECH,
    ENDER,
    NETHER,
    OVERWORLD,
    GHASTLY,

    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    ABNORMAL,
    ;
    Tag() {

    }

    public boolean is(Smab smab) {
        return smab.hasTag(this);
    }

    public static Set<Tag> create(Tag... tags) {
        switch(tags.length) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(tags[0]);
            case 2:
                return EnumSet.of(tags[0], tags[1]);
            default:
                Tag[] t = new Tag[tags.length-2];
                System.arraycopy(tags, 1, t, 0, tags.length-2);
                return EnumSet.of(tags[0], t);
        }
    }
}
