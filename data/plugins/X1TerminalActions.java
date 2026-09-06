package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;

public class X1TerminalActions {

    public static void initialiserMenu(X1TerminalMenu menu) {
        menu.textPanel.clear(); 
        menu.textPanel.addParagraph("=== EXCHANGEREFIT SHIPYARD TERMINAL ===");
        chargerFlotteBrute(menu); 
        X1TerminalMenusDisplay.afficherMenuVaisseaux(menu);
    }

    public static void chargerFlotteBrute(X1TerminalMenu menu) {
        menu.flotteFiltree.clear();
        if (Global.getSector() == null) return;
        if (Global.getSector().getPlayerFleet() == null) return;
        
        List rawList = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (int i = 0; i < rawList.size(); i++) {
            FleetMemberAPI m = (FleetMemberAPI) rawList.get(i);
            if (m.isFighterWing()) continue;
            if (m.getHullId().contains("module")) continue;
            if (m.getHullId().equals("flare")) continue;
            
            menu.flotteFiltree.add(m);
        }
    }

    public static void traiterOptionSelectionnee(String opt, X1TerminalMenu menu) {
        if (opt.startsWith("SHIP_")) { 
            menu.vaisseauSelectionne = (FleetMemberAPI) menu.flotteFiltree.get(Integer.parseInt(opt.substring(5))); 
            menu.pageSlotsCourante = 1; 
            X1TerminalMenusDisplay.afficherMenuSlots(menu); 
            return; 
        }
        if (opt.startsWith("SLOT_")) { 
            menu.slotChoisi = Integer.parseInt(opt.substring(5)); 
            X1TerminalMenusDisplay.afficherMenuActions(menu); 
            return; 
        }
        X1TerminalMenusDisplay.executerOptionRoute(opt, menu);
    }
}
