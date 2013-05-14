package chess.jchessboard;


public interface JChessBoard {

	ConnectionIndicator getConnectionIndicator();

	String getWhitePlayer();

	void setWhitePlayer(String whitePlayer);

	String getBlackPlayer();

	void setBlackPlayer(String blackPlayer);

	Settings getSettings();

	void setSettings(Settings settings);


	long getNewWhiteTime();

	void setNewWhiteTime(long newWhiteTime);

	long getNewBlackTime();

	void setNewBlackTime(long newBlackTime);

	boolean isCoupleSliders();

	void setCoupleSliders(boolean coupleSliders);

	Protocol getProtocol();

	void setProtocol(Protocol protocol);

	GameTable getGameTable();

	void setGameTable(GameTable gameTable);

	int getCurrentGameIndex();

	void setCurrentGameIndex(int currentGameIndex);

	void setConnectionIndicator(ConnectionIndicator connectionIndicator);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#getBoardConnector()
	 */
	BoardConnector getBoardConnector();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#setBoardConnector(chess.jchessboard.BoardConnector)
	 */
	void setBoardConnector(BoardConnector connector);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#isConnected()
	 */
	boolean isConnected();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#setEnableClock(boolean)
	 */
	void setEnableClock(boolean enable);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#getEnableClock()
	 */
	boolean getEnableClock();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#connectionClosed()
	 */
	void connectionClosed();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#connectionEstablished()
	 */
	void connectionEstablished();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#updateNavigationButtons()
	 */
	void updateNavigationButtons();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#getCurrentVirtualBoard()
	 */
	VirtualBoard getCurrentVirtualBoard();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#isWhiteTurn()
	 */
	boolean isWhiteTurn();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#makePeersMove(chess.jchessboard.Move)
	 */
	void makePeersMove(Move move);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#makeBatchMove(chess.jchessboard.Move)
	 */
	void makeBatchMove(Move move);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#makeAIsMove(chess.jchessboard.Move)
	 */
	void makeAIsMove(Move move);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#makeUsersMove(chess.jchessboard.Move)
	 */
	void makeUsersMove(Move move);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#triggerAI()
	 */
	void triggerAI();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#prepareMove()
	 */
	void prepareMove();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#switchSides()
	 */
	void switchSides();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#updateSTR()
	 */
	void updateSTR();

	/*
	 * Stops the game and creates the appropriate message if the game comes out
	 * to be finished.
	 */
	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#checkFinish()
	 */
	void checkFinish();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#newGame()
	 */
	void newGame();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#newGame(chess.jchessboard.VirtualBoard)
	 */
	void newGame(VirtualBoard vb);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#exit()
	 */
	void exit();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#updateMenu()
	 */
	void updateMenu();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#undoMove()
	 */
	void undoMove();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#showMessage(java.lang.String)
	 */
	void showMessage(String message);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#showMessage(java.lang.String, java.lang.String)
	 */
	void showMessage(String message, String style);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#showReplaceableMessage(java.lang.String)
	 */
	void showReplaceableMessage(String message);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#showReplaceableMessage(java.lang.String, java.lang.String)
	 */
	void showReplaceableMessage(String message, String style);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#timeForfeit()
	 */
	void timeForfeit();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#showTimeoutSelector()
	 */
	void showTimeoutSelector();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#setClocks(long, long)
	 */
	void setClocks(long whiteTime, long blackTime);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#gotoGame(int)
	 */
	void gotoGame(int newGameIndex);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#update()
	 */
	void update();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#isFileChanged()
	 */
	boolean isFileChanged();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#loadFile(java.io.File)
	 */
	void loadFile(java.io.File file);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#askForSaving()
	 */
	void askForSaving();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#save(java.lang.String, boolean)
	 */
	void save(String fileName, boolean append);

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#saveAs()
	 */
	void saveAs();

	/* (non-Javadoc)
	 * @see chess.jchessboard.JChessBoad#uploadGame()
	 */
	void uploadGame();

	ChessClock getChessClock();

	History getHistory();

	AI getAi();
	

}