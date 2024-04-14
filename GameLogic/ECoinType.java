package GameLogic;

public enum ECoinType {

	P1(0),
	P2(1);
	
	 private final int value;

	ECoinType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
