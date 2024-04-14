package GameLogic;

public enum EPlayerType {
	
	HUMAN(1),
    COMPUTER(2);

    private final int value;

    EPlayerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
