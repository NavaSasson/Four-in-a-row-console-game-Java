package GameLogic;

public enum EMatrixCellType {

	BLANK(0),
	P1(1),
	P2(2);
	
	 private final int value;

    EMatrixCellType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
