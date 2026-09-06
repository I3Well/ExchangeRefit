package data.plugins;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import org.lwjgl.input.Keyboard;

public class X1ClavierScript implements EveryFrameScript {
    
    public boolean isDone() { return false; }
    public boolean runWhilePaused() { return true; }
    
    public void advance(float amount) {
        if (Global.getSector() == null) return;
        CampaignUIAPI ui = Global.getSector().getCampaignUI();
        if (ui == null || ui.getCurrentInteractionDialog() == null) return;
        
        InteractionDialogAPI dialog = ui.getCurrentInteractionDialog();
        InteractionDialogPlugin originalPlugin = dialog.getPlugin();
        if (originalPlugin == null) return;

        if (originalPlugin instanceof X1TerminalMenu) {
            return;
        }

        OptionPanelAPI options = dialog.getOptionPanel();
        if (options != null) {
            String pluginClass = originalPlugin.getClass().getName().toLowerCase();
            
            if (pluginClass.contains("barcmd") || pluginClass.contains("combat") || 
                pluginClass.contains("refit") || pluginClass.contains("fleet") ||
                pluginClass.contains("contact") || pluginClass.contains("intel") || 
                pluginClass.contains("officer") || pluginClass.contains("person")) {
                if (options.hasOption("TERMINAL_EXCHANGE_REFIT")) {
                    options.removeOption("TERMINAL_EXCHANGE_REFIT");
                }
                return; 
            }

            SectorEntityToken cible = dialog.getInteractionTarget();
            if (cible != null && cible.getMarket() != null) {
                if (dialog.getInteractionTarget().getActivePerson() != null) {
                    if (options.hasOption("TERMINAL_EXCHANGE_REFIT")) {
                        options.removeOption("TERMINAL_EXCHANGE_REFIT");
                    }
                    return;
                }

                if (!options.hasOption("TERMINAL_EXCHANGE_REFIT")) {
                    options.addOption("For access to ExchangeRefit Terminal, press [X]", "TERMINAL_EXCHANGE_REFIT");
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                    X1TerminalMenu menu = new X1TerminalMenu(originalPlugin);
                    dialog.setPlugin(menu);
                    menu.init(dialog);
                }
            }
        }
    }
}
