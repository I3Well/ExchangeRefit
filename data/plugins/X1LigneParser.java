package data.plugins;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;

public class X1LigneParser {

    public static String traiterLigneImport(String cleanLine, ShipVariantAPI v, CargoAPI soute, String targetSlot, int wingIdx,
                                             boolean parseWeapons, boolean parseHullmods, boolean parseSMods, boolean parseTags, boolean parseFighters) {
        
        if (parseHullmods && cleanLine.startsWith("FLUX_CAPACITORS:")) {
            v.setNumFluxCapacitors(Integer.parseInt(cleanLine.substring(16).trim()));
        }
        if (parseHullmods && cleanLine.startsWith("FLUX_VENTS:")) {
            v.setNumFluxVents(Integer.parseInt(cleanLine.substring(11).trim()));
        }
        if (parseHullmods && cleanLine.startsWith("HULLMOD:")) {
            v.addMod(cleanLine.substring(8).trim());
        }
        if (cleanLine.startsWith("S_HULLMOD:")) {
            String modId = cleanLine.substring(10).trim();
            if (parseSMods && X1Plugin.ENABLE_SMOD_HANDLING) {
                v.addPermaMod(modId, true);
            } else if (parseHullmods) {
                v.addMod(modId);
            }
        }
        if (cleanLine.startsWith("ARME_SLOT:")) {
            return cleanLine.substring(10).trim();
        }
        if (parseWeapons && cleanLine.startsWith("ARME_ID:") && !targetSlot.isEmpty()) {
            String wId = cleanLine.substring(8).trim();
            if (soute.getQuantity(com.fs.starfarer.api.campaign.CargoAPI.CargoItemType.WEAPONS, wId) >= 1) {
                v.clearSlot(targetSlot);
                v.addWeapon(targetSlot, wId);
                soute.removeWeapons(wId, 1);
            }
            return "";
        }
        if (parseFighters && X1Plugin.ENABLE_FIGHTER_HANDLING && cleanLine.startsWith("FIGHTER_WING:")) {
            String wingId = cleanLine.substring(13).trim();
            if (soute.getNumFighters(wingId) >= 1) {
                v.setWingId(wingIdx, wingId);
                soute.removeFighters(wingId, 1);
            }
        }
        if (parseTags && X1Plugin.ENABLE_WEAPON_GROUP_HANDLING && cleanLine.startsWith("AGC_TAG:")) {
            v.addTag(cleanLine.substring(8).trim());
        }
        return targetSlot;
    }
}
