package data.plugins;

import com.fs.starfarer.api.campaign.OptionPanelAPI;

public class X1TerminalActions {

    public static void initialiserMenu(X1TerminalMenu menu) {
        if (menu == null) return;
        if (menu.textPanel != null) {
            menu.textPanel.clear();
        }
        // DEBLOCAGE : On force le chargement et le filtrage des vaisseaux pour la Page 1
        X1TerminalRouteAiguillage.filtrerEtTrierFlotte(menu);
        X1TerminalMenusDisplay.afficherMenuVaisseaux(menu);
    }

    public static void traiterOptionSelectionnee(String optionId, X1TerminalMenu menu) {
        if (menu == null || optionId == null) return;

        if (optionId.equals("LEAVE")) {
            if (menu.dialog != null && menu.originalPlugin != null) {
                menu.dialog.setPlugin(menu.originalPlugin);
                menu.originalPlugin.init(menu.dialog);
            }
            return;
        }
        X1TerminalMenusDisplay.executerOptionRoute(optionId, menu);
    }
}
