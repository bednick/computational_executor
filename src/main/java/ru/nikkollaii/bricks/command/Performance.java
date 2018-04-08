package ru.nikkollaii.bricks.command;

public enum Performance {
    NOT_SPECIFIED,
    CAN_PERFORMED, // может выполняться
    CAN_NOT_PERFORMED,
    TRAJECTORY, // Выбрана алгоритмом -> необходимо исполнить
    RUNNING, // запущенно
    PERFORMED_CORRECT,
    PERFORMED_INCORRECT;

    public static boolean isFinish(Performance performance) {
        return performance.equals(PERFORMED_CORRECT) || performance.equals(PERFORMED_INCORRECT);
    }


}
