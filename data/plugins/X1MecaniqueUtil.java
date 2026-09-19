package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class X1MecaniqueUtil {

    public static boolean gabaritExiste(FleetMemberAPI member, int slot) {
        if (member == null) return false;
        String fileName = X1MecaniqueIO.assainirNomFichier(member.getHullSpec().getHullId(), slot);
        return Global.getSettings().fileExistsInCommon(fileName);
    }

    public static void appliquerGabaritSelectionne(X1TerminalMenu menu) {
        if (menu == null || menu.vaisseauSelectionne == null) return;
        
        try {
            X1MecaniqueIO.importerGabaritFileMenu(menu, false);
        } catch (Exception e) {
            if (menu.textPanel != null) {
                menu.textPanel.addParagraph("Error applying assembly parameters: " + e.getMessage(), java.awt.Color.RED);
            }
        }
    }

    public static void executerSauvegardeLayout(X1TerminalMenu menu) {
        if (menu == null || menu.vaisseauSelectionne == null) return;
        X1MecaniqueIO.exporterGabaritFile(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel);
    }

    public static void effacerFichierGabarit(FleetMemberAPI member, int slot, com.fs.starfarer.api.campaign.TextPanelAPI panel) {
        if (member == null || panel == null) return;
        String fileName = X1MecaniqueIO.assainirNomFichier(member.getHullSpec().getHullId(), slot);
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                Global.getSettings().deleteTextFileFromCommon(fileName);
                panel.addParagraph("=> Archive configuration wiped from slot " + slot, java.awt.Color.YELLOW);
            } else {
                panel.addParagraph("No physical blueprint detected on slot " + slot, java.awt.Color.GRAY);
            }
        } catch (Exception e) {
            panel.addParagraph("Wipe failure on slot " + slot + ": " + e.getMessage(), java.awt.Color.RED);
        }
    }
}
