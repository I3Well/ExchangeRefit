package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import java.awt.Color;
import java.util.Map;

public class X1SouteScanner {

    // Couleur orange foncé exacte conservée pour vos alertes de soute
    private static final Color ORANGE_FONCE = new Color(255, 90, 0);

    public static void exécuterAnalyseSouteAtelier(Map armesCompteurDuFichier, TextPanelAPI textPanel) {
        if (armesCompteurDuFichier == null || armesCompteurDuFichier.isEmpty() || textPanel == null) return;
        if (Global.getSector() == null || Global.getSector().getPlayerFleet() == null) return;

        try {
            com.fs.starfarer.api.campaign.CargoAPI souteFlotte = Global.getSector().getPlayerFleet().getCargo();
            
            // --- ÉTAPE 1 : PRE-SCAN POUR VÉRIFIER S'IL Y A AU MOINS UNE ARME MANQUANTE ---
            boolean aDesComposantsManquants = false;
            java.util.Iterator checkIterator = armesCompteurDuFichier.keySet().iterator();
            while (checkIterator.hasNext()) {
                String idArme = (String) checkIterator.next();
                int quantiteRequise = ((Integer) armesCompteurDuFichier.get(idArme)).intValue();
                if (souteFlotte.getNumWeapons(idArme) < quantiteRequise) {
                    aDesComposantsManquants = true;
                    break;
                }
            }

            // --- ÉTAPE 2 : AFFICHAGE DU TITRE UNIQUE OU DU MESSAGE DE SUCCÈS ---
            if (aDesComposantsManquants) {
                // Remplacement des répétitions par un en-tête unique et propre
                textPanel.addParagraph("   ⚠️ [COMPONENTS STOCKPILE WARNING] — UNVANAILABLE WEAPONS IN FLEET CARGO :", ORANGE_FONCE);
            } else {
                textPanel.addParagraph("   • Fleet Cargo Stockpile Availability Scan:", Color.WHITE);
                textPanel.addParagraph("     [ALL COMPONENTS READY] ➔ All requested blueprint parts are available in stock.", Color.GRAY);
                textPanel.addParagraph("");
                return; // On s'arrête ici si tout est prêt
            }

            // --- ÉTAPE 3 : IMPRESSION ÉPURÉE DES FICHES TECHNIQUES DES ARMES ABSENTES ---
            java.util.Iterator weaponIterator = armesCompteurDuFichier.keySet().iterator();
            while (weaponIterator.hasNext()) {
                String idArme = (String) weaponIterator.next();
                int quantiteRequise = ((Integer) armesCompteurDuFichier.get(idArme)).intValue();
                int quantitePossedee = souteFlotte.getNumWeapons(idArme);

                if (quantitePossedee < quantiteRequise) {
                    int quantiteManquante = quantiteRequise - quantitePossedee;

                    // Extraction des métadonnées réelles via l'API Starsector
                    WeaponSpecAPI specsArme = Global.getSettings().getWeaponSpec(idArme);
                    String nomLisible = (specsArme != null) ? specsArme.getWeaponName() : idArme;
                    int coutOP = (specsArme != null) ? (int) specsArme.getOrdnancePointCost(null) : 0;
                    int porteeMax = (specsArme != null) ? (int) specsArme.getMaxRange() : 0;
                    String typeEmplacement = (specsArme != null && specsArme.getType() != null) ? specsArme.getType().name() : "UNKNOWN";
                    
                    // Votre appel d'origine X1ModChecker
                    String modOrigine = X1ModChecker.getNomModOrigine(idArme, false);

                    // MISE EN PAGE NETTOYÉE : Plus de texte répétitif [MISSING COMPONENT] sur chaque ligne !
                    String ligneFicheTechnique = "     • Missing " + quantiteManquante + "x " + nomLisible + " [" + idArme + "]\n" +
                                                 "       Profile: [Type: " + typeEmplacement + " | Range: " + porteeMax + "px | Cost: " + coutOP + " OP | Origin: " + modOrigine + "]";
                    
                    textPanel.addParagraph(ligneFicheTechnique, ORANGE_FONCE);
                }
            }
            textPanel.addParagraph(""); // Saut de ligne final pour aérer le menu d'actions
            
        } catch (Exception e) {}
    }
}
