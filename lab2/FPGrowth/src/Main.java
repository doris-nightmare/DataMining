import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args){
		File input = new File("tran1000");
		
		List<String> tranList = new ArrayList<String>();
		String transaction = null;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
			
			
			while((transaction = br.readLine() )!= null){
				tranList.add(transaction);
				
			}
			
			FPGrowth  fpGrowth = new FPGrowth(180,tranList);	
			
			
			//mine frequent patterns with fp tree
			fpGrowth.mine();
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	int i=0;
}
