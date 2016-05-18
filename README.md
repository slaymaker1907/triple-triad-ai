# TripleTriadSimulator

In order to download a release, please refer to the GitHub release page.

The TripleTriadSimulator is a simulation engine, ai, and associated GUI for the mini-game Triple Triad as found in FFXIV (though support for other versions may be added at some later time). It's primary use case is to be used by a player against the native AI of FFXIV in order to beat NPCs in game that would otherwise be too difficult for a player to beat on their own (this a standalone program that in no way directly interfaces with FFXIV).

In addtion to the GUI, other modules include the TRIPLE_TRIAD_ENGINE and the TRIPLE_TRIAD_AI which provide a highly optimized implementation of the Triple Triad game mechanics as well as a somewhat intelligent AI for playing the game. Detailed documentation is not available at this time (though might be available at some later time); please contact slaymaker1907 if you would like more information on contributing or using the source code.

# User Guide

Use of this software requires Java 8 (64-bit) and is currently only supported on Windows (OSX is untested, but there are some issues with the GUI on Linux related to drag and drop). This program requires at least 4G of RAM and works best with at least 8G of RAM.

To run the program, use the batch file included in the download from the release page. Running the raw jar works as well, though certain command line arguments in the batch file are neccessary for optimal performance.

Once the program is running, click the Build Deck button to create your deck. The deck builder is case sensitive and uses predictive text for efficient use. Once you have built your deck, click the Load Deck button on the let side to upload your deck in the simulator. Then, upload upload a deck on the right either from the npc_decks folder (located in the program directory) or load a custom deck. Then, adjust the Settings by clicking the Settings button to select your desired game mode as well as options specific to this program. Finally, click Start to begin the simulator.

Switching between AI and Manual modes is instantaneous and can be done even while a game is in progress.

The Quick Load button opens the Deck Builder and is identical to that feature except that it does not save to disk and instead immediatly inputs the deck as the associated player's deck.

# Game Mode Notes

Hidden cards aren't supported but can be simulated by creating a deck with > 5 cards.

Order is asymetric in that it is only enforced for the left (blue) player as a consequence of the above rule.

Random is supported through the QuickLoad button. This feature allows for creation of a temporary new deck (not saved to disk) using the Deck Builder.

Chaos is not supported due to the challenges of building an AI for this game mode. All other game modes of FFXIV Triple Triad are supported.
