package GameLogic;

public enum EPlayerWantsAnotherGameType {
    YES(1),
    NO(2);

    private final int value;

    EPlayerWantsAnotherGameType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
