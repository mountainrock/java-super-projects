/* JChessBoard -- a chess game
 * Copyright (C) 2000-2004 Claus Divossen <claus.divossen@gmx.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

/* $Id: History.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * This class maintains the history of the game and creates an user
 * interface to it.
 */
class History extends JScrollPane {

	private HistorySelectionListener selectionListener;
//	private TreeModelListener notationUpdateListener;
	private JChessBoard jcb;
	private JTree tree;
	private boolean isChanged = true;
	private JPopupMenu treeContextMenu;
	private GameNode topGameNode;
	private DefaultTreeModel treeModel;
	private GameNode currentGameNode;
	private int gameIndex = -1;
	PGN.STR str=null;

	public static String getVersion() {
		return "$Id: History.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	/**
	 * The HistroySelectionListener allows the user to select a board position
	 * directly from the history list. It has to be disabled sometimes since
	 * selection changes that are not directly made from the user (e.g. using the 
	 * PREV and NEXT buttons below the list, which are not part of the JTree)
	 * will also cause this listener to be invoked.
	 */
	class HistorySelectionListener
	implements javax.swing.event.TreeSelectionListener {
		boolean isEnabled = true;
		;
		/**
		 * Enable/disable this Listener.
		 */
		public void setEnabled(boolean en) {
			isEnabled = en;
		}

		/**
		 * Invoked on changes to the list selection.
		 */
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (!isEnabled)
				return;
			synchronized (treeModel) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode childNode =
					(DefaultMutableTreeNode) path.getLastPathComponent();
				if (childNode instanceof GameNode) {
					gotoGameNode((GameNode) childNode);
				}
			}
		}
	}


	public void editComment() {
		Object message[] = new Object[4];
		String comment = currentGameNode.getComment();
		message[0] = "Edit comment:\n(Linefeeds will be removed.)";
		JTextArea commentField = new JTextArea(comment, 5, 40);
		commentField.setLineWrap(true);
		commentField.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(commentField);
		message[1] = scrollPane;
		JList jlist = null;
		if (currentGameNode.isLeaf()) {
			message[2] = "Numerical Annotation Glyphs (NAG):";
			Vector nagList = new Vector();
			for (int n = 0; n < PGN.NAGStrings.length; n++)
				nagList.add(n + ". " + PGN.NAGStrings[n]);
			jlist = new JList(nagList);
			message[3] = new JScrollPane(jlist);
			//    jlist.setEnabled(false);
		} else {
			message[2] = null;
			message[3] = null;
		}
		jlist.setSelectedIndices(currentGameNode.getNags());
		Object options[] = { "OK", "Cancel" };

		int result =
			JOptionPane.showOptionDialog(
					null,
					message,
					"Edit comment",
					0,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);

		if (result == 0) {
			StringBuffer commentBuffer =
				new StringBuffer(commentField.getText());
			for (int n = 0; n < commentBuffer.length(); n++)
				if (commentBuffer.charAt(n) == '\n')
					commentBuffer.setCharAt(n, ' ');
			if (commentBuffer.length() > 0)  {
				currentGameNode.setComment(commentBuffer.toString());
			}
			if (currentGameNode.isLeaf()) {
				int[] indices = jlist.getSelectedIndices();
				if (indices.length > 1
						|| (indices.length == 1 && indices[0] > 0))
					currentGameNode.setNags(indices);
				else
					currentGameNode.setNags(new int[0]);

			}
			treeModel.nodeChanged(currentGameNode);
			isChanged = true;
		}
	}

	public String getPGNBody() {
		StringBuffer pgn = new StringBuffer();
		pgn.append(topGameNode.getPGN());
		if (str != null) {
			if (str.getTag("Result").equals("?"))
				pgn.append(((GameNode) topGameNode.getLastLeaf()).getBoard().getResult());
			else
				pgn.append(str.getTag("Result"));
			pgn.append("\n");
		}
		return pgn.toString();
	}

	public void setBoard(VirtualBoard vb) {
		currentGameNode.setBoard(vb);
		treeModel.nodeChanged(currentGameNode);
		isChanged = true;
	}
	public VirtualBoard getStartBoard() {
		return topGameNode.getBoard();
	}
	public VirtualBoard getCurrentBoard() {
		return currentGameNode.getBoard();
	}
	public GameNode getCurrentGameNode() {
		return currentGameNode;
	}
	public GameNode getTopGameNode() {
		return topGameNode;
	}

	public void loadGame(String pgn, PGN.STR str) throws VirtualBoard.ImpossibleMoveException {
		this.str=str;
		topGameNode.reset();
		currentGameNode = topGameNode;
		try {
			if(str.hasTag("SetUp"))
				topGameNode.getBoard().initFromFEN(str.getTag("FEN"));
			PGN.parseGame(this, topGameNode.getBoard(), pgn, false);
			treeModel.reload();
			isChanged = false;
		} catch (VirtualBoard.ImpossibleMoveException e) {
			treeModel.reload();
			isChanged = false;
			throw new VirtualBoard.ImpossibleMoveException(e.toString());
		}
	}

	public void setResult(String result) {
		str.setTag("Result", result);
		isChanged = true;
	}

	/**
	 * Clears all entries from the history. 
	 */
	public void clear() {
		topGameNode.reset();
		treeModel.reload();
		gotoGameNode(topGameNode);
		gameIndex = -1;
		isChanged = false;
	}

	/**
	 * Adds a move to the history.
	 * @param algMove Algebraic notation of the move.
	 * @param vb The VirtualBoard the given move leads to.
	 */
	public void addBatchMove(
			String algMove,
			VirtualBoard vb) {
		synchronized (this) {
			GameNode newGameNode = new GameNode(algMove, vb);
			if ( currentGameNode.getMove() ==null || currentGameNode.getMove().length() == 0) {
				// current node is variation or game start
				if (currentGameNode.isLeaf())
					// insert first move into variation
					treeModel.insertNodeInto(newGameNode, currentGameNode, 0);
				else { // Start sub-variation
					GameNode newParentNode =
						new GameNode("", currentGameNode.getBoard());
					treeModel.insertNodeInto(newParentNode, currentGameNode, 1);
					treeModel.insertNodeInto(newGameNode, newParentNode, 0);
				}
			} else {
				GameNode parent = (GameNode) currentGameNode.getParent();
				int childIndex = parent.getIndex(currentGameNode);
				int lastLeafIndex = parent.getChildCount() - 1;
				while (!parent.getChildAt(lastLeafIndex).isLeaf()
						&& lastLeafIndex > 0) {
					lastLeafIndex--;
				}
				if (childIndex == lastLeafIndex)
					// currentGameNode is last leaf-child, just append move
					treeModel.insertNodeInto(
							newGameNode,
							parent,
							parent.getChildCount());
				else { // Start a variation
					GameNode newParentNode =
						new GameNode("", currentGameNode.getBoard());
					int insertIndex = childIndex + 1;
					while (!parent.getChildAt(insertIndex).isLeaf())
						insertIndex++;
					treeModel.insertNodeInto(
							newParentNode,
							parent,
							insertIndex + 1);
					treeModel.insertNodeInto(newGameNode, newParentNode, 0);
				}
			}
			currentGameNode = newGameNode;
		}
		isChanged = true;
	}

	public void addMove(String algMove, VirtualBoard vb) {
		addBatchMove(algMove, vb);
		gotoGameNode(currentGameNode);
	}

	public void addComment(String comment) {
		currentGameNode.addComment(comment);
		treeModel.nodeChanged(currentGameNode);
		isChanged = true;
	}

	public void addNAG(int nag) {
		currentGameNode.addNAG(nag);
		treeModel.nodeChanged(currentGameNode);
		isChanged = true;
	}

	public void addVariation(VirtualBoard startBoard, String pgnData) throws VirtualBoard.ImpossibleMoveException {
		GameNode lastGameNode = currentGameNode;
		GameNode parent = (GameNode) currentGameNode.getParent();
		int childIndex = parent.getIndex(currentGameNode);
		GameNode variationNode = new GameNode("", startBoard);
		treeModel.insertNodeInto(variationNode, parent, childIndex + 1);
		currentGameNode = variationNode;
		PGN.parseGame(this, startBoard, pgnData,false);
		currentGameNode = lastGameNode;
		isChanged = true;
	}

	/**
	 * Removes the last entry from the history.
	 */
	public void removeLastMove() {
		synchronized (this) {
			if (currentGameNode == topGameNode || !currentGameNode.isLeaf())
				return;
			GameNode targetNode = currentGameNode;
			do targetNode = (GameNode) targetNode.getNextSibling();
			while (targetNode != null && !targetNode.isLeaf());
			if (targetNode != null) // There is another move following
			return;
			targetNode = currentGameNode;
			prev();
			GameNode parent = (GameNode) targetNode.getParent();
			treeModel.removeNodeFromParent(targetNode);
			if (parent.isLeaf()
					&& parent != topGameNode) { // Remove empty variation also
				prev();
				treeModel.removeNodeFromParent(parent);
			}
			isChanged = true;
		}
	}

	/**
	 * Returns the size of this history list.
	 */
	public int getFullMoveNumber() {
		return currentGameNode.getFullMoveNumber();
	}

	public void gotoGameNode(GameNode gameNode) {
		if (gameNode == null)
			return;
		synchronized (jcb) {
			synchronized (this) {
				selectionListener.setEnabled(false);
				TreePath path = new TreePath(gameNode.getPath());
				tree.setSelectionPath(path);
				tree.makeVisible(path);
				tree.scrollRowToVisible(tree.getRowForPath(path));
				/*                String annotation = "";
                if (gameNode.hasComment()) {
                    annotation = gameNode.getAnnotation();
                    jcb.showReplaceableMessage(annotation, "annotation");
                } else
                    jcb.showReplaceableMessage(""); */
				currentGameNode = gameNode;
				selectionListener.setEnabled(true);
				jcb.update();
			}
		}
	}
	public void gotoLast() {
		gotoGameNode((GameNode) topGameNode.getLastLeaf());
	}
	public void gotoFirst() {
		gotoGameNode(topGameNode);
	}

	/**
	 * 
	 * @return A List of VirtualBoards that lead to the current position.
	 */
	public List getAllBoards() {
		List result = new Vector();
		GameNode node = currentGameNode;
		GameNode lastNode = node;
		result.add(node.getBoard());
		do {	
			if (node == null)
				return result;

			do {
				lastNode=node;
				node = (GameNode) node.getPreviousSibling();
			} while (node != null && !node.isLeaf());

			if(node!=null && node.isLeaf())
				result.add(node.getBoard());

			if (node == null)
				node = (GameNode) lastNode.getParent();
		}	while(node != null); 
		return result;
	}

	public GameNode getPreviousGameNode() {
		GameNode targetNode = currentGameNode;
		do targetNode = (GameNode) targetNode.getPreviousSibling();
		while (targetNode != null && !targetNode.isLeaf());
		if (targetNode == null) 
			targetNode = (GameNode) currentGameNode.getParent();
//		if (targetNode != null) 
//		targetNode = (GameNode) ((GameNode) currentGameNode.getParent()).getPreviousSibling();
		if (targetNode != null)
			return targetNode;
		else
			return currentGameNode;
	}

	public void prev() {
		gotoGameNode(getPreviousGameNode());
	}

	public GameNode getNextGameNode() {
		if (!currentGameNode.isLeaf())
			return (GameNode) currentGameNode.getChildAt(0);
		else {
			GameNode targetNode = currentGameNode;
			do targetNode = (GameNode) targetNode.getNextSibling();
			while (targetNode != null && !targetNode.isLeaf());
			if (targetNode != null)
				return targetNode;
			else
				return currentGameNode;
		}
	}

	public void next() {
		gotoGameNode(getNextGameNode());
	}

	/**
	 * Creates a new instance.
	 */
	 public History(JChessBoard jChessBoard) {
		jcb = jChessBoard;

		VirtualBoard startBoard = new VirtualBoard();
		startBoard.init();
		topGameNode = new GameNode("", startBoard);
		currentGameNode = topGameNode;
		treeModel = new DefaultTreeModel(currentGameNode);
		str = new PGN.STR();
		tree = new JTree(treeModel);
		tree.setEditable(false);
		tree.setRootVisible(true);
		tree.setShowsRootHandles(false);
		selectionListener = new HistorySelectionListener();
		tree.addTreeSelectionListener(selectionListener);
		//        tree.setRowHeight(14);
		(tree.getSelectionModel()).setSelectionMode(
				DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);

		treeContextMenu = new JPopupMenu();
		JMenuItem treeEditCommentItem = new JMenuItem("Edit comment...");
		treeEditCommentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComment();
				jcb.update();
			}
		});
		treeContextMenu.add(treeEditCommentItem);
		JMenuItem treeRemoveVariationItem = new JMenuItem("Remove variation");
		treeRemoveVariationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// removeVariation();
				DefaultMutableTreeNode removeNode = currentGameNode;
				while (removeNode.isLeaf() && removeNode != topGameNode)
					removeNode =
						(DefaultMutableTreeNode) removeNode.getParent();
				if (removeNode != topGameNode) {
					gotoGameNode((GameNode) (removeNode.getPreviousSibling()));
					treeModel.removeNodeFromParent(removeNode);
				}
			}
		});
		treeContextMenu.add(treeRemoveVariationItem);

		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					treeContextMenu.show(
							(Component) e.getSource(),
							e.getX(),
							e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					treeContextMenu.show(
							(Component) e.getSource(),
							e.getX(),
							e.getY());
				}
			}
		});

		final TreeCellRenderer defaultRenderer = tree.getCellRenderer();
		if (defaultRenderer instanceof DefaultTreeCellRenderer) {
			//            ((DefaultTreeCellRenderer) defaultRenderer).setLeafIcon(null);
			tree.setRowHeight(16);
		}

		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getViewport().add(tree);

	 }
	 /**
	  * @return Wheter this game was changed since the last load or clear.
	  */
	 public boolean isChanged() {
		 return isChanged;
	 }

}
