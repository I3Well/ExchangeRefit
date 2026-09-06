package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;

public class X1SpecExtractor {

    public static String extraireArme(String wId) {
        WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
        if (spec == null) return null;
        
        String origin = X1ModChecker.getNomModOrigine(wId, false);
        int range = (int) spec.getMaxRange();
        String dmgType = (spec.getDamageType() != null) ? spec.getDamageType().getDisplayName() : "None";
        int opCost = (int) spec.getOrdnancePointCost(null);

        return " - " + spec.getWeaponName() + " [" + origin + "] (Range: " + range + " | Dmg: " + dmgType + " | Cost: " + opCost + " OP)";
    }

    /**
     * Récupère le type de dégâts interne (KINETIC, HIGH_EXPLOSIVE, ENERGY, FRAGMENTATION) pour la couleur
     */
    public static String getArmeDamageType(String wId) {
        WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
        if (spec != null && spec.getDamageType() != null) {
            return spec.getDamageType().name();
        }
        return "OTHER";
    }

    public static int getArmeOpCost(String wId) {
        WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
        if (spec != null) return (int) spec.getOrdnancePointCost(null);
        return 0;
    }

    public static String getArmeSize(String wId) {
        WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
        if (spec != null && spec.getSize() != null) return spec.getSize().name();
        return "SMALL";
    }

    public static String extraireHullmod(String mId, FleetMemberAPI member, boolean isSMod) {
        HullModSpecAPI spec = Global.getSettings().getHullModSpec(mId);
        if (spec == null) return null;
        
        String origin = X1ModChecker.getNomModOrigine(mId, true);
        int opCost = getHullmodOpCost(mId, member);

        String prefix = isSMod ? "Base Cost: " : "Cost: ";
        return " - " + spec.getDisplayName() + " [" + origin + "] (" + prefix + opCost + " OP)";
    }

    public static int getHullmodOpCost(String mId, FleetMemberAPI member) {
        HullModSpecAPI spec = Global.getSettings().getHullModSpec(mId);
        if (spec != null && member != null && member.getVariant() != null) {
            return spec.getCostFor(member.getVariant().getHullSize());
        }
        return 0;
    }
}
