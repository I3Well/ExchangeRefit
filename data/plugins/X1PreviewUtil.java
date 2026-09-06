package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;

public class X1PreviewUtil {

    public static void imprimerArmesColoreesAtelier(String title, ArrayList ids, TextPanelAPI textPanel) {
        X1PreviewPrinter.imprimerArmesColoreesAtelier(title, ids, textPanel);
    }

    public static void imprimerSectionRécapAtelier(String title, String[] items, TextPanelAPI textPanel, java.awt.Color txtColor) {
        X1PreviewPrinter.imprimerSectionRécapAtelier(title, items, textPanel, txtColor);
    }

    public static void générerEtAfficherRécapitulatif(FleetMemberAPI member, int slotChoisi, TextPanelAPI textPanel, X1TerminalMenu menu) {
        try {
            String id = member.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            String raw = Global.getSettings().readTextFileFromCommon("ExchangeRefit_" + id + "_Slot" + slotChoisi + ".txt");
            if (raw == null || raw.isEmpty()) return;

            String[] lines = raw.split("\n");
            int caps = 0, vents = 0;
            boolean hasGunnery = false;
            String creatorCommander = "Unknown", realTimeIRL = "Unknown Date";
            int creatorCycle = 0;
            String offName = "", offPers = "steady";
            int offLevel = 0;
            
            ArrayList offSkills = new ArrayList();
            ArrayList rawWeaponIds = new ArrayList();
            ArrayList rawHullmodIds = new ArrayList();
            ArrayList rawSModIds = new ArrayList();
            ArrayList listUpgrades = new ArrayList();

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line == null || line.isEmpty()) continue;
                String trimmed = line.trim();

                if (trimmed.startsWith("METADATA_COMMANDER:")) creatorCommander = trimmed.substring(19).trim();
                if (trimmed.startsWith("METADATA_CYCLE:")) creatorCycle = Integer.parseInt(trimmed.substring(15).trim());
                if (trimmed.startsWith("METADATA_REALTIME:")) realTimeIRL = trimmed.substring(18).trim();
                if (trimmed.startsWith("OFFICER_NAME:")) offName = trimmed.substring(13).trim();
                if (trimmed.startsWith("OFFICER_LEVEL:")) offLevel = Integer.parseInt(trimmed.substring(14).trim());
                if (trimmed.startsWith("OFFICER_PERSONALITY:")) offPers = trimmed.substring(20).trim();
                if (trimmed.startsWith("FLUX_CAPACITORS:")) caps = Integer.parseInt(trimmed.substring(16).trim());
                if (trimmed.startsWith("FLUX_VENTS:")) vents = Integer.parseInt(trimmed.substring(11).trim());
                if (trimmed.startsWith("AGC_TAG:")) hasGunnery = true;

                if (trimmed.startsWith("UPGRADE_TAG:")) {
                    String upTag = trimmed.substring(12).trim();
                    if (!listUpgrades.contains(upTag)) listUpgrades.add(" - Upgrade Tag ID: " + upTag);
                }

                if (trimmed.startsWith("OFFICER_SKILL:")) {
                    String skillRaw = trimmed.substring(14).trim();
                    String skillId = skillRaw;
                    String statut = " (Standard)";
                    if (skillRaw.contains("-")) {
                        skillId = skillRaw.substring(0, skillRaw.indexOf("-"));
                        if (skillRaw.substring(skillRaw.indexOf("-") + 1).equals("2")) statut = " [ELITE]";
                    }
                    offSkills.add("   • " + X1SkillTraducteur.getNomLisible(skillId) + statut);
                }

                if (trimmed.startsWith("ARME_ID:")) {
                    String wId = trimmed.substring(8).trim();
                    if (!rawWeaponIds.contains(wId)) rawWeaponIds.add(wId);
                }
                if (trimmed.startsWith("HULLMOD:")) {
                    String mId = trimmed.substring(8).trim();
                    if (!rawHullmodIds.contains(mId)) rawHullmodIds.add(mId);
                }
                if (trimmed.startsWith("S_HULLMOD:")) {
                    String smId = trimmed.substring(10).trim();
                    if (!rawSModIds.contains(smId)) rawSModIds.add(smId);
                }
            }

            X1Sorter.trierArmesParOp(rawWeaponIds);
            X1Sorter.trierHullmodsParOp(rawHullmodIds, member);
            X1Sorter.trierHullmodsParOp(rawSModIds, member);

            ArrayList listLargeIds = new ArrayList();
            ArrayList listMediumIds = new ArrayList();
            ArrayList listSmallIds = new ArrayList();

