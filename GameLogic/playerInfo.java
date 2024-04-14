package GameLogic;

public class playerInfo {

	 private int m_currentPoints = 0;
	 private boolean m_isComputerPlayer = false;
	 private final ECoinType f_playerCoin;

	 public playerInfo()
	 {
	     f_playerCoin = ECoinType.P1;
	 }

	 public playerInfo(boolean i_IsComputerPlayer, ECoinType i_Coin)
	 {
	     m_isComputerPlayer = i_IsComputerPlayer;
	     f_playerCoin = i_Coin;
	 }

	 public int getCurrentPoints() {
	        return m_currentPoints;
	    }

	    public void setCurrentPoints(int currentPoints) {
	        this.m_currentPoints = currentPoints;
	    }

	    public boolean isComputerPlayer() {
	        return m_isComputerPlayer;
	    }

	    public ECoinType getPlayerCoin() {
	        return f_playerCoin;
	    }
}
