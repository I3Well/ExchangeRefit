package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class X1MecaniqueUtil {

    public static boolean gabaritExiste(FleetMemberAPI member, int slot) {
        try {
            if (member == null) return false;
            if (member.getHullSpec() == null) return false;
            String id = member.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            String fileName = "ExchangeRefit_" + id + "_Slot" + slot + ".txt";
            String data = Global.getSettings().readTextFileFromCommon(fileName);
            return (data != null && !data.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

    public static float getCalculerPorteeMaxArmes(FleetMemberAPI member) {
        if (member == null) return 0f;
        if (member.getVariant() == null) return 0f;
        float maxRange = 0f;
        ShipVariantAPI v = member.getVariant();
        List slots = new ArrayList(v.getFittedWeaponSlots());
        for (int i = 0; i < slots.size(); i++) {
            String slotId = (String) slots.get(i);
            String wId = v.getWeaponId(slotId);
            if (wId != null) {
                WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
                if (spec != null && spec.getMaxRange() > maxRange) {
                    maxRange = spec.getMaxRange();
                }
            }
        }
        return maxRange;
    }

    public static String determinerTechnologieMajoritaire(FleetMemberAPI member) {
        if (member == null) return "NEUTRAL";
        if (member.getVariant() == null) return "NEUTRAL";
        ShipVariantAPI v = member.getVariant();
        int lowTechCount = 0;
        int highTechCount = 0;
        int midTechCount = 0;

        List slots = new ArrayList(v.getFittedWeaponSlots());
        for (int i = 0; i < slots.size(); i++) {
            String slotId = (String) slots.get(i);
            String wId = v.getWeaponId(slotId);
            if (wId != null) {
                WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
                if (spec != null) {
                    String type = spec.getType().name().toLowerCase();
                    if (type.contains("ballistic")) lowTechCount++;
                    else if (type.contains("energy")) highTechCount++;
                    else if (type.contains("missile")) midTechCount++;
                }
            }
        }

        if (lowTechCount == 0 && highTechCount == 0 && midTechCount == 0) {
            if (member.getHullSpec() != null && member.getHullSpec().getManufacturer() != null) {
                String manu = member.getHullSpec().getManufacturer().toLowerCase();
                if (manu.contains("low") || manu.contains("hegemony")) return "LOW_TECH";
                if (manu.contains("high") || manu.contains("tri-tachyon")) return "HIGH_TECH";
                if (manu.contains("mid") || manu.contains("league")) return "MID_TECH";
            }
            return "NEUTRAL";
        }

        if (lowTechCount >= highTechCount && lowTechCount >= midTechCount) return "LOW_TECH";
        if (highTechCount >= lowTechCount && highTechCount >= midTechCount) return "HIGH_TECH";
        return "MID_TECH";
    }

    public static void executerSuppression(FleetMemberAPI member, int slot, TextPanelAPI textPanel) {
        try {
            if (gabaritExiste(member, slot)) {
                String id = member.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
                String fileName = "ExchangeRefit_" + id + "_Slot" + slot + ".txt";
                Global.getSettings().writeTextFileToCommon(fileName, "");
                textPanel.addParagraph("[CLEANUP] Blueprint content has been wiped from saves/common/ directory.", Color.YELLOW);
            }
        } catch (Exception e) {}
    }

    public static void supprimerSModsEtRembourser(FleetMemberAPI member, TextPanelAPI textPanel) {
        X1SModStripper.nettoyerVaisseauSMods(member, textPanel, true);
    }

    public static void executerNettoyageVaisseauSélectif(FleetMemberAPI member, boolean skipWeapons, boolean skipHullmods, boolean skipFighters, boolean skipTags) {
        try {
            if (member == null) return;
            if (member.getVariant() == null) return;
            ShipVariantAPI v = member.getVariant();
            CargoAPI soute = Global.getSector().getPlayerFleet().getCargo();

            if (!skipWeapons) {
                List slots = new ArrayList(v.getFittedWeaponSlots());
                for (int i = 0; i < slots.size(); i++) {
                    String slotId = (String) slots.get(i);
                    String oldW = v.getWeaponId(slotId);
                    if (oldW != null && !oldW.isEmpty()) {
                        v.clearSlot(slotId);
                        soute.addWeapons(oldW, 1);
                    }
                }
            }

            if (!skipFighters) {
                for (int i = 0; i < v.getFittedWings().size(); i++) {
                    v.setWingId(i, null);
                }
            }

            if (!skipHullmods) {
                List hMods = new ArrayList(v.getHullMods());
                for (int i = 0; i < hMods.size(); i++) {
                    String modId = (String) hMods.get(i);
                    if (v.getHullSpec().getBuiltInMods() == null || !v.getHullSpec().getBuiltInMods().contains(modId)) {
                        v.removeMod(modId);
                        v.removePermaMod(modId);
                    }
                }
                v.setNumFluxCapacitors(0);
                v.setNumFluxVents(0);
            }

            if (!skipTags && v.getTags() != null) {
                List tagsACleanner = new ArrayList(v.getTags());
                for (int i = 0; i < tagsACleanner.size(); i++) {
                    String tag = (String) tagsACleanner.get(i);
                    if (!tag.startsWith("hull_") && !tag.equals("archetype") && !tag.equals("agc_enabled") && !tag.equals("AGC_TAG")) {
                        v.removeTag(tag);
                    }
                }
            }
        } catch (Exception e) {}
    }

    public static void exporterGabarit(FleetMemberAPI member, int slot, TextPanelAPI textPanel) {
        X1MecaniqueIO.exporterGabaritFile(member, slot, textPanel);
    }

    // Réalignement complet de la signature avec l'Option 8 (parseUpgrades)
    public static void importerGabaritFiltre(FleetMemberAPI member, int slot, TextPanelAPI textPanel, 
                                             boolean parseWeapons, boolean parseHullmods, boolean parseSMods, 
                                             boolean parseTags, boolean parseFighters, boolean parseGroups, boolean parseOfficer, boolean parseUpgrades) {
        X1MecaniqueIO.importerGabaritFileFiltre(member, slot, textPanel, parseWeapons, parseHullmods, parseSMods, parseTags, parseFighters, parseGroups, parseOfficer, parseUpgrades);
    }
}
