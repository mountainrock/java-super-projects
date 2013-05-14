package interviewrelated;

public class DataStructures {

	enum SortAlgorithmType {
		QUICK, MERGE, HEAP
	};

	/**
	 * 
	 * @author Sandeep.Maloth
	 * 
	 */
	class Sort {

		int[] sort(int[] a, SortAlgorithmType x) throws Exception
		{
			if (x.equals(SortAlgorithmType.QUICK)) {
				quickSort(a, 0, a.length - 1);
			}
			return a;
		}

		private void pause() throws Exception
		{
			Thread.sleep(20);

		}

		/**
		 * Quick sort. Follows divide and conquer rule. A pivot is defined initially(a median). time complexity : O(nlog n) worst case: O(n^2)
		 * @param a
		 * @param lo0
		 * @param hi0
		 * @throws Exception
		 */
		public void quickSort(int[] a, int lo0, int hi0) throws Exception
		{

			int lo = lo0;
			int hi = hi0;
			pause();
			if (lo >= hi) {
				return;
			}
			int mid = a[(lo + hi) / 2];
			while (lo < hi) {
				while (lo < hi && a[lo] < mid) {
					lo++;
				}
				while (lo < hi && a[hi] > mid) {
					hi--;
				}
				if (lo < hi) {
					int T = a[lo];
					a[lo] = a[hi];
					a[hi] = T;
					lo++;
					hi--;
					pause();
				}
			}
			if (hi < lo) {
				int T = hi;
				hi = lo;
				lo = T;
			}
			quickSort(a, lo0, lo);
			quickSort(a, lo == lo0 ? lo + 1 : lo, hi0);
		}

	}

	/**
	 * Constructs a binary tree.
	 * 
	 * @author Sandeep.Maloth
	 * 
	 */
	class MyBinaryTree {
		TNode rootNode = null;

		public void insert(int newValue)
		{
			if (rootNode == null) {
				rootNode = new TNode(newValue);
				return;
			}
			insert(rootNode, newValue);
		}

		private void insert(TNode node, int newValue)
		{
			int nodeValue = Integer.parseInt(node.getValue().toString());
			if (newValue < nodeValue) {
				if (node.left != null) {
					insert(node.left, newValue);
				} else {
					System.out.println("  Inserted " + newValue + " to left of " + node.value);
					node.left = new TNode(newValue);
				}
			} else if (newValue > nodeValue) {
				if (node.right != null) {
					insert(node.right, newValue);
				} else {
					System.out.println("  Inserted " + newValue + " to right of " + node.value);
					node.right = new TNode(newValue);
				}
			}
		}

		private void traverse()
		{
			traverse(rootNode);
		}

		private void traverse(TNode node)
		{// in order - left data Right
			// ....depth first search
			if (node != null) {
				traverse(node.getLeft());

				System.out.println(node);

				traverse(node.getRight());
			}

		}

		private void search(Object value)
		{
			// do a binary search.

		}
	}

	class MyLinearLinkedList {
		Node rootNode = null;

		public void add(Object value)
		{
			Node newNode = new Node(value);
			if (rootNode == null) {
				rootNode = newNode;
				return;
			} else {

				Node tempNode = rootNode;
				while (tempNode.getNext() != null)// traverse till the
				// last node is reached.
				{
					tempNode = tempNode.getNext();
				}
				tempNode.setNext(newNode);// insert at the last position.

			}

		}

		public void insert(Object value, int position)
		{
			Node newNode = new Node(value);
			if (position == 1) {
				System.out.println("Insert after" + rootNode.toString());
				newNode.setNext(rootNode);
				rootNode = newNode;
				return;
			}
			Node tempNode = rootNode;
			int i = 1;
			// traverse till the
			// position node is reached.
			while (i < position - 1 && tempNode != null) {
				tempNode = tempNode.getNext();
				i++;
			}
			Node forward = tempNode.getNext();
			// System.out.println("Replace after" + tempNode.toString());
			newNode.setNext(forward);
			tempNode.setNext(newNode);

		}

		@Override
		public String toString()
		{
			Node tempNode = rootNode;
			StringBuffer buffer = new StringBuffer("[");

			while (tempNode != null) {
				buffer.append(tempNode.getValue()).append(", ");
				tempNode = tempNode.getNext();
			}
			buffer.append("]");
			return buffer.toString();
		}
	}

	class TNode extends BaseNode {
		TNode left;

		TNode right;

		public TNode(Object value) {
			this.value = value;
		}

		public TNode getLeft()
		{
			return left;
		}

		public void setLeft(TNode left)
		{
			this.left = left;
		}

		public TNode getRight()
		{
			return right;
		}

		public void setRight(TNode right)
		{
			this.right = right;
		}

	}

	class Node extends BaseNode {

		Node next;

		public Node(Object value) {
			this.value = value;

		}

		public Node getNext()
		{
			return next;
		}

		public void setNext(Node next)
		{
			this.next = next;
		}

	}

	abstract class BaseNode {
		Object value;

		public Object getValue()
		{
			return value;
		}

		public void setValue(Object value)
		{
			this.value = value;
		}

		@Override
		public String toString()
		{

			return value.toString();
		}

	}
}
