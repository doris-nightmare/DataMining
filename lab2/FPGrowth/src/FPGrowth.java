import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class FPGrowth {
	
	private List<String> tranList;	
	private int support;	
	private Map<String,Integer> freqPatterns;
	
	
	
	public FPGrowth(int support, List<String> tranList){
		this.support = support;
		this.tranList = tranList;
		freqPatterns = new LinkedHashMap<String,Integer>();
	}
	
	private Map<String,Integer> preprocess(List<String> tranList) {
		Map<String,Integer> fList = new LinkedHashMap<String,Integer>();
		Iterator<String> it = tranList.iterator();
		while(it.hasNext()){
			String transaction = it.next();
			
			String[] items = transaction.split(" ");
			List<String> itemList = new LinkedList<String>();
			for(String item : items){
				itemList.add(item);
			}		
			recursivePreprocess(itemList,fList);
		}
		
		return processFList(fList);
	
	}
	
	private void recursivePreprocess(List<String> itemList, Map<String,Integer> fList){
		if(!itemList.isEmpty()){
			String currentItem = itemList.get(0);
			itemList.remove(0);
			
			if(fList.containsKey(currentItem)){
				int freq = fList.get(currentItem);
				freq++;
				fList.put(currentItem, freq);
			}else{
				fList.put(currentItem, 1);
			}
			recursivePreprocess(itemList, fList);
		}
		
	}
	
	private Map<String,Integer> processFList(Map<String,Integer> fList){
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String,Integer>>(fList.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String, Integer>>(){

			@Override
			public int compare(Entry<String, Integer> e1,
					Entry<String, Integer> e2) {
				// TODO Auto-generated method stub
				return (e2.getValue().compareTo(e1.getValue()));
			}
			
		});
		
		Map<String,Integer> result = new LinkedHashMap<String,Integer>();
		for(Map.Entry<String, Integer> entry : list){
			if(entry.getValue()>=support){
				result.put(entry.getKey(), entry.getValue());
			}			
		}
		return result;
	}
	
	
	
	public FPTree buildTree(List<String> tranList){
		
		//sort the frequent items by descending order and eliminate the items under minsup
		Map<String,Integer> fList = preprocess(tranList);
			
		FPTree fpTree = new FPTree();
		Iterator<String> it = tranList.iterator();
		while(it.hasNext()){
			String transaction = it.next();
			String[] items = transaction.split(" ");
			List<String> itemList = new LinkedList<String>();
			
			//need use the information from fList
			
			Iterator<String> itList = fList.keySet().iterator();
			while(itList.hasNext()){
				String currentFreqItem = itList.next();
				
				for(String itemTran : items){
					if(itemTran.equals(currentFreqItem)){
						itemList.add(itemTran);
					}
				}
			}
							
			//build fp tree
			insert_tree(itemList,fpTree.getRoot(),fpTree.getHeaderTable());
		}
		return fpTree;
		
	}
	
	private void insert_tree(List<String> itemList, 
			TreeNode currentRoot,
			HashMap<String,TreeNode> headerTable){
		if(!itemList.isEmpty()){
			String currentItem = itemList.get(0);
			itemList.remove(0);
			
			TreeNode successor = currentRoot.getSuccessor(currentItem);
			successor.addFrequency();
			
			if(headerTable.containsKey(currentItem) && successor.getFrequency()==1){
				//if frequency >1, do nothing - old node
				//find the last element reached from header table
				//add currentNode to the tail of the linked list
				TreeNode itemHeader = headerTable.get(currentItem);
				TreeNode itemTail = itemHeader;
				while(itemTail.getNodeLink()!=null){
					itemTail = itemTail.getNodeLink();
				}
				//find the tail of the link list
				itemTail.setNodeLink(successor);
				
			}else if(!headerTable.containsKey(currentItem)){
				headerTable.put(successor.getItemId(), successor);
			}
							
			insert_tree(itemList,successor,headerTable);
		}
	}
	
	public void mine(){
					
		//construct the initial fp tree
		FPTree fpTree= buildTree(tranList);
		
		fpGrowth(fpTree,"");
		
		
		//pretty-print frequent patterns
		System.out.println(freqPatterns);
		
	}
	
	private void fpGrowth(FPTree fpTree, String prefix){
		List<TreeNode> headerList = new LinkedList<TreeNode>();
		
		Iterator<Map.Entry<String, TreeNode>> itHeaderTable = 
				fpTree.getHeaderTable().entrySet().iterator();
		while(itHeaderTable.hasNext()){			
			headerList.add(itHeaderTable.next().getValue());
		}
		
		Collections.reverse(headerList);
		//reverse the list just in favor of the traversal
		
		for(TreeNode node : headerList){
			//mining frequent patterns from each node
			int freqNode = 0;
			TreeNode currentNode = node;
			List<String> condTranList = new ArrayList<String>();
			//consider the prefix thing
			
			
			while(currentNode!=null){
				freqNode += currentNode.getFrequency();
				//if the frequency is greater than 1, copy the transaction twice
				//just to leverage the already written insert_tree method
				String condTran = "";
				TreeNode parent = currentNode.getAncestor();
				while(parent!=null){
					
					if(!fpTree.isRoot(parent)){
						condTran +=(parent.getItemId() +" ");					
					}
					parent = parent.getAncestor();
					
					
				}
				for(int i=0; i< currentNode.getFrequency();i++){
					if(condTran!=""){
						condTranList.add(condTran);
					}
					
				}
				currentNode = currentNode.getNodeLink();
			}
			
			if(freqNode >= support){
				if(prefix==""){
					freqPatterns.put(prefix+node.getItemId(), freqNode);
				}else{
					freqPatterns.put(prefix+","+node.getItemId(), freqNode);
				}
				
				//construct conditional fp tree
				FPTree condFpTree = buildTree(condTranList);
				condFpTree.print();
				fpGrowth(condFpTree,prefix + node.getItemId());
			}		
			
		}
		
		
	}
	
	
}
