package data.plugins;

public class X1TerminalRouteFinAiguillage {

    public static boolean filterUpgradesStat = true;

    public static void aiguillerActionTerminal(String actionId, X1TerminalMenu menu) {
        if (menu == null || actionId == null) return;

        if (actionId.equals("ACTION_STRIP_SMODS_CONFIRM_EXECUTE")) {
            X1SModStripper.nettoyerVaisseauSMods(menu.vaisseauSelectionne, menu.textPanel, true);
            menu.textPanel.clear();
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }

        if (actionId.equals("CONFIRM_PASTE_DEFAULT")) {
            try {
                X1MecaniqueIO.importerGabaritFileMenu(menu, true);
            } catch (Exception e) {
                menu.textPanel.addParagraph("Execution failure: " + e.getMessage(), java.awt.Color.RED);
            }
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }

        if (actionId.equals("CONFIRM_PASTE_FILTERED")) {
            try {
                X1MecaniqueIO.importerGabaritFileMenu(menu, false);
            } catch (Exception e) {
                menu.textPanel.addParagraph("Filtered execution failure: " + e.getMessage(), java.awt.Color.RED);
            }
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }
    }
}
