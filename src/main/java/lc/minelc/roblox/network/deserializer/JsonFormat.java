package lc.minelc.roblox.network.deserializer;

public final record JsonFormat(
    String[] players,
    String[] changed_blocks,
    String[] joins,
    String[] quits
) {
}