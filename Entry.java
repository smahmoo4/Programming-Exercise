//this class represents an Entry, or a row of information for the CSV file

import java.util.Arrays;
import java.lang.Comparable; 
import java.util.Comparator; 
import java.util.HashMap;
import java.util.*;
import java.util.Collections;
import java.io.*;
import javax.script.*;

public class Entry {
	
	static ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	
	// variables for one row of the CSV file
	private static String trainLine;
	private static String routeName;
	private static String runNumber;
	private static String operatorID;
	
	//Constructor
	public Entry(String tLine, String rName, String rNumber, String opID) {
		this.trainLine = tLine;
		this.routeName = rName; 
		this.runNumber = rNumber;
		this.operatorID = opID; 
	}
	
/*------------------------------------------------------*/
	//GETTERS
	
	public static String getTrainLine() {
		return trainLine;
	}
	
	public static String getRouteName() {
		return routeName;
	}
	
	public static String getRunNumber() {
		return runNumber;
	}
	
	public static String getOperatorID() {
		return operatorID;
	}
	
	/*------------------------------------------------------*/
	
	//SETTERS
	
	public static void setTrainLine(String tLine) {
		trainLine = tLine;
	}
	
	public static void setRouteName(String rName) {
		routeName = rName;
	}
	
	public static void setRunNumber(String rNumber) {
		runNumber = rNumber;
	}
	
	public static void setOperatorID(String opID) {
		operatorID = opID;
	}

/*------------------------------------------------------*/
	/* public void sortByRunNumber(Entry [] entries) {
		Arrays.sort(entries);
	} */ 

/*------------------------------------------------------*/
	
	/* public int compareTo(Entry compareEntry) {
 
		String compareRunNumber = compareEntry.getRunNumber(); 
		
		return this.runNumber - compareRunNumber;
	} 
	
	public static Comparator<Entry> EntryComparator = new Comparator<Entry>() {
	 	
		public int compare(Entry entry1, Entry entry2) {
	 
	      String entryName1 = entry1.getRunNumber();
	      String entryName2 = entry2.getRunNumber();
 
	      //ascending order
	      return entryName1.compareTo(entryName2);
 
	      //descending order
	      //return entryName2.compareTo(entryName1);
		}
	}; */
	
   	
	/*------------------------------------------------------*/
	
