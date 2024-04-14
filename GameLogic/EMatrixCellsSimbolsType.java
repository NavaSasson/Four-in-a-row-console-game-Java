package GameLogic;

public enum EMatrixCellsSimbolsType {

	X(1), 
	O(2);
	
	private final int value;

	EMatrixCellsSimbolsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
