package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.util.List;

public class X1TerminalMenusDisplay {

    public static void afficherMenuVaisseaux(X1TerminalMenu menu) {
        menu.options.clearOptions();
        boolean fr = X1Localisation.estEnFrancais();
        menu.textPanel.addParagraph(fr ? "Sélectionnez un vaisseau de la flotte pour gérer les gabarits :" : "Select a fleet hull to manage layouts:");
        
        for (int i = 0; i < menu.flotteFiltree.size(); i++) {
            FleetMemberAPI m = (FleetMemberAPI) menu.flotteFiltree.get(i);
            String label = m.getShipName() + " (" + m.getHullSpec().getHullName() + ")";
            if (m.isFlagship()) label += fr ? " [VAISSEAU AMIRAL]" : " [FLAGSHIP]";
            menu.options.addOption(label, "SHIP_" + i);
        }
        menu.options.addOption(fr ? "<- Quitter le Terminal [Esc]" : "<- Leave Terminal [Esc]", "LEAVE");
    }

    public static void afficherMenuSlots(X1TerminalMenu menu) {
        menu.options.clearOptions();
        if (menu.vaisseauSelectionne != null) {
            X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
        }
        
        boolean fr = X1Localisation.estEnFrancais();
        String prompt = fr ? "Sélectionnez un emplacement d'archive pour [" : "Select an Archive Slot for [";
        menu.textPanel.addParagraph(prompt + menu.vaisseauSelectionne.getShipName() + "] - Page " + menu.pageSlotsCourante + "/2:");
        
        int start = menu.pageSlotsCourante == 2 ? 8 : 1;
        int end = menu.pageSlotsCourante == 2 ? 13 : 7;
        
        for (int i = start; i <= end; i++) {
            String label = fr ? "Emplacement " + i + " (Vide)" : "Slot " + i + " (Empty)";
            String idSanitized = menu.vaisseauSelectionne.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
            String fileName = "ExchangeRefit_" + idSanitized + "_Slot" + i + ".txt";
            
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                label = fr ? "Emplacement " + i + " (Archivé)" : "Slot " + i + " (Archived)";
                try {
                    String raw = Global.getSettings().readTextFileFromCommon(fileName);
                    String[] lines = raw.split("\n");
                    String amiral = "Unknown", date = "Unknown";
                    int cycle = 0;
                    for (int j = 0; j < lines.length; j++) {
                        String l = lines[j].trim();
                        if (l.startsWith("METADATA_COMMANDER:")) amiral = l.substring(19).trim();
                        if (l.startsWith("METADATA_CYCLE:")) cycle = Integer.parseInt(l.substring(15).trim());
                        if (l.startsWith("METADATA_REALTIME:")) {
                            date = l.substring(18).trim();
                            if (date.contains(" ")) date = date.substring(0, date.indexOf(" "));
                        }
                    }
                    label = fr ? "Emplacement " + i + " [Cycle " + cycle + " - " + amiral + " | " + date + "]" : "Slot " + i + " [Cycle " + cycle + " - " + amiral + " | " + date + "]";
                } catch(Exception e){}
            }
            menu.options.addOption(label, "SLOT_" + i);
        }
        
