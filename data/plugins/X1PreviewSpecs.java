package data.plugins;

import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;

public class X1PreviewSpecs {

    public static void afficherSectionArmesEtHullmods(FleetMemberAPI member, ArrayList rawWeaponIds, ArrayList rawHullmodIds, X1TerminalMenu menu, TextPanelAPI textPanel) {
        try {
            if (menu.filterWeapons) {
                X1Sorter.trierArmesParOp(rawWeaponIds);
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

                textPanel.addParagraph("Mounted Weaponry Specs:", java.awt.Color.CYAN);
                X1PreviewUtil.imprimerArmesColoreesAtelier("  Large:", listLargeIds, textPanel);
                X1PreviewUtil.imprimerArmesColoreesAtelier("  Medium:", listMediumIds, textPanel);
                X1PreviewUtil.imprimerArmesColoreesAtelier("  Small:", listSmallIds, textPanel);
                textPanel.addParagraph(""); 
            }

            if (menu.filterHullmods && !rawHullmodIds.isEmpty()) {
                X1Sorter.trierHullmodsParOp(rawHullmodIds, member);
                ArrayList listHullmods = new ArrayList();
                for (int i = 0; i < rawHullmodIds.size(); i++) {
                    String details = X1SpecExtractor.extraireHullmod((String) rawHullmodIds.get(i), member, false);
                    if (details != null) listHullmods.add(details);
                }
                if (!listHullmods.isEmpty()) {
                    X1PreviewUtil.imprimerSectionRécapAtelier("Standard Hull Modifications (Sorted by OP):", (String[]) listHullmods.toArray(new String[listHullmods.size()]), textPanel, java.awt.Color.WHITE);
                    textPanel.addParagraph(""); 
                }
            }
        } catch (Exception e) {}
    }
}
