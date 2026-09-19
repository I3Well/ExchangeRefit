package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.awt.Color;
import java.util.List;

public class X1FleetMenuDisplay {

    public static void afficherMenuActionsVaisseau(X1TerminalMenu menu) {
        if (menu == null || menu.options == null) return;
        menu.options.clearOptions();

        // ÉCRAN D'ACCUEIL : SÉLECTION DU VAISSEAU DE FLOTTE
        if (menu.vaisseauSelectionne == null) {
            menu.textPanel.addParagraph("=== EXCHANGE REFIT : SELECT TARGET VESSEL ===", Color.CYAN);
            menu.textPanel.addParagraph("Choose a ship from your fleet list below to open its assembly terminal:", Color.WHITE);
            menu.textPanel.addParagraph("");

            if (Global.getSector() != null && Global.getSector().getPlayerFleet() != null) {
                List flotteJoueur = Global.getSector().getPlayerFleet().getFleetData().getMembersInPriorityOrder();
                for (int i = 0; i < flotteJoueur.size(); i++) {
                    FleetMemberAPI ship = (FleetMemberAPI) flotteJoueur.get(i);
                    menu.options.addOption("   • " + ship.getShipName() + " (" + ship.getHullSpec().getHullName() + ")", "FLEET_SHIP_" + i);
                }
            }
            menu.options.addOption("Exit Shipyard Terminal [Esc]", "MENU_EXIT_TERMINAL");
            menu.options.setShortcut("MENU_EXIT_TERMINAL", 1, false, false, false, true);
            return;
        }

        // ÉCRAN SECONDAIRE : LE VAISSEAU EST SÉLECTIONNÉ -> ON AFFICHE SES OUTILS DÉDIÉS
        X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
        
        menu.options.addOption("1. Save setup to a Blueprint Slot", "MENU_GOTO_SAVE");
        menu.options.addOption("2. Load and Apply a Blueprint layout (Paste)", "MENU_GOTO_LOAD");
        menu.options.addOption("3. Strip Story Modifications (S-Mods Tool)", "MENU_GOTO_STRIP");
        
        // Option pour revenir en arrière et changer de vaisseau
        menu.options.addOption("4. [Back] Choose another ship from Fleet", "ACTION_RESET_TARGET_SHIP");
        menu.options.addOption("Exit Shipyard Terminal [Esc]", "MENU_EXIT_TERMINAL");
        menu.options.setShortcut("MENU_EXIT_TERMINAL", 1, false, false, false, true);
    }
}