        String pageLabel = menu.pageSlotsCourante == 1 ? (fr ? "Page Suivante (Slots 8 - 13) ->" : "Next Page (Slots 8 - 13) ->") : (fr ? "<- Page Précédente (Slots 1 - 7)" : "<- Previous Page (Slots 1 - 7)");
        menu.options.addOption(pageLabel, menu.pageSlotsCourante == 1 ? "NEXT_PAGE_SLOTS" : "PREV_PAGE_SLOTS");
        menu.options.addOption(fr ? "<- Retourner à la sélection de la flotte [Esc]" : "<- Return to fleet selection [Esc]", "BACK_SHIPS");
    }

    public static void afficherMenuActions(X1TerminalMenu menu) {
        menu.options.clearOptions();
        String idSanitized = menu.vaisseauSelectionne.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
        String fileName = "ExchangeRefit_" + idSanitized + "_Slot" + menu.slotChoisi + ".txt";
        boolean gabaritExiste = Global.getSettings().fileExistsInCommon(fileName);
        
        if (menu.textPanel != null) menu.textPanel.clear();
        boolean fr = X1Localisation.estEnFrancais();

        if (gabaritExiste) {
            X1PreviewUtil.générerEtAfficherRécapitulatif(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, menu);
        } else {
            X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
            String emptyMsg = fr ? "--- L'EMPLACEMENT SÉLECTIONNÉ " + menu.slotChoisi + " EST ACTUELLEMENT VIDE ---" : "--- SELECTED SLOT " + menu.slotChoisi + " IS CURRENTLY EMPTY ---";
            menu.textPanel.addParagraph(emptyMsg, Color.GRAY);
            menu.textPanel.addParagraph(""); 
        }
        
        String opPrompt = fr ? "Sélectionnez une opération pour l'Emplacement " : "Select an operation for Slot ";
        menu.textPanel.addParagraph(opPrompt + menu.slotChoisi + " :");
        
        menu.options.addOption(fr ? "Copier (Sauvegarder la configuration du vaisseau et le profil d'officier)" : "Copy (Save current ship layout & Officer profile to file)", "ACTION_COPY");
        menu.options.addOption(fr ? "[ATELIER DE STATION] Retirer tous les S-Mods du vaisseau & Remboursement 100% Story Points" : "[STATION WORKSHOP] Strip all S-Mods from ship & 100% Refund Story Points", "ACTION_STRIP_SMODS_CONFIRM");
        
        if (gabaritExiste) {
            menu.options.addOption(fr ? "Coller la Configuration (Par défaut - Appliquer tous les composants & l'Officier)" : "Paste Layout (Default - Apply all components & Officer)", "ACTION_PASTE_DEFAULT");
            menu.options.addOption(fr ? "Configurer les Filtres d'Application Sélective... [Configuration Personnalisée]" : "Configure Selective Paste Filters... [Custom Setup]", "ACTION_GOTO_FILTERS");
            menu.options.addOption(fr ? "Supprimer le plan (Effacer définitivement l'archive)" : "Delete blueprint (Permanently wipe archive)", "ACTION_DELETE");
        }
        menu.options.addOption(fr ? "<- Choisir un autre Emplacement" : "<- Choose another Slot", "BACK_SLOTS"); 
        menu.options.addOption(fr ? "<- Retourner à la sélection de la flotte [Esc]" : "<- Return to fleet selection [Esc]", "BACK_SHIPS");
    }

    public static void afficherMenuFiltres(X1TerminalMenu menu) {
        menu.options.clearOptions(); 
        if (menu.textPanel != null) menu.textPanel.clear();
        X1PreviewUtil.générerEtAfficherRécapitulatif(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, menu);
        
        boolean fr = X1Localisation.estEnFrancais();
        String filterPrompt = fr ? "Configurer les composants d'application sélective (Cliquez pour commuter) :" : "Configure Selective Paste Components (Click to Toggle):";
        menu.textPanel.addParagraph(filterPrompt, Color.CYAN);
        
        String stateOn = fr ? "[ACTIVÉ]" : "[ON]";
        String stateOff = fr ? "[DESACTIVÉ]" : "[OFF]";

        menu.options.addOption((fr ? "1. Monter l'arsenal d'armes : " : "1. Weapons Mounted: ") + (menu.filterWeapons ? stateOn : stateOff), "TOGGLE_WEAPONS");
        menu.options.addOption((fr ? "2. Installer les Hullmods standards & Flux : " : "2. Standard Hullmods: ") + (menu.filterHullmods ? stateOn : stateOff), "TOGGLE_HULLMODS");
        menu.options.addOption((fr ? "3. Appliquer les Modules d'Histoire (S-Mods) : " : "3. Story Mods (S-Mods): ") + (menu.filterSMods ? stateOn : stateOff), "TOGGLE_SMODS");
        menu.options.addOption((fr ? "4. Logique de contrôle d'artillerie (AGC / Ajustements IA) : " : "4. Advanced Gunnery Control (AGC): ") + (menu.filterTags ? stateOn : stateOff), "TOGGLE_TAGS");
        
        if (menu.vaisseauSelectionne != null && menu.vaisseauSelectionne.getVariant() != null && menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) {
            menu.options.addOption((fr ? "5. Escadrons de Chasseurs : " : "5. Fighter Wings: ") + (menu.filterFighters ? stateOn : stateOff), "TOGGLE_FIGHTERS");
        } else { 
            menu.filterFighters = false; 
        }
        
        menu.options.addOption((fr ? "6. Assignations des Groupes d'Armes : " : "6. Weapon Group Assignments: ") + (menu.filterGroups ? stateOn : stateOff), "TOGGLE_GROUPS");
        menu.options.addOption((fr ? "7. Réassigner le profil de l'Officier : " : "7. Re-assign Officer Blueprint: ") + (menu.filterOfficer ? stateOn : stateOff), "TOGGLE_OFFICER");
        
        // PURGE TERMINÉE : Remplacement chirurgical du mot à l'option 8
        menu.options.addOption((fr ? "8. Améliorations de Modifications Additionnelles : " : "8. Additional Refit Upgrades: ") + (X1TerminalRouteFinAiguillage.filterUpgradesStat ? stateOn : stateOff), "TOGGLE_UPGRADES");
        
        menu.options.addOption(fr ? "=> EXÉCUTER L'APPLICATION SÉLECTIVE" : "=> EXECUTE SELECTIVE PASTE", "EXECUTE_FILTERED_PASTE");
        menu.options.addOption(fr ? "<- Retourner au menu des opérations" : "<- Back to operation menu", "BACK_ACTIONS");
    }

    public static void executerOptionRoute(String opt, X1TerminalMenu menu) {
        X1TerminalRouteAiguillage.aiguillerBoutonAction(opt, menu);
    }

    public static void traiterCadreClavier(float amount, X1TerminalMenu menu) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { 
            menu.dialog.setPlugin(menu.originalPlugin); 
            if (menu.originalPlugin != null) menu.originalPlugin.init(menu.dialog); 
        }
    }
}
