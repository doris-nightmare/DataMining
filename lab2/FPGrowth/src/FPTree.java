import java.util.*;
import java.util.Map.Entry;

public class FPTree {
	
	private TreeNode root;
	private HashMap<String,TreeNode> headerTable;
	
	public boolean isRoot(TreeNode node){
		if(node != root){
			return false;
		}
		return true;
	}
	
	
	public FPTree(){
		root = new TreeNode("null");
		headerTable = new LinkedHashMap<String,TreeNode>();		
	}
	

	public TreeNode getRoot() {
		return this.root;
	}
	
	public HashMap<String,TreeNode> getHeaderTable(){
		return headerTable;
	}

	public void print(){
		//Breadth first search
		Queue<TreeNode> q = new ArrayDeque<TreeNode>();	
		printNode(root,q);
	}

	private void printNode(TreeNode node, Queue<TreeNode> q) {
		System.out.println(node.getItemId()+": "+node.getFrequency());
		q.addAll(node.getSuccessors());
		while(!q.isEmpty()){
			TreeNode current = q.poll();
			printNode(current,q);
			
		}	
	}
	
	

}
