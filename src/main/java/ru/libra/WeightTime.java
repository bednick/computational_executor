package ru.libra;

import ru.bricks.command.Command;

/**
 *
 */
public class WeightTime implements ILibraCommand {
    @Override
    public float getWeight(Command command) {
        String timeStr = command.getMark("time");
        int time = 10;
        if (timeStr != null) {
            time = Integer.parseInt(timeStr);
        }
        return Math.min(100, Math.max(0, time));
    }
}
