package net.tropicbliss.tabgrabber.grabber;

sealed interface ScoreboardKey permits Player, Metadata {
    String getString();
}




