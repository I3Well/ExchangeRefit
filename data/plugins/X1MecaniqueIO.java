package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import java.awt.Color;

public class X1MecaniqueIO {

    public static String assainirNomFichier(String hullId, int slot) {
        if (hullId == null) return "exchangerefit_unknown_slot" + slot + ".txt";
        return "exchangerefit_" + hullId.toLowerCase().trim() + "_slot" + slot + ".txt";
    }

    public static void exporterGabaritFile(FleetMemberAPI member, int slot, TextPanelAPI textPanel) {
        if (member == null || textPanel == null) return;
        String fileName = assainirNomFichier(member.getHullSpec().getHullId(), slot);
        try {
            PersonAPI captain = member.getCaptain();
            String nomOfficier = "No Officer Assigned";
            boolean isAI = false;
            if (captain != null) {
                isAI = captain.isAICore();
                nomOfficier = isAI ? "Integrated AI Core" : captain.getName().getFullName();
            }
            long cycleActuel = Global.getSector().getClock().getCycle();
            StringBuilder sb = new StringBuilder();
            sb.append("# COMMANDER: ").append(nomOfficier).append("\n");
            sb.append("# IS_AI: ").append(isAI ? "TRUE" : "FALSE").append("\n");
            sb.append("# CYCLE: ").append(cycleActuel).append("\n");
            sb.append("# HULL: ").append(member.getHullSpec().getHullId()).append("\n");
            sb.append("--------------------------------------------------\n");
            sb.append("VARIANT_DATA:").append(member.getVariant().getHullVariantId()).append("\n");

            if (captain != null && !captain.isDefault() && captain.getStats() != null) {
                java.util.List skills = captain.getStats().getSkillsCopy();
                for (int i = 0; i < skills.size(); i++) {
                    Object obj = skills.get(i);
                    if (obj != null) {
                        String strObj = obj.toString();
                        if (strObj.contains("=") && !strObj.contains("officier")) {
                            sb.append("OFFICER_SKILL:").append(strObj).append("\n");
                        }
                    }
                }
            }

            java.util.List slots = member.getVariant().getHullSpec().getAllWeaponSlotsCopy();
            for (int i = 0; i < slots.size(); i++) {
                WeaponSlotAPI s = (WeaponSlotAPI) slots.get(i);
                if (s != null && !s.isDecorative()) {
                    String wId = member.getVariant().getWeaponId(s.getId());
                    if (wId != null && !wId.isEmpty()) {
                        sb.append("WEAPON_SLOT:").append(s.getId()).append("=").append(wId).append("\n");
                    }
                }
            }

            for (String hm : member.getVariant().getHullMods()) {
                if (member.getVariant().getSMods().contains(hm)) {
                    sb.append("S_MOD:").append(hm).append("\n");
                } else {
                    sb.append("HULL_MOD:").append(hm).append("\n");
                }
            }

            Global.getSettings().writeTextFileToCommon(fileName, sb.toString());
            textPanel.addParagraph("==================================================", Color.GRAY);
            textPanel.addParagraph("-> Blueprint successfully saved into common/" + fileName, Color.GREEN);
            textPanel.addParagraph("==================================================", Color.GRAY);
        } catch (Exception e) {}
    }

    public static void importerGabaritFileMenu(X1TerminalMenu menu, boolean defaultFull) {
        if (menu == null || menu.vaisseauSelectionne == null || menu.textPanel == null) return;
        menu.textPanel.addParagraph("Executing assembly protocols with requested component filters...", Color.GREEN);
    }

    public static String lireNomOfficierDepuisFichier(String fileName) {
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                String contenu = Global.getSettings().readTextFileFromCommon(fileName);
                if (contenu != null && !contenu.isEmpty()) {
                    String[] lignes = contenu.replace("\r", "").split("\n");
                    for (int i = 0; i < lignes.length; i++) {
                        String l = lignes[i].trim();
                        if (l.contains("COMMANDER:") || l.contains("OFFICER:")) return l.substring(l.indexOf(":") + 1).trim();
                    }
                }
            }
        } catch (Exception e) {}
        return "No Officer Assigned";
    }

    public static String lireCycleDepuisFichier(String fileName) {
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                String contenu = Global.getSettings().readTextFileFromCommon(fileName);
                if (contenu != null && !contenu.isEmpty()) {
                    String[] lignes = contenu.replace("\r", "").split("\n");
                    for (int i = 0; i < lignes.length; i++) {
                        String l = lignes[i].trim();
                        if (l.contains("CYCLE:") || l.contains("DATE:")) return l.substring(l.indexOf(":") + 1).trim();
                    }
                }
            }
        } catch (Exception e) {}
        return "N/A";
    }

    public static String lireArmeDepuisSlotFichier(String fileName, String slotId) {
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                String contenu = Global.getSettings().readTextFileFromCommon(fileName);
                if (contenu != null && !contenu.isEmpty()) {
                    String[] lignes = contenu.replace("\r", "").split("\n");
                    for (int i = 0; i < lignes.length; i++) {
                        String l = lignes[i].trim();
                        if (l.startsWith("WEAPON_SLOT:") && l.substring(12).startsWith(slotId + "=")) {
                            return l.substring(12 + slotId.length() + 1).trim();
                        }
                    }
                }
            }
        } catch (Exception e) {}
        return "";
    }

    public static java.util.List lireHullmodsDepuisFichier(String fileName, FleetMemberAPI secours) {
        java.util.List list = new java.util.ArrayList();
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                String contenu = Global.getSettings().readTextFileFromCommon(fileName);
                String[] lignes = contenu.replace("\r", "").split("\n");
                boolean aDonnees = false;
                for (String l : lignes) {
                    if (l.trim().startsWith("HULL_MOD:")) {
                        list.add(l.trim().substring(9).trim());
                        aDonnees = true;
                    }
                }
                if (!aDonnees && secours != null && secours.getVariant() != null) {
                    for (String hm : secours.getVariant().getHullMods()) {
                        if (!secours.getVariant().getSMods().contains(hm)) list.add(hm);
                    }
                }
            }
        } catch (Exception e) {}
        return list;
    }

    public static java.util.List lireSModsDepuisFichier(String fileName, FleetMemberAPI secours) {
        java.util.List list = new java.util.ArrayList();
        try {
            if (Global.getSettings().fileExistsInCommon(fileName)) {
                String contenu = Global.getSettings().readTextFileFromCommon(fileName);
                String[] lignes = contenu.replace("\r", "").split("\n");
                boolean aDonnees = false;
                for (String l : lignes) {
                    if (l.trim().startsWith("S_MOD:")) {
                        list.add(l.trim().substring(6).trim());
                        aDonnees = true;
                    }
                }
                if (!aDonnees && secours != null && secours.getVariant() != null) {
                    list.addAll(secours.getVariant().getSMods());
                }
            }
        } catch (Exception e) {}
        return list;
    }

    public static java.util.List lireUpgradesDepuisFichier(String fileName) {
        return new java.util.ArrayList();
    }
}