	public static void main(String [] args) throws IOException, ScriptException {
		String fileName = "trains.csv";
		String line;
		BufferedReader bufferedReader = null;
		ArrayList<ArrayList<String>> bigList = new ArrayList<ArrayList<String>>();
		String y [] = {""};
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));
			
			//read in the file
			while ((line = bufferedReader.readLine()) != null) {
				if(line.contains("TRAIN_LINE")) { //the first entry is the header so handled separately
					y = line.split(",");
					System.out.println("\t"+ y[0] + "\t" + y[1] + "\t" + y[2] + "\t" + y[3]);
				}

				else { //convert each line of the CSV to an ArrayList<String>
					ArrayList<String> list = CSVtoArrayList(line);
					if(list.size() > 0) {  //if its not null, i.e. empty entry -> ", , , " then do not add it
						bigList.add(list); //ArrayList of ArrayList<String>, i.e. each element in bigList is an ArrayList
					}
				}
			}
			
			createEntries(bigList);
			
			while(bigList.size() > 0) {
				sortByRunNumber(bigList); 
			}
			
			removeDuplicates(result); //removes duplicate entries
			displayResult(result); //displays result to console
		}
		
		catch (IOException e) {
			e.printStackTrace();
		} 
		
		finally {
			try {
				if (bufferedReader != null) 
					bufferedReader.close();
			} 
			
			catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		
		//write to file output.csv
			try {
				String [] trainData = new String[4];
				File file = new File("output.txt");
		 
				if (file.exists()) {
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(y[0] + "\t" + y[1] + "\t" + y[2] + "\t" + y[3] + "\n");
				
				// adding the result list to output.txt
				for (int i = 0; i < result.size(); i++) {
					trainData[0] = result.get(i).get(0).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[1] = result.get(i).get(1).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[2] = result.get(i).get(2).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[3] = result.get(i).get(3).toString().replaceAll("[\\[\\]\\,]", "");
					//bw.write(Arrays.toString(trainData) + "\n");
					
					bw.write(trainData[0] + "\t\t");
					bw.write(trainData[1] + "\t\t");
					bw.write(trainData[2] + "\t\t");
					bw.write(trainData[3] + "\n");
				}
				
				bw.close();
	 		 
			} 
			
			catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/*------------------------------------------------------*/

	
	public static ArrayList<String> CSVtoArrayList(String csv) {
		ArrayList<String> result = new ArrayList<String>();
		//transforming each line of the csv file to an ArrayList<String>
		if (csv != null) {
			String[] splitData = csv.split("\\s*,\\s*");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					result.add(splitData[i].trim());
				}
			}
		}
		
		return result;
	}
	
	/*------------------------------------------------------*/
	
	public static void createEntries(ArrayList<ArrayList<String>> bList){
		
		//This Code didn't work, my intention was to make an ArrayList of Entry objects but the objects were getting overwritten

		ArrayList<Entry> outList = new ArrayList<Entry>(); 
		for (int x = 0; x < bList.size(); x++) {
			ArrayList<String> list = new ArrayList<String>(); 
			list = bList.get(x);
			String trainLine = list.get(0);
			String routeName = list.get(1);
			String runNumber = list.get(2);
			String operatorID =list.get(3);
			Entry entry = new Entry(trainLine, routeName, runNumber, operatorID);
			outList.add(entry);
			
			//System.out.println("E" + x + ": " + outList.get(x).stringify());
		}
		
		
		/*for (int i = 0; i < outList.size(); i++) {
			
			//System.out.println(outList.get(i).stringify());
		
		} */ 		
		//System.out.println("OutList Size is: " + outList.size());
	}
	
	/*------------------------------------------------------*/

	public static void sortByRunNumber(ArrayList<ArrayList<String>> bList) {
		String minNumber = bList.get(0).get(2);
		int index = 0; 
		// get third element from inner list from each index of bList (the Run Number)
		for(int i = 1; i < bList.size(); i++) {
			if ((minNumber).compareTo(bList.get(i).get(2)) > 0) {
				minNumber = bList.get(i).get(2); //get the minimum number
			}
		}
		//find index of minimum Run Number in bList through helper function
		index = indexOf(bList, minNumber);
		//System.out.println("minNumber is: " + minNumber +", located at index: " + index);
		
		result.add(bList.get(index)); //add it to the result list...
		bList.remove(bList.get(index)); //remove it from bList
	}
	
	/*------------------------------------------------------*/
	//helper function to get index of a particular String in the bigList
	public static int indexOf(ArrayList<ArrayList<String>> aL, String x) {
		int index = 0;
		for (int i = 0; i < aL.size(); i++) {
			if(aL.get(i).get(2).equals(x)){
				index = i;
				return index;
			}
		}
		return -1;
	}
	
	/*------------------------------------------------------*/
	//prints the resulting train entry to console
	public static void displayResult(ArrayList<ArrayList<String>> aL){
		for(int i = 1; i < aL.size(); i++) {
			System.out.println("[Entry " + i + "]: " + result.get(i));
		}
	}
	 
	/*------------------------------------------------------*/
	//removes duplicate entries based on comparing the i-th and i+1-th index
	//since the arrayList is now sorted it is easier to remove duplicates
	public static void removeDuplicates(ArrayList<ArrayList<String>> aL) {
		for (int i = 0; i < aL.size()-1; i++) {
			if(aL.get(i).containsAll(aL.get(i+1))){
				aL.remove(aL.get(i+1));
			}
		}
	}
	
	/*------------------------------------------------------*/

	// Entry's toString() method
	public static String stringify() {
		String str = trainLine + "\t\t" + routeName + "\t\t" + runNumber + " " + operatorID;
			return str;
	}
	
	/*------------------------------------------------------*/

}