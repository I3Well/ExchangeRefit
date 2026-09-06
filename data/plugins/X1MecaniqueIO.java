package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.characters.OfficerDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.awt.Color;
import java.util.List;

public class X1MecaniqueIO {

    public static void exporterGabaritFile(FleetMemberAPI member, int slot, TextPanelAPI textPanel) {
        try {
            ShipVariantAPI v = member.getVariant();
            StringBuilder sb = new StringBuilder();
            String name = Global.getSector().getPlayerPerson().getNameString();
            
            // Format de date en jeu autorisé par le bac à sable Starsector
            float cyc = Global.getSector().getClock().getCycle();
            String dateInGame = "Cycle " + (int)cyc + " - " + Global.getSector().getClock().getMonthString() + " " + Global.getSector().getClock().getDay();
            
            sb.append("METADATA_COMMANDER:").append(name).append("\n");
            sb.append("METADATA_CYCLE:").append((int)cyc).append("\n");
            sb.append("METADATA_REALTIME:").append(dateInGame).append("\n");
            
            PersonAPI officer = member.getCaptain();
            if (officer != null && !officer.isDefault() && !officer.isPlayer()) {
                sb.append("OFFICER_ID:").append(officer.getId()).append("\n");
                sb.append("OFFICER_NAME:").append(officer.getNameString()).append("\n");
                sb.append("OFFICER_LEVEL:").append(officer.getStats().getLevel()).append("\n");
                sb.append("OFFICER_PERSONALITY:").append(officer.getPersonalityAPI().getId()).append("\n");
                
                String[] combatSkills = new String[]{
                    "ballistic_mastery", "energy_weapon_mastery", "ordnance_expert", 
                    "damage_control", "combat_endurance", "impact_mitigation", 
                    "field_modulation", "point_defense", "target_analysis", 
                    "systems_expertise", "gunnery_implants", "helmsmanship", 
                    "missile_specialization", "phase_corps", "wolfpack_tactics"
                };
                
                for (int i = 0; i < combatSkills.length; i++) {
                    String skillId = combatSkills[i];
                    int levelVal = (int) officer.getStats().getSkillLevel(skillId);
                    if (levelVal > 0) {
                        sb.append("OFFICER_SKILL:").append(skillId).append("-").append(levelVal).append("\n");
                    }
                }
            }
            sb.append("HULL_ID:").append(v.getHullSpec().getHullId()).append("\n");
            
            // CORRECTION DE LA FRAPPE ICI : getNumFluxCapacitors corrigé et épuré
            sb.append("FLUX_CAPACITORS:").append(v.getNumFluxCapacitors()).append("\n");
            sb.append("FLUX_VENTS:").append(v.getNumFluxVents()).append("\n");
            
            for (String mId : v.getHullMods()) {
                if (v.getSMods() != null && v.getSMods().contains(mId) && X1Plugin.ENABLE_SMOD_HANDLING) {
                    sb.append("S_HULLMOD:").append(mId).append("\n");
                } else { sb.append("HULLMOD:").append(mId).append("\n"); }
            }
            for (String sId : v.getFittedWeaponSlots()) {
                String wId = v.getWeaponId(sId);
                if (wId != null) { sb.append("ARME_SLOT:").append(sId).append("\n").append("ARME_ID:").append(wId).append("\n"); }
            }
            for (String wingId : v.getFittedWings()) {
                if (wingId != null) sb.append("FIGHTER_WING:").append(wingId).append("\n");
            }
            
            if (v.getTags() != null) {
                java.util.List tags = new java.util.ArrayList(v.getTags());
                for (int i = 0; i < tags.size(); i++) {
                    String tag = (String) tags.get(i);
                    if (tag.startsWith("agc_") || tag.contains("Gunnery") || tag.equals("agc_enabled") || tag.equals("AGC_TAG")) {
                        if (X1Plugin.ENABLE_WEAPON_GROUP_HANDLING) {
                            sb.append("AGC_TAG:").append(tag).append("\n");
                        }
                    } else if (!tag.startsWith("hull_") && !tag.equals("archetype")) {
                        sb.append("UPGRADE_TAG:").append(tag).append("\n");
                    }
                }
            }
            
            String id = v.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            String fileName = "ExchangeRefit_" + id + "_Slot" + slot + ".txt";
            
            Global.getSettings().writeTextFileToCommon(fileName, sb.toString());
            textPanel.addParagraph("[OK] Template saved to universal slot " + slot + "!", Color.CYAN);
        } catch (Exception e) { textPanel.addParagraph("[ERROR] Export failed.", Color.RED); }
    }

    public static void importerGabaritFileFiltre(FleetMemberAPI member, int slot, TextPanelAPI textPanel, 
                                             boolean parseW, boolean parseH, boolean parseS, 
                                             boolean parseT, boolean parseF, boolean parseG, boolean parseO, boolean parseUpgrades) {
        try {
            ShipVariantAPI v = member.getVariant();
            String id = member.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            String fileName = "ExchangeRefit_" + id + "_Slot" + slot + ".txt";
            String raw = Global.getSettings().readTextFileFromCommon(fileName);
            if (raw == null || raw.isEmpty()) return;

            String[] lines = raw.split("\n");
            CargoAPI soute = Global.getSector().getPlayerFleet().getCargo();
            String offId = ""; String offName = "";
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].startsWith("OFFICER_ID:")) offId = lines[i].substring(11).trim();
                if (lines[i].startsWith("OFFICER_NAME:")) offName = lines[i].substring(13).trim();
            }
            if (parseO && !offId.isEmpty()) {
                List officers = Global.getSector().getPlayerFleet().getFleetData().getOfficersCopy();
                for (int i = 0; i < officers.size(); i++) {
                    OfficerDataAPI d = (OfficerDataAPI) officers.get(i);
                    if (d.getPerson() != null && d.getPerson().getId().equals(offId)) {
                        FleetMemberAPI ancienVaisseau = Global.getSector().getPlayerFleet().getFleetData().getMemberWithCaptain(d.getPerson());
                        if (ancienVaisseau != null) {
                            ancienVaisseau.setCaptain(null);
                        }
                        member.setCaptain(d.getPerson());
                        textPanel.addParagraph("[OFFICER] " + offName + " re-assigned.", Color.CYAN); break;
                    }
                }
            }
            
            X1MecaniqueUtil.executerNettoyageVaisseauSélectif(member, !parseW, !parseH, !parseF, !parseUpgrades);
            if (parseG) v.getWeaponGroups().clear();
            
            String currentSlotTarget = ""; int wingIdx = 0;
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] == null || lines[i].isEmpty()) continue;
                String lineTrimmed = lines[i].trim();
                
                if (lineTrimmed.startsWith("UPGRADE_TAG:")) {
                    if (parseUpgrades) {
                        String tagAInjecter = lineTrimmed.substring(12).trim();
                        v.addTag(tagAInjecter);
                    }
                    continue;
                }
                
                currentSlotTarget = X1LigneParser.traiterLigneImport(lineTrimmed, v, soute, currentSlotTarget, wingIdx, parseW, parseH, parseS, parseT, parseF);
                if (lineTrimmed.startsWith("FIGHTER_WING:") && parseF) wingIdx++;
            }
            textPanel.addParagraph("[SUCCESS] Template applied successfully.", Color.GREEN);
        } catch (Exception e) { textPanel.addParagraph("[ERROR] Import failed.", Color.RED); }
    }
}
