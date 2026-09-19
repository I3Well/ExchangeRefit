package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import java.util.List;

public class X1LigneParser {

    public static String traiterLigneImport(String line, ShipVariantAPI v, CargoAPI soute, String currentSlot, int wingIdx, 
                                           boolean parseW, boolean parseH, boolean parseS, boolean parseT, boolean parseF, 
                                           List elementsManquants) {
        try {
            if (line == null || line.isEmpty()) return currentSlot;

            // 1. Emplacement d'arme
            if (line.startsWith("ARME_SLOT:")) {
                return line.substring(10).trim();
            }

            // 2. Installation de l'arme
            if (line.startsWith("ARME_ID:") && parseW) {
                String weaponId = line.substring(8).trim();
                if (!currentSlot.isEmpty() && weaponId != null && !weaponId.isEmpty()) {
                    if (soute.getNumWeapons(weaponId) > 0) {
                        v.clearSlot(currentSlot);
                        v.addWeapon(currentSlot, weaponId);
                        soute.removeWeapons(weaponId, 1);
                    }
                }
                return "";
            }

            // 3. Condensateurs & Diffuseurs
            if (line.startsWith("FLUX_CAPACITORS:") && parseH) {
                v.setNumFluxCapacitors(Integer.parseInt(line.substring(16).trim()));
                return currentSlot;
            }
            if (line.startsWith("FLUX_VENTS:") && parseH) {
                v.setNumFluxVents(Integer.parseInt(line.substring(11).trim()));
                return currentSlot;
            }

            // 4. Hullmods Standards
            if (line.startsWith("HULLMOD:") && parseH) {
                String modId = line.substring(8).trim();
                if (modId != null && !modId.isEmpty() && !v.getHullMods().contains(modId)) {
                    v.addMod(modId);
                }
                return currentSlot;
            }

            // 5. Story Modifications (S-Mods)
            if (line.startsWith("S_HULLMOD:") && parseS) {
                boolean enableSmodHandling = true;
                if (Global.getSector() != null && Global.getSector().getMemoryWithoutUpdate() != null) {
                    if (Global.getSector().getMemoryWithoutUpdate().contains("$exchangerefit_hasProgSMods")) {
                        enableSmodHandling = !Global.getSector().getMemoryWithoutUpdate().getBoolean("$exchangerefit_hasProgSMods");
                    }
                }

                if (enableSmodHandling) {
                    String modId = line.substring(10).trim();
                    if (modId != null && !modId.isEmpty()) {
                        v.addPermaMod(modId, true);
                    }
                }
                return currentSlot;
            }

            // 6. Escadrilles de Chasseurs
            if (line.startsWith("FIGHTER_WING:") && parseF) {
                String wingId = line.substring(13).trim();
                if (wingId != null && !wingId.isEmpty()) {
                    if (soute.getNumFighters(wingId) > 0) {
                        v.setWingId(wingIdx, wingId);
                        soute.removeFighters(wingId, 1);
                    }
                }
                return currentSlot;
            }

            // 7. Automation (AGC / AI Tweaks)
            if (line.startsWith("AGC_TAG:") && parseT) {
                String tagId = line.substring(8).trim();
                if (tagId != null && !tagId.isEmpty()) {
                    v.addTag(tagId);
                }
                return currentSlot;
            }

        } catch (Exception e) {
            // Sécurité silencieuse
        }
        return currentSlot;
    }
}
