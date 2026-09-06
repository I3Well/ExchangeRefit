package data.plugins;

import com.fs.starfarer.api.campaign.TextPanelAPI;
import java.util.ArrayList;

public class X1PreviewPrinter {

    public static void imprimerArmesColoreesAtelier(String title, ArrayList ids, TextPanelAPI textPanel) {
        if (ids == null || ids.isEmpty()) return;
        textPanel.addParagraph(title, java.awt.Color.CYAN);
        
        for (int i = 0; i < ids.size(); i++) {
            String wId = (String) ids.get(i);
            String displayLine = X1SpecExtractor.extraireArme(wId);
            if (displayLine == null) continue;
            
            String dmgType = X1SpecExtractor.getArmeDamageType(wId);
            java.awt.Color couleurLigne = java.awt.Color.WHITE; 

            if (dmgType.equals("HIGH_EXPLOSIVE")) {
                couleurLigne = new java.awt.Color(220, 90, 90); 
            } else if (dmgType.equals("KINETIC")) {
                couleurLigne = new java.awt.Color(245, 245, 220); 
            } else if (dmgType.equals("ENERGY")) {
                couleurLigne = new java.awt.Color(65, 105, 225); 
            } else if (dmgType.equals("FRAGMENTATION")) {
                couleurLigne = java.awt.Color.YELLOW; 
            }

            textPanel.addParagraph(displayLine, couleurLigne);
        }
    }

    public static void imprimerSectionRécapAtelier(String title, String[] items, TextPanelAPI textPanel, java.awt.Color txtColor) {
        if (items == null || items.length == 0) return;
        textPanel.addParagraph(title, java.awt.Color.CYAN);
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) textPanel.addParagraph(items[i], txtColor);
        }
    }
}
