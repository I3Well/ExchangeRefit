package data.plugins;

import com.fs.starfarer.api.Global;

public class X1ArchiveDecoder {

    public static class StructureArchive {
        public String officier = "No Officer Assigned";
        public String niveau = "";
        public boolean isAI = false;
        public java.util.List competences = new java.util.ArrayList();
        public java.util.Map armes = new java.util.HashMap();
    }

    public static X1ArchiveDecoder.StructureArchive chargerDonnéesDepuisArchive(String fName) {
        X1ArchiveDecoder.StructureArchive sa = new X1ArchiveDecoder.StructureArchive();
        try {
            String contenu = Global.getSettings().readTextFileFromCommon(fName);
            if (contenu == null || contenu.isEmpty()) return sa;
            String[] lignes = contenu.replace("\r", "").split("\n");
            String dernierSlotId = "";
            
            for (int i = 0; i < lignes.length; i++) {
                String l = lignes[i].trim();
                if (l.startsWith("OFFICER_NAME:")) {
                    sa.officier = l.substring(13).trim();
                    if (sa.officier.toLowerCase().contains("core") || sa.officier.toLowerCase().contains("ia") || sa.officier.toLowerCase().contains("alpha") || sa.officier.toLowerCase().contains("beta") || sa.officier.toLowerCase().contains("gamma")) {
                        sa.isAI = true;
                    }
                }
                if (l.startsWith("OFFICER_LEVEL:")) {
                    sa.niveau = " (Level " + l.substring(14).trim() + ")";
                }
                if (l.startsWith("OFFICER_SKILL:")) {
                    String data = l.substring(14).trim();
                    if (data.contains("-")) {
                        sa.competences.add(data);
                    }
                }
                if (l.startsWith("ARME_SLOT:")) {
                    dernierSlotId = l.substring(10).trim();
                }
                if (l.startsWith("ARME_ID:") && !dernierSlotId.isEmpty()) {
                    sa.armes.put(dernierSlotId, l.substring(8).trim());
                    dernierSlotId = "";
                }
            }
        } catch (Exception e) {}
        return sa;
    }
}
