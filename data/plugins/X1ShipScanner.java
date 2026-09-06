package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.characters.PersonAPI;

public class X1ShipScanner {

    public static void afficherDiagnosticVaisseauActuel(FleetMemberAPI member, TextPanelAPI textPanel) {
        try {
            ShipVariantAPI variant = member.getVariant();
            if (variant == null) return;

            textPanel.addParagraph("=== HULL SCANNER DIAGNOSTIC : " + member.getShipName().toUpperCase() + " ===", java.awt.Color.GREEN);
            
            int maxSpeed = (int) member.getStats().getMaxSpeed().getModifiedValue();
            int dpCost = (int) member.getUnmodifiedDeploymentPointsCost();
            
            int hullPoints = (int) member.getHullSpec().getHitpoints();
            int armorPoints = (int) member.getHullSpec().getArmorRating();
            
            com.fs.starfarer.api.characters.MutableCharacterStatsAPI charStats = null;
            if (Global.getSector() != null && Global.getSector().getPlayerPerson() != null) {
                charStats = Global.getSector().getPlayerPerson().getStats();
            }
            int opLeft = variant.getUnusedOP(charStats);
            int opTotal = variant.getHullSpec().getOrdnancePoints(charStats);

            String shieldType = "NONE";
            if (variant.getHullSpec().getShieldType() != null) {
                shieldType = variant.getHullSpec().getShieldType().name();
            }

            textPanel.addParagraph(" * Deployment: " + dpCost + " DP | Top Speed: " + maxSpeed + " su | Shield Type: " + shieldType, java.awt.Color.WHITE);
            textPanel.addParagraph(" * Structural Hull (Base): " + hullPoints + " | Armor Rating (Base): " + armorPoints, java.awt.Color.WHITE);
            textPanel.addParagraph(" * Capacitors: " + variant.getNumFluxCapacitors() + " | Vents: " + variant.getNumFluxVents() + " | Remaining OP: " + opLeft + " / " + opTotal + " OP", java.awt.Color.WHITE);
            
            boolean hasGunnery = variant.getTags().contains("agc_enabled") || variant.getTags().contains("AGC_TAG");
            textPanel.addParagraph(" * Advanced Gunnery Control (AGC): " + (hasGunnery ? "[ACTIVE]" : "[NOT INSTALLED]"), hasGunnery ? java.awt.Color.GREEN : java.awt.Color.GRAY);
            
            textPanel.addParagraph("");

            PersonAPI captain = member.getCaptain();
            if (captain != null && !captain.isDefault()) {
                String nomCap = captain.getName().getFullName();
                int lvl = captain.getStats().getLevel();
                String personality = captain.getPersonalityAPI().getId().toUpperCase();
                
                if (captain.isAICore()) {
                    textPanel.addParagraph(" * Assigned Commander: [INTELLIGENCE ARTIFICIELLE] " + nomCap + " [Personality: " + personality + "]", java.awt.Color.CYAN);
                } else {
                    textPanel.addParagraph(" * Assigned Commander: " + nomCap + " [Lvl " + lvl + " | Personality: " + personality + "]", java.awt.Color.CYAN);
                }
                
                // RESTAURATION SÉCURISÉE SANS AUCUNE RÉFLEXION (ZÉRO GETMETHOD / INVOKE)
                String[] combatSkills = new String[]{
                    "ballistic_mastery", "energy_weapon_mastery", "ordnance_expert", 
                    "damage_control", "combat_endurance", "impact_mitigation", 
                    "field_modulation", "point_defense", "target_analysis", 
                    "systems_expertise", "gunnery_implants", "helmsmanship", 
                    "missile_specialization", "phase_corps", "wolfpack_tactics"
                };
                
                java.util.ArrayList tempSkills = new java.util.ArrayList();
                
                for (int i = 0; i < combatSkills.length; i++) {
                    String skillId = combatSkills[i];
                    int levelVal = (int) captain.getStats().getSkillLevel(skillId);
                    
                    if (levelVal > 0) {
                        String elStr = (levelVal >= 2) ? " [ELITE]" : " (Standard)";
                        tempSkills.add("   • " + X1SkillTraducteur.getNomLisible(skillId) + elStr);
                    }
                }
                
                for (int i = 0; i < tempSkills.size(); i++) {
                    textPanel.addParagraph((String) tempSkills.get(i), java.awt.Color.LIGHT_GRAY);
                }
            } else {
                textPanel.addParagraph(" * Assigned Commander: None / Unassigned", java.awt.Color.GRAY);
            }

            textPanel.addParagraph("");

            java.util.ArrayList weaponIds = new java.util.ArrayList();
            java.util.Collection slots = variant.getFittedWeaponSlots();
            java.util.Iterator slotIt = slots.iterator();
            while (slotIt.hasNext()) {
                String slotId = (String) slotIt.next();
                if (variant.getWeaponId(slotId) != null) {
                    String wId = variant.getWeaponId(slotId);
                    if (!weaponIds.contains(wId)) weaponIds.add(wId);
                }
            }
            X1Sorter.trierArmesParOp(weaponIds);

            java.util.ArrayList large = new java.util.ArrayList();
            java.util.ArrayList med = new java.util.ArrayList();
            java.util.ArrayList small = new java.util.ArrayList();
            for (int i = 0; i < weaponIds.size(); i++) {
                String wId = (String) weaponIds.get(i);
                String size = X1SpecExtractor.getArmeSize(wId);
                if (size.equals("LARGE")) large.add(wId);
                else if (size.equals("MEDIUM")) med.add(wId);
                else small.add(wId);
            }

            textPanel.addParagraph("Current Mounted Weaponry Specs:", java.awt.Color.CYAN);
            X1PreviewUtil.imprimerArmesColoreesAtelier("  Large:", large, textPanel);
            X1PreviewUtil.imprimerArmesColoreesAtelier("  Medium:", med, textPanel);
            X1PreviewUtil.imprimerArmesColoreesAtelier("  Small:", small, textPanel);

            textPanel.addParagraph("");

            java.util.ArrayList hmIds = new java.util.ArrayList();
            java.util.ArrayList smIds = new java.util.ArrayList();
            
            java.util.Collection installedMods = variant.getHullMods();
            java.util.Iterator modIt = installedMods.iterator();
            while (modIt.hasNext()) {
                String mId = (String) modIt.next();
                if (variant.getSMods().contains(mId)) {
                    smIds.add(mId);
                } else {
                    hmIds.add(mId);
                }
            }
            X1Sorter.trierHullmodsParOp(hmIds, member);
            X1Sorter.trierHullmodsParOp(smIds, member);

            java.util.ArrayList listHullmods = new java.util.ArrayList();
            for (int i = 0; i < hmIds.size(); i++) {
                String line = X1SpecExtractor.extraireHullmod((String) hmIds.get(i), member, false);
                if (line != null) listHullmods.add(line);
            }

            java.util.ArrayList listSMods = new java.util.ArrayList();
            for (int i = 0; i < smIds.size(); i++) {
                String line = X1SpecExtractor.extraireHullmod((String) smIds.get(i), member, true);
                if (line != null) {
                    line = line.replace("Base Cost:", "Downgrade Cost:");
                    listSMods.add(line);
                }
            }

            String[] hullmodsArr = (String[]) listHullmods.toArray(new String[listHullmods.size()]);
            String[] smodsArr = (String[]) listSMods.toArray(new String[listSMods.size()]);

            X1PreviewUtil.imprimerSectionRécapAtelier("Equipped Standard Hull Modifications:", hullmodsArr, textPanel, java.awt.Color.WHITE);
            textPanel.addParagraph("");
            X1PreviewUtil.imprimerSectionRécapAtelier("Built-in Story Modifications (S-Mods):", smodsArr, textPanel, new java.awt.Color(255, 215, 0));
            
            textPanel.addParagraph("");
            
            textPanel.addParagraph("Additional Refit Upgrades:", java.awt.Color.CYAN);
            boolean aDesUpgrades = false;
            if (variant.getTags() != null) {
                java.util.List tags = new java.util.ArrayList(variant.getTags());
                for (int i = 0; i < tags.size(); i++) {
                    String tag = (String) tags.get(i);
                    if (!tag.startsWith("hull_") && !tag.equals("archetype") && !tag.equals("agc_enabled") && !tag.equals("AGC_TAG")) {
                        textPanel.addParagraph(" - Upgrade Tag ID: " + tag, java.awt.Color.WHITE);
                        aDesUpgrades = true;
                    }
                }
            }
            if (!aDesUpgrades) {
                textPanel.addParagraph(" - None", java.awt.Color.GRAY);
            }
            
            textPanel.addParagraph("");
            textPanel.addParagraph("--------------------------------------------------", java.awt.Color.GRAY);
            textPanel.addParagraph("");

        } catch (Exception e) {
            textPanel.addParagraph("Error rendering live vehicle diagnostic scan: " + e.getMessage(), java.awt.Color.RED);
        }
    }
}
