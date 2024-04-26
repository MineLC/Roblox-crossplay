package lc.minelc.roblox.tasks;

import lc.minelc.roblox.network.deserializer.JsonDeserializer;
import lc.minelc.roblox.tasks.types.JoinPlayersTask;
import lc.minelc.roblox.tasks.types.MovePlayersTask;
import lc.minelc.roblox.tasks.types.QuitPlayersTask;

public final class TaskManager {

    private final JoinPlayersTask joinTask = new JoinPlayersTask();
    private final MovePlayersTask moveTask = new MovePlayersTask();
    private final QuitPlayersTask quitPlayersTask = new QuitPlayersTask();

    public void execute(final JsonDeserializer format) {
        joinTask.execute(format.getPlayerJoins());
        moveTask.execute(format.getPlayersData());
        quitPlayersTask.execute(format.quitPlayers());
    }
}