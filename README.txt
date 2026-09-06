===============================================================================
                       EXCHANGE REFIT - SHIPYARD TERMINAL
                                 Version 1.3.0
===============================================================================

Author: Anjasnare
Game Version Compatibility: Starsector 0.98a-RC8 (and above)
Dependencies: None (100% Standalone)
Supported Mod Utilities: Version Checker, Advanced Gunnery Control (AGC), Progressive S-Mods

-------------------------------------------------------------------------------
1. OVERVIEW
-------------------------------------------------------------------------------
ExchangeRefit introduces a powerful, secure Shipyard Terminal accessible 
directly from station docks. It allows captains to scan, duplicate, archive, 
and selectively paste complete ship loadouts using resources from the fleet 
cargo and can reattribute s_mods. 

Never manually re-fit a fleet of identical hulls weapon-by-weapon again!

-------------------------------------------------------------------------------
2. KEY FEATURES
-------------------------------------------------------------------------------
* Docks Hotkey: Press [X] while interacting with any station dock to open the 
  ExchangeRefit Terminal immediately.
* Live Hull Scanner Diagnostic: Instantly displays an advanced breakdown of 
  your active hulls, including deployment DP costs, shield types, live speed, 
  unassigned Ordnance Points (OP), custom station refit upgrades, and detailed 
  officer or AI Core skill profiles.
* Smart Weapon Sorting: Mounted weaponry in reviews is automatically filtered 
  and sorted by size brackets (Large, Medium, Small) and ranked by OP costs.
* Advanced Visual Palette: Weapons are dynamically color-coded by damage type 
  for swift tactical assessment (Red for High Explosive, White/Beige for Kinetic, 
  Blue for Energy, Yellow for Fragmentation).
* Selective Custom Setup (Paste Filters): Choose exactly what components to 
  apply from a blueprint. Toggle individual modules:
    1. Mounted Weapons
    2. Standard Hullmods (sorted by OP)
    3. Story Mods (S-Mods)
    4. Advanced Gunnery Control (AGC) weapon tags
    5. Fighter Wings layout
    6. Weapon Group assignments
    7. Officer profile assignment
    8. Additional Refit Upgrades (Station specific sub-upgrades / Mod tracking)
* Progressive S-Mods Synergy: Fully compatible with the Progressive S-Mods framework. 
  By toggling Option 8 (Additional Refit Upgrades), your hull's progressive experience 
  (XP) tags and unique progression data are safely archived and restored.
* Dynamic Refit Command Previews: A visual validation step showing a concise 
  [YES] / [NO] confirmation checklist before executing any layout override.
* Secure S-Mod Stripper Tool: Refund and manage built-in modifications with 
  two safe workshop commands:
    - Option 1 (Complete Wipe): Removes S-Mods and returns 100% Story Points.
    - Option 2 (Downgrade to Standard): Strips the S-Mod status but safely 
      attempts to keep the mod equipped as a normal Hullmod (blocks action and 
      notifies you with cost requirements if remaining ship OP is insufficient).
* Ergonomic Navigation: Native terminal design supporting raw [Esc] hotkey mapping 
  on all backward options.

-------------------------------------------------------------------------------
3. INSTALLATION
-------------------------------------------------------------------------------
1. Extract the content of the "ExchangeRefit.zip" file.
2. Place the "ExchangeRefit" folder inside your Starsector mod directory:
   "../Starsector/mods/"
3. Launch Starsector and enable "ExchangeRefit" in the mod manager checklist.

-------------------------------------------------------------------------------
4. CONFIGURATION & SAVES
-------------------------------------------------------------------------------
Your ship templates are universally archived as structural plain text blueprints. 
They are automatically written to and loaded from your local Starsector common 
save path:
"../Starsector/saves/common/ExchangeRefit_[Hull_ID]_Slot[1-13].txt"

Blueprints can be easily shared between profiles or manually wiped through the 
terminal interface using the "Delete blueprint" operational command.

-------------------------------------------------------------------------------
5. CHANGELOG & STABILITY (v1.2.0)
-------------------------------------------------------------------------------
* Refactored data loading architecture into dedicated lightweight sub-modules 
  to fully comply with Janino script compilation buffer constraints.
* Eliminated reflection-based class structures within sub-loops to satisfy 
  Starsector's script security sandbox restrictions.
* Confirmed compatibility matrix and architecture support for Progressive S-Mods XP mechanics.
* Patched Work-in-Progress variant clones crash toggles by enforcing preventive 
  fleet variant committing procedures.
* Added full Version Checker metadata framework integration ("ExchangeRefit.version").
* Implemented modular isolation for Option 8 (Additional Upgrades) handling 
  custom faction and station infrastructure tags.

-------------------------------------------------------------------------------
Thank you for downloading ExchangeRefit! Safe travels through the Persean Sector.
===============================================================================
