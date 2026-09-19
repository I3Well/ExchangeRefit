package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class X1MecaniqueExport {

    public static String echapperValeur(String text) {
        if (text == null) return "";
        return text.replace(":", "§§");
    }

    public static void executerExportationBrute(FleetMemberAPI member, int slot, TextPanelAPI textPanel) {
        try {
            ShipVariantAPI v = member.getVariant();
            StringBuilder sb = new StringBuilder();
            String name = Global.getSector().getPlayerPerson().getNameString();
            
            float cyc = Global.getSector().getClock().getCycle();
            String dateInGame = "Cycle " + (int)cyc + " - " + Global.getSector().getClock().getMonthString() + " " + Global.getSector().getClock().getDay();
            
            sb.append("METADATA_COMMANDER:").append(echapperValeur(name)).append("\n");
            sb.append("METADATA_CYCLE:").append((int)cyc).append("\n");
            sb.append("METADATA_REALTIME:").append(echapperValeur(dateInGame)).append("\n");
            
            PersonAPI officer = member.getCaptain();
            if (officer != null && !officer.isDefault() && !officer.isPlayer()) {
                sb.append("OFFICER_ID:").append(officer.getId()).append("\n");
                sb.append("OFFICER_NAME:").append(echapperValeur(officer.getNameString())).append("\n");
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
                    int levelVal = (int) officer.getStats().getSkillLevel(combatSkills[i]);
                    if (levelVal > 0) sb.append("OFFICER_SKILL:").append(combatSkills[i]).append("-").append(levelVal).append("\n");
                }
            }
            sb.append("HULL_ID:").append(v.getHullSpec().getHullId()).append("\n");
            sb.append("FLUX_CAPACITORS:").append(v.getNumFluxCapacitors()).append("\n");
            sb.append("FLUX_VENTS:").append(v.getNumFluxVents()).append("\n");
            
            boolean enableSmodHandling = true;
            if (Global.getSector() != null && Global.getSector().getMemoryWithoutUpdate() != null) {
                if (Global.getSector().getMemoryWithoutUpdate().contains("$exchangerefit_hasProgSMods")) {
                    enableSmodHandling = !Global.getSector().getMemoryWithoutUpdate().getBoolean("$exchangerefit_hasProgSMods");
                }
            }
            
            for (String mId : v.getHullMods()) {
                if (mId == null || mId.startsWith("aitweaks_") || mId.contains("aitweaks") || mId.equals("finish_beam_protocol") || mId.equals("search_destroy")) continue;
                if (v.getSMods() != null && v.getSMods().contains(mId) && enableSmodHandling) {
                    sb.append("S_HULLMOD:").append(mId).append("\n");
                } else { sb.append("HULLMOD:").append(mId).append("\n"); }
            }
            for (String sId : v.getFittedWeaponSlots()) {
                String wId = v.getWeaponId(sId);
                if (wId != null) sb.append("ARME_SLOT:").append(sId).append("\n").append("ARME_ID:").append(wId).append("\n");
            }
            for (String wingId : v.getFittedWings()) {
                if (wingId != null) sb.append("FIGHTER_WING:").append(wingId).append("\n");
            }
            
            if (v.getTags() != null) {
                List tags = new ArrayList(v.getTags());
                for (int i = 0; i < tags.size(); i++) {
                    String tag = (String) tags.get(i);
                    if (tag == null) continue;
                    if (tag.startsWith("agc_") || tag.contains("Gunnery") || tag.equals("agc_enabled") || tag.equals("AGC_TAG") || tag.startsWith("aitweaks_") || tag.contains("AITweaks")) {
                        sb.append("AGC_TAG:").append(tag).append("\n");
                    } else if (!tag.startsWith("hull_") && !tag.equals("archetype")) {
                        sb.append("UPGRADE_TAG:").append(tag).append("\n");
                    }
                }
            }
            
            String fileName = X1MecaniqueIO.assainirNomFichier(v.getHullSpec().getHullId(), slot);
            Global.getSettings().writeTextFileToCommon(fileName, sb.toString());
            textPanel.addParagraph("[OK] Template saved to universal slot " + slot + "!", Color.CYAN);
        } catch (Exception e) { textPanel.addParagraph("[ERROR] Export failed: " + e.getMessage(), Color.RED); }
    }
}
