package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;
import java.util.List;

public class X1TerminalRouteAiguillage {

    public static void aiguillerActionInitiale(String actionId, X1TerminalMenu menu) {
        if (menu == null || actionId == null) return;
        X1TerminalRouteFinAiguillage.aiguillerActionTerminal(actionId, menu);
    }

    public static void filtrerEtTrierFlotte(X1TerminalMenu menu) {
        if (menu == null) return;
        menu.flotteFiltree.clear();
        if (Global.getSector() == null || Global.getSector().getPlayerFleet() == null) return;
        
        List deLaFlotte = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (int i = 0; i < deLaFlotte.size(); i++) {
            FleetMemberAPI m = (FleetMemberAPI) deLaFlotte.get(i);
            if (m == null || m.isFighterWing()) continue;
            
            // FILTRE DE MODULE : Élimine les sections de stations orbitales amovibles
            if (m.getHullSpec() != null && m.getHullSpec().getHullId() != null) {
                String hId = m.getHullSpec().getHullId().toLowerCase();
                if (hId.contains("_module") || hId.contains("module_")) continue;
            }
            
            menu.flotteFiltree.add(m);
        }
        X1VaisseauTri.trierFlotte(menu.flotteFiltree, "NONE");
    }

    public static void aiguillerBoutonAction(String actionId, X1TerminalMenu menu) {
        if (menu == null || actionId == null) return;

        if (actionId.startsWith("SHIP_")) {
            try {
                int idx = Integer.parseInt(actionId.substring(5).trim());
                menu.vaisseauSelectionne = (FleetMemberAPI) menu.flotteFiltree.get(idx);
                menu.options.clearOptions();
                if (menu.textPanel != null) {
                    menu.textPanel.clear();
                    X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
                }
                X1TerminalMenusDisplay.afficherMenuSlots(menu);
            } catch (Exception e) {}
            return;
        }

        if (actionId.startsWith("SLOT_")) {
            try {
                menu.slotChoisi = Integer.parseInt(actionId.substring(5).trim());
                menu.options.clearOptions();
                X1TerminalMenusDisplay.afficherMenuActions(menu);
            } catch (Exception e) {}
            return;
        }

        if (actionId.equals("NEXT_PAGE_SLOTS")) {
            menu.pageSlotsCourante = 2;
            menu.options.clearOptions();
            X1TerminalMenusDisplay.afficherMenuSlots(menu);
            return;
        }

        if (actionId.equals("PREV_PAGE_SLOTS")) {
            menu.pageSlotsCourante = 1;
            menu.options.clearOptions();
            X1TerminalMenusDisplay.afficherMenuSlots(menu);
            return;
        }

        if (actionId.equals("BACK_SHIPS")) {
            menu.vaisseauSelectionne = null;
            menu.slotChoisi = -1;
            menu.options.clearOptions();
            if (menu.textPanel != null) {
                menu.textPanel.clear();
            }
            X1TerminalMenusDisplay.afficherMenuVaisseaux(menu);
            return;
        }

        if (actionId.equals("BACK_SLOTS")) {
            menu.slotChoisi = -1;
            menu.options.clearOptions();
            X1TerminalMenusDisplay.afficherMenuSlots(menu);
            return;
        }

        if (actionId.equals("BACK_ACTIONS")) {
            menu.options.clearOptions();
            if (menu.textPanel != null) {
                menu.textPanel.clear();
            }
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }

        if (actionId.startsWith("TOGGLE_")) {
            if (actionId.equals("TOGGLE_WEAPONS")) menu.filterWeapons = !menu.filterWeapons;
            if (actionId.equals("TOGGLE_HULLMODS")) menu.filterHullmods = !menu.filterHullmods;
            if (actionId.equals("TOGGLE_SMODS")) menu.filterSMods = !menu.filterSMods;
            if (actionId.equals("TOGGLE_TAGS")) menu.filterTags = !menu.filterTags;
            if (actionId.equals("TOGGLE_FIGHTERS")) menu.filterFighters = !menu.filterFighters;
            if (actionId.equals("TOGGLE_GROUPS")) menu.filterGroups = !menu.filterGroups;
            if (actionId.equals("TOGGLE_OFFICER")) menu.filterOfficer = !menu.filterOfficer;
            if (actionId.equals("TOGGLE_UPGRADES")) X1TerminalRouteFinAiguillage.filterUpgradesStat = !X1TerminalRouteFinAiguillage.filterUpgradesStat;
            menu.options.clearOptions();
            X1TerminalMenusDisplay.afficherMenuFiltres(menu);
            return;
        }

        if (actionId.equals("ACTION_COPY")) {
            X1MecaniqueUtil.executerSauvegardeLayout(menu);
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }

        if (actionId.equals("ACTION_STRIP_SMODS_CONFIRM")) {
            X1TerminalRouteFinAiguillage.aiguillerActionTerminal("ACTION_STRIP_SMODS_CONFIRM_EXECUTE", menu);
            return;
        }

        if (actionId.equals("ACTION_PASTE_DEFAULT")) {
            X1TerminalRouteFinAiguillage.aiguillerActionTerminal("CONFIRM_PASTE_DEFAULT", menu);
            return;
        }

        if (actionId.equals("ACTION_GOTO_FILTERS")) {
            menu.options.clearOptions();
            X1TerminalMenusDisplay.afficherMenuFiltres(menu);
            return;
        }

        if (actionId.equals("EXECUTE_FILTERED_PASTE")) {
            X1TerminalRouteFinAiguillage.aiguillerActionTerminal("CONFIRM_PASTE_FILTERED", menu);
            return;
        }

        if (actionId.equals("ACTION_DELETE")) {
            X1MecaniqueUtil.effacerFichierGabarit(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel);
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }
    }
}
