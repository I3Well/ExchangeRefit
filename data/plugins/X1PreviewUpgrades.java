package data.plugins;

import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;

public class X1PreviewUpgrades {

    public static void afficherSectionSModsEtUpgrades(FleetMemberAPI member, ArrayList rawSModIds, ArrayList listUpgrades, TextPanelAPI textPanel) {
        try {
            if (!rawSModIds.isEmpty()) {
                X1Sorter.trierHullmodsParOp(rawSModIds, member);
                ArrayList listSMods = new ArrayList();
                for (int i = 0; i < rawSModIds.size(); i++) {
                    String details = X1SpecExtractor.extraireHullmod((String) rawSModIds.get(i), member, true);
                    if (details != null) listSMods.add(details);
                }
                if (!listSMods.isEmpty()) {
                    X1PreviewUtil.imprimerSectionRécapAtelier("Built-in Story Modifications (S-Mods):", (String[]) listSMods.toArray(new String[listSMods.size()]), textPanel, new java.awt.Color(255, 215, 0));
                    textPanel.addParagraph(""); 
                }
            }

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
        } catch (Exception e) {}
    }
}
