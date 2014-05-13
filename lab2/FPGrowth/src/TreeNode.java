import java.util.*;


class TreeNode{
		private String itemId;
		private int frequency;
		
		private TreeNode nodeLink;
		
		private TreeNode ancestor;
		private ArrayList<TreeNode> successors;
		
		public TreeNode(String itemId){
			this.itemId = itemId;
			this.frequency=0;
			this.ancestor = null;
			successors = new ArrayList<TreeNode>();
		}
		
		public TreeNode(String itemId, TreeNode ancestor){
			this.itemId = itemId;
			this.frequency=0;
			this.ancestor = ancestor;
			successors = new ArrayList<TreeNode>();
		}
		
		public TreeNode getSuccessor(String itemId){
			TreeNode successor = retrieveSuccessor(itemId);
			if(successor ==null){
				successor =new TreeNode(itemId,this);
				this.successors.add(successor);				
			}
			return successor;
		}
		
		private TreeNode retrieveSuccessor(String itemId){
			for(TreeNode successor : successors){
				if(successor.equals(itemId)){
					return successor;
				}
			}
			return null;
		}
		
		public String toString(){
			return "("+itemId+": "+frequency+") ";
		}
		
		@Override
		public boolean equals(Object o){
			if(o.equals(itemId)){
				return true;
			}
			return false;
		}

		public void addFrequency() {
			this.frequency++;
			
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public int getFrequency() {
			return frequency;
		}

		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}

		public TreeNode getAncestor() {
			return ancestor;
		}

		public void setAncestor(TreeNode ancestor) {
			this.ancestor = ancestor;
		}

		public TreeNode getNodeLink() {
			return this.nodeLink;
		}

		public void setNodeLink(TreeNode successor) {
			this.nodeLink = successor;
			
		}

		public ArrayList<TreeNode> getSuccessors() {
			return successors;
		}
		
	}