package data.plugins;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;
import java.util.List;

public class X1TerminalMenu implements InteractionDialogPlugin {
    protected final InteractionDialogPlugin originalPlugin;
    protected InteractionDialogAPI dialog;
    protected TextPanelAPI textPanel;
    protected OptionPanelAPI options;
    
    public FleetMemberAPI vaisseauSelectionne = null;
    public int slotChoisi = 1;
    public int pageSlotsCourante = 1;
    public List flotteFiltree = new ArrayList();
    
    public boolean filterWeapons = true;
    public boolean filterHullmods = true;
    public boolean filterSMods = true;
    public boolean filterTags = true;
    public boolean filterFighters = true;
    public boolean filterGroups = true;
    public boolean filterOfficer = true;

    public X1TerminalMenu(InteractionDialogPlugin originalPlugin) { 
        this.originalPlugin = originalPlugin; 
    }

    public void setPageSlotsCourante(int val) { this.pageSlotsCourante = val; }

    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog; 
        this.textPanel = dialog.getTextPanel(); 
        this.options = dialog.getOptionPanel();
        X1TerminalActions.initialiserMenu(this);
    }

    public void optionSelected(String text, Object data) {
        if (data == null) return;
        X1TerminalActions.traiterOptionSelectionnee((String) data, this);
    }

    public void advance(float amount) {
        X1TerminalMenusDisplay.traiterCadreClavier(amount, this);
    }
    
    public void optionMousedOver(String t, Object d) {}
    public void backFromEngagement(EngagementResultAPI r) {}
    
    // =========================================================================
    // CORES INTERFACES FIXES : SÉCURISATION ABSOLUE EXIGÉE PAR JANINO
    // =========================================================================
    @Override
    public java.lang.Object getContext() { 
        return null; 
    }
    
    @Override
    public java.util.Map getMemoryMap() { 
        return null; 
    }
}
