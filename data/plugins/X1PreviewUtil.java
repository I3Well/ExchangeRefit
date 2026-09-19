package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class X1PreviewUtil {

    private static String getNomLisibleLocal(String id) {
        if (id == null) return "Unknown";
        String cleanId = id.trim().toLowerCase();
        if (cleanId.startsWith("helmsmanship")) return "Helmsmanship";
        if (cleanId.startsWith("combat_endurance")) return "Combat Endurance";
        if (cleanId.startsWith("impact_mitigation")) return "Impact Mitigation";
        if (cleanId.startsWith("damage_control")) return "Damage Control";
        if (cleanId.startsWith("field_modulation")) return "Field Modulation";
        if (cleanId.startsWith("point_defense")) return "Point Defense";
        if (cleanId.startsWith("target_analysis")) return "Target Analysis";
        if (cleanId.startsWith("ballistic_mastery")) return "Ballistic Mastery";
        if (cleanId.startsWith("systems_expertise")) return "Systems Expertise";
        if (cleanId.startsWith("missile_specialization")) return "Missile Specialization";
        if (cleanId.startsWith("gunnery_implants")) return "Gunnery Implants";
        if (cleanId.startsWith("energy_weapon_mastery")) return "Energy Weapon Mastery";
        if (cleanId.startsWith("ordnance_expert")) return "Ordnance Expert";
        if (cleanId.startsWith("polarized_armor")) return "Polarized Armor";
        if (cleanId.startsWith("neural_link")) return "Neural Link";
        if (cleanId.startsWith("cybernetic_augmentation")) return "Cybernetic Augmentation";
        return id;
    }

    public static void générerEtAfficherRécapitulatif(FleetMemberAPI actuel, int slotIndex, TextPanelAPI panel, X1TerminalMenu menu) {
        if (actuel == null || panel == null) return;
        String idSanitized = actuel.getHullSpec().getHullId().replaceAll("[\\\\/:*?\"<>|\\s]", "_");
        String fileName = "ExchangeRefit_" + idSanitized + "_Slot" + slotIndex + ".txt";
        if (!Global.getSettings().fileExistsInCommon(fileName)) return;

        boolean fr = X1Localisation.estEnFrancais();
        panel.addParagraph(fr ? "--- PROFILE DU PLAN ARCHIVÉ (SLOT " + slotIndex + ") ---" : "--- ARCHIVED SPECIFICATION BLUEPRINT (SLOT " + slotIndex + ") ---", Color.CYAN);
        panel.addParagraph((fr ? "Profil du Plan: Classe " : "Blueprint Profile: ") + actuel.getHullSpec().getHullName() + " [" + actuel.getHullSpec().getDesignation() + "]", Color.WHITE);
        panel.addParagraph((fr ? " Métriques: Blindage: " : " Blueprint Hull Metrics: Armor: ") + (int)actuel.getHullSpec().getArmorRating() + " | Vents: " + actuel.getVariant().getNumFluxVents() + " | Capacitors: " + actuel.getVariant().getNumFluxCapacitors(), Color.LIGHT_GRAY);
        panel.addParagraph("");

        List shortLarge = new ArrayList();
        List shortMedium = new ArrayList();
        List shortSmall = new ArrayList();
        List planShortages = new ArrayList();

        X1ArchiveDecoder.StructureArchive data = X1ArchiveDecoder.chargerDonnéesDepuisArchive(fileName);

        X1WeaponPreview.traiterArmesEtChasseursArchive(actuel, data, panel, shortLarge, shortMedium, shortSmall);

        X1ShortageChecker.verifierHullmodsFaction(fileName, actuel, planShortages);

        panel.addParagraph(X1Localisation.get(data.isAI ? "OFFICER_AI" : "OFFICER_STANDARD"), Color.LIGHT_GRAY);
        panel.addParagraph("  " + data.officier + data.niveau, data.isAI ? Color.ORANGE : Color.WHITE);
        
        for (int i = 0; i < data.competences.size(); i++) { 
            String rawSkill = (String) data.competences.get(i);
            if (rawSkill == null || rawSkill.isEmpty()) continue;
            boolean isElite = rawSkill.contains("-2") || rawSkill.contains("elite");
            String skillId = rawSkill.trim();
            int separatorIndex = rawSkill.indexOf("-");
            if (separatorIndex != -1) skillId = rawSkill.substring(0, separatorIndex).trim();
            String skillName = getNomLisibleLocal(skillId);
            panel.addParagraph(isElite ? "    - " + skillName + " [ELITE]" : "    - " + skillName, isElite ? Color.GREEN : Color.YELLOW);
        }

        panel.addParagraph("");
        panel.addParagraph(X1Localisation.get("SHORTAGE_WEAPONS"), Color.LIGHT_GRAY);
        if (shortLarge.isEmpty() && shortMedium.isEmpty() && shortSmall.isEmpty()) {
            panel.addParagraph(X1Localisation.get("SHORTAGE_EMPTY"), Color.GREEN);
        } else {
            for (int i = 0; i < shortLarge.size(); i++) { X1ElementArme e = (X1ElementArme) shortLarge.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }
            for (int i = 0; i < shortMedium.size(); i++) { X1ElementArme e = (X1ElementArme) shortMedium.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }
            for (int i = 0; i < shortSmall.size(); i++) { X1ElementArme e = (X1ElementArme) shortSmall.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }
        }

        panel.addParagraph(X1Localisation.get("SHORTAGE_HULLMODS"), Color.LIGHT_GRAY);
        if (planShortages.isEmpty()) {
            panel.addParagraph(X1Localisation.get("All required hullmod plans are currently known by your faction."), Color.GREEN);
        } else {
            Color orangeFonce = new Color(220, 90, 10);
            for (int i = 0; i < planShortages.size(); i++) { panel.addParagraph((String) planShortages.get(i), orangeFonce); }
        }
        panel.addParagraph("");
    }
}