            for (int i = 0; i < rawWeaponIds.size(); i++) {
                String wId = (String) rawWeaponIds.get(i);
                String size = X1SpecExtractor.getArmeSize(wId);
                if (size.equals("LARGE")) listLargeIds.add(wId);
                else if (size.equals("MEDIUM")) listMediumIds.add(wId);
                else listSmallIds.add(wId);
            }

            ArrayList listHullmods = new ArrayList();
            for (int i = 0; i < rawHullmodIds.size(); i++) {
                String details = X1SpecExtractor.extraireHullmod((String) rawHullmodIds.get(i), member, false);
                if (details != null) listHullmods.add(details);
            }

            ArrayList listSMods = new ArrayList();
            for (int i = 0; i < rawSModIds.size(); i++) {
                String details = X1SpecExtractor.extraireHullmod((String) rawSModIds.get(i), member, true);
                if (details != null) listSMods.add(details);
            }

            textPanel.addParagraph("--- TEMPLATE ARCHIVE CONTENT (SLOT " + slotChoisi + ") ---", java.awt.Color.GREEN);
            textPanel.addParagraph(" * Creator: " + creatorCommander + " (Cycle " + creatorCycle + ")", java.awt.Color.YELLOW);
            textPanel.addParagraph(" * Saved IRL on: " + realTimeIRL, new java.awt.Color(135, 206, 250));
            
            if (menu.filterHullmods || menu.filterTags) {
                String textFlux = " * Capacitors: " + caps + " | Vents: " + vents;
                if (menu.filterTags && hasGunnery) textFlux += " | [Advanced Gunnery Control Profile Included]";
                textPanel.addParagraph(textFlux, java.awt.Color.WHITE);
            }
            
            textPanel.addParagraph(""); 

            if (menu.filterOfficer) {
                if (!offName.isEmpty()) {
                    textPanel.addParagraph(" * Attached Officer: " + offName + " [Lvl " + offLevel + " | Personality: " + offPers.toUpperCase() + "]", java.awt.Color.CYAN);
                    textPanel.addParagraph("   Officer Talents / Skills analyzed:", java.awt.Color.LIGHT_GRAY);
                    for (int i = 0; i < offSkills.size(); i++) textPanel.addParagraph((String) offSkills.get(i), java.awt.Color.LIGHT_GRAY);
                } else {
                    textPanel.addParagraph(" * Attached Officer: None / Unassigned", java.awt.Color.GRAY);
                }
                textPanel.addParagraph(""); 
            }

            if (menu.filterWeapons) {
                textPanel.addParagraph("Mounted Weaponry Specs:", java.awt.Color.CYAN);
                X1PreviewPrinter.imprimerArmesColoreesAtelier("  Large:", listLargeIds, textPanel);
                X1PreviewPrinter.imprimerArmesColoreesAtelier("  Medium:", listMediumIds, textPanel);
                X1PreviewPrinter.imprimerArmesColoreesAtelier("  Small:", listSmallIds, textPanel);
                textPanel.addParagraph(""); 
            }

            if (menu.filterHullmods && !listHullmods.isEmpty()) {
                X1PreviewPrinter.imprimerSectionRécapAtelier("Standard Hull Modifications (Sorted by OP):", (String[]) listHullmods.toArray(new String[listHullmods.size()]), textPanel, java.awt.Color.WHITE);
                textPanel.addParagraph(""); 
            }

            if (menu.filterSMods && !listSMods.isEmpty()) {
                X1PreviewPrinter.imprimerSectionRécapAtelier("Built-in Story Modifications (S-Mods):", (String[]) listSMods.toArray(new String[listSMods.size()]), textPanel, new java.awt.Color(255, 215, 0));
                textPanel.addParagraph(""); 
            }

            // CORRECTION DE RÉFÉRENCE DE LA VARIABLE MANQUANTE PAR NOTRE VARIABLE STATIQUE GLOBALE
            if (X1TerminalRouteFinAiguillage.filterUpgradesStat) {
                textPanel.addParagraph("Additional Refit Upgrades:", java.awt.Color.CYAN);
                if (!listUpgrades.isEmpty()) {
                    for (int i = 0; i < listUpgrades.size(); i++) {
                        textPanel.addParagraph((String) listUpgrades.get(i), java.awt.Color.WHITE);
                    }
                } else {
                    textPanel.addParagraph(" - None", java.awt.Color.GRAY);
                }
                textPanel.addParagraph(""); 
            }

        } catch (Exception e) {
            textPanel.addParagraph("Error reading slot preview: " + e.getMessage(), java.awt.Color.RED);
        }
    }
}
