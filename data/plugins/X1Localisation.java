package data.plugins;

import com.fs.starfarer.api.Global;

public class X1Localisation {

    public static boolean estEnFrancais() {
        try {
            org.json.JSONObject config = Global.getSettings().loadJSON("data/config/exchangerefit_config.json");
            if (config != null && config.has("french_translation")) {
                return config.getBoolean("french_translation");
            }
        } catch (Throwable t) {}
        return false;
    }

    public static String get(String cle) {
        boolean fr = estEnFrancais();
        if (cle == null) return "";

        if (cle.equals("DIAG_TITLE")) return fr ? "--- DIAGNOSTIC DU MATÉRIEL ÉQUIPÉ ---" : "--- CURRENT MOUNTED MATERIAL DIAGNOSTIC ---";
        if (cle.equals("HULL_SEL")) return fr ? "Coque Sélectionnée: " : "Hull Selected: ";
        if (cle.equals("HULL_METRICS")) return fr ? " Métriques de la Coque: Blindage: " : " Hull Metrics: Armor: ";
        if (cle.equals("ASSIGN_CAP")) return fr ? "Capitaine Assigné: " : "Assignee Captain: ";
        if (cle.equals("VACANT")) return fr ? "[EMPLACEMENT DE PONT VACANT] - Aucun Officier à bord" : "[VACANT MOUNT POINT] - No Officer on deck";
        if (cle.equals("WEAPONS_LAYOUT")) return fr ? "Arsenal d'Armes Équipées (Trié par Taille):" : "Equipped Weapons Arsenal (Sorted by Size):";
        if (cle.equals("FIGHTERS_LAYOUT")) return fr ? "Configuration des Escadrons de Chasseurs:" : "Equipped Fighter Squadrons Layout:";
        if (cle.equals("HULLMODS_LAYOUT")) return fr ? "Modifications de Coque Standards Installées:" : "Equipped Standard Hull Modifications:";
        if (cle.equals("SMODS_LAYOUT")) return fr ? "Modifications d'Histoire Intégrées (S-Mods):" : "Built-in Story Modifications (S-Mods):";
        if (cle.equals("OFFICER_AI")) return fr ? "Profil du Noyau d'IA Intégré:" : "Integrated AI Core Profile:";
        if (cle.equals("OFFICER_STANDARD")) return fr ? "Profil de l'Officier Actif:" : "Active Officer Profile:";
        
        if (cle.equals("SHORTAGE_WEAPONS")) return fr ? "Pénuries d'Armes Requises en Soute (Trié par Taille):" : "Required Stockpile Weapon Shortages (Sorted by Size):";
        if (cle.equals("SHORTAGE_HULLMODS")) return fr ? "Pénuries de Plans Techniques de Modifications:" : "Required Tech Blueprint Shortages:";
        if (cle.equals("SHORTAGE_EMPTY")) return fr ? "  Toutes les armes requises sont actuellement disponibles dans la soute." : "  All required weapons are currently available in cargo fleet stockpiles.";
        if (fr && cle.equals("All required hullmod plans are currently known by your faction.")) return "  Tous les plans de modifications de coque requis sont connus par votre faction.";

        if (cle.equals("SMOD_STRIP_TITLE")) return fr ? "--- ATELIER DE RECYCLAGE DES S-MODS ---" : "--- S-MOD STRIPPING & RECYCLING WORKSHOP ---";
        if (cle.equals("SMOD_STRIP_DESC")) return fr ? "Permet d'extraire les modifications d'histoire. Rembourse vos Story Points." : "Extract built-in Story Modifications. Refunds Story Points.";
        if (cle.equals("ACTION_STRIP_SMODS")) return fr ? "Retirer et Déclasser les S-Mods" : "Strip and Downgrade S-Mods";
        if (cle.equals("SMOD_STRIP_SUCCESS")) return fr ? "Nettoyage structurel terminé. Story Points restitués avec succès." : "Structural cleanup complete. Story Points successfully refunded.";
        
        if (cle.equals("FILTER_TITLE")) return fr ? "--- CONFIGURATION DES FILTRES ---" : "--- SELECTIVE APPLICATION FILTERS CONFIGURATION ---";
        if (cle.equals("FILTER_DESC")) return fr ? "Activez ou désactivez les catégories pour coller les éléments choisis." : "Toggle categories on or off to apply only selected elements.";
        if (cle.equals("TOGGLE_WEAPONS")) return fr ? "Filtre: Armes Équipées" : "Filter: Mounted Weapons";
        if (cle.equals("TOGGLE_HULLMODS")) return fr ? "Filtre: Modifications de Coque" : "Filter: Standard Hullmods";
        if (cle.equals("TOGGLE_SMODS")) return fr ? "Filtre: Modules d'Histoire (S-Mods)" : "Filter: Built-in S-Mods";
        if (cle.equals("TOGGLE_FIGHTERS")) return fr ? "Filtre: Escadrons de Chasseurs" : "Filter: Fighter Wings";
        if (cle.equals("TOGGLE_GROUPS")) return fr ? "Filtre: Groupes d'Armes & Raccourcis" : "Filter: Weapon Groups & Shortcuts";
        if (cle.equals("TOGGLE_OFFICER")) return fr ? "Filtre: Affectation de l'Officier" : "Filter: Officer Assignment";
        if (cle.equals("STATE_ON")) return fr ? " [ACTIVÉ]" : " [ENABLED]";
        if (cle.equals("STATE_OFF")) return fr ? " [DÉSACTIVÉ]" : " [DISABLED]";

        if (cle.equals("BTN_LEAVE")) return fr ? "Quitter le Terminal" : "Leave Terminal";
        if (cle.equals("BTN_BACK")) return fr ? "<- Retour" : "<- Back";
        if (cle.equals("BTN_NEXT_PAGE")) return fr ? "Page Suivante ->" : "Next Page ->";
        if (cle.equals("BTN_PREV_PAGE")) return fr ? "<- Page Précédente" : "<- Previous Page";
        if (cle.equals("SELECT_SHIP_PROMPT")) return fr ? "Sélectionnez un vaisseau de la flotte pour ouvrir son atelier :" : "Select a fleet vessel to open its workspace workshop:";

        return cle;
    }
}
