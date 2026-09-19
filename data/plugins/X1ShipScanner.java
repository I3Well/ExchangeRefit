package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.awt.Color;
import java.util.ArrayList;

public class X1ShipScanner {

    private static final String[] COMBAT_SKILLS = new String[] {
        "helmsmanship", "combat_endurance", "impact_mitigation", "damage_control",
        "field_modulation", "point_defense", "target_analysis", "ballistic_mastery",
        "systems_expertise", "missile_specialization", "gunnery_implants", "energy_weapon_mastery",
        "ordnance_expert", "polarized_armor", "neural_link", "cybernetic_augmentation"
    };

    private static String getNomLisibleLocal(String id) {
        if (id == null) return "Unknown";
        if (id.equals("helmsmanship")) return "Helmsmanship";
        if (id.equals("combat_endurance")) return "Combat Endurance";
        if (id.equals("impact_mitigation")) return "Impact Mitigation";
        if (id.equals("damage_control")) return "Damage Control";
        if (id.equals("field_modulation")) return "Field Modulation";
        if (id.equals("point_defense")) return "Point Defense";
        if (id.equals("target_analysis")) return "Target Analysis";
        if (id.equals("ballistic_mastery")) return "Ballistic Mastery";
        if (id.equals("systems_expertise")) return "Systems Expertise";
        if (id.equals("missile_specialization")) return "Missile Specialization";
        if (id.equals("gunnery_implants")) return "Gunnery Implants";
        if (id.equals("energy_weapon_mastery")) return "Energy Weapon Mastery";
        if (id.equals("ordnance_expert")) return "Ordnance Expert";
        if (id.equals("polarized_armor")) return "Polarized Armor";
        if (id.equals("neural_link")) return "Neural Link";
        if (id.equals("cybernetic_augmentation")) return "Cybernetic Augmentation";
        return id;
    }

    public static void afficherDiagnosticVaisseauActuel(FleetMemberAPI member, TextPanelAPI textPanel) {
        if (member == null || textPanel == null) return;

        textPanel.addParagraph(X1Localisation.get("DIAG_TITLE"), Color.CYAN);
        
        String hullName = member.getHullSpec().getHullName();
        String designation = member.getHullSpec().getDesignation();
        
        int baseArmor = (int) member.getHullSpec().getArmorRating();
        int maxVents = member.getVariant().getNumFluxVents();
        int maxCaps = member.getVariant().getNumFluxCapacitors();
        
        com.fs.starfarer.api.characters.MutableCharacterStatsAPI charStats = null;
        if (Global.getSector() != null && Global.getSector().getPlayerPerson() != null) {
            charStats = Global.getSector().getPlayerPerson().getStats();
        }
        int totalOP = member.getVariant().getHullSpec().getOrdnancePoints(charStats);

        textPanel.addParagraph(X1Localisation.get("HULL_SEL") + member.getShipName() + " [" + hullName + " Class " + designation + "]", Color.WHITE);
        textPanel.addParagraph(X1Localisation.get("HULL_METRICS") + baseArmor + " | Vents: " + maxVents + " | Capacitors: " + maxCaps + " | Total OP: " + totalOP, Color.LIGHT_GRAY);
        textPanel.addParagraph("");

        if (member.getCaptain() != null) {
            boolean isAI = member.getCaptain().isAICore();
            textPanel.addParagraph(X1Localisation.get(isAI ? "OFFICER_AI" : "OFFICER_STANDARD") + " " + member.getCaptain().getNameString() + " (Level " + member.getCaptain().getStats().getLevel() + ")", Color.YELLOW);
            
            com.fs.starfarer.api.characters.MutableCharacterStatsAPI stats = member.getCaptain().getStats();
            for (int i = 0; i < COMBAT_SKILLS.length; i++) {
                String skillId = COMBAT_SKILLS[i];
                int level = (int) stats.getSkillLevel(skillId);
                if (level <= 0) continue;
                
                String skillName = getNomLisibleLocal(skillId);
                textPanel.addParagraph(level > 1 ? "   • " + skillName + " [ELITE]" : "   • " + skillName, level > 1 ? Color.GREEN : Color.WHITE);
            }
        } else {
            textPanel.addParagraph(X1Localisation.get("VACANT"), Color.GRAY);
        }

        textPanel.addParagraph("");

        if (member.getVariant() != null) {
            X1WeaponScanner.scannerEtAfficherArmes(member, textPanel);

            textPanel.addParagraph("");
            ArrayList hmIds = new ArrayList();
            ArrayList smIds = new ArrayList();
            java.util.Collection mods = member.getVariant().getHullMods();
            java.util.Iterator modIt = mods.iterator();
            while (modIt.hasNext()) {
                String mId = (String) modIt.next();
                if (member.getVariant().getSMods().contains(mId)) smIds.add(mId);
                else hmIds.add(mId);
            }
            X1Sorter.trierHullmodsParOp(hmIds, member);
            X1Sorter.trierHullmodsParOp(smIds, member);

            textPanel.addParagraph(X1Localisation.get("HULLMODS_LAYOUT"), Color.WHITE);
            for (int i = 0; i < hmIds.size(); i++) {
                String line = X1SpecExtractor.extraireHullmod((String) hmIds.get(i), member, false);
                if (line != null) textPanel.addParagraph(line, Color.WHITE);
            }
            textPanel.addParagraph("");
            textPanel.addParagraph(X1Localisation.get("SMODS_LAYOUT"), new Color(255, 215, 0));
            for (int i = 0; i < smIds.size(); i++) {
                String line = X1SpecExtractor.extraireHullmod((String) smIds.get(i), member, true);
                if (line != null) textPanel.addParagraph(line.replace("Base Cost:", "Downgrade Cost:"), new Color(255, 215, 0));
            }
        }
        textPanel.addParagraph("");
    }
}
