package data.plugins;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import org.lwjgl.input.Keyboard;

public class X1ClavierScript implements EveryFrameScript {
    
    private boolean isPressed = false;
    
    @Override
    public boolean isDone() { return false; }
    
    @Override
    public boolean runWhilePaused() { return true; }
    
    @Override
    public void advance(float amount) {
        if (Global.getSector() == null) return;
        CampaignUIAPI ui = Global.getSector().getCampaignUI();
        if (ui == null) return;
        
        // CONFINEMENT TOTAL : N'ouvrir que si le joueur est en vol libre dans l'espace.
        // Si ui.getCurrentInteractionDialog() est nul, aucun panneau de station ou de flotte n'est ouvert.
        if (ui.getCurrentInteractionDialog() == null) {
            
            if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
                if (!isPressed) {
                    isPressed = true;
                    
                    // Instanciation de votre terminal sous forme de fenêtre volante autonome
                    X1TerminalMenu menu = new X1TerminalMenu(null);
                    ui.showInteractionDialog(menu, Global.getSector().getPlayerFleet());
                }
            } else {
                isPressed = false;
            }
        }
    }
}
