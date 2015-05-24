	//this class represents an Entry, or a row of information for the CSV file

import java.util.Arrays;
import java.util.HashMap;
import java.util.*;
import java.util.Collections;
import java.io.*;
import javax.script.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;


public class Entry {
	//global variables
	static ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	static String y [] = {""};

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
	
	public static void main(String [] args) throws Exception, IOException {
		readFromCSVFile();
		letsCRUD();
	}
	
	/*------------------------------------------------------*/
	
	public static void readFromCSVFile() throws IOException, Exception {
		String fileName = "trains.csv";
		String line;
		BufferedReader bufferedReader = null;
		ArrayList<ArrayList<String>> bigList = new ArrayList<ArrayList<String>>();
		result.clear();
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));
			
			//read in the file
			while ((line = bufferedReader.readLine()) != null) {
				if(line.contains("TRAIN_LINE")) { //the first entry is the header so handled separately
					y = line.split(",");
				}

				else { //convert each line of the CSV to an ArrayList<String>
					ArrayList<String> list = CSVtoArrayList(line);
					if(list.size() > 0) {  //if its not null, i.e. empty entry -> ", , , " then do not add it
						bigList.add(list); //ArrayList of ArrayList<String>, i.e. each element in bigList is an ArrayList
					}
				}
			}
						
			while(bigList.size() > 0) {
				sortByRunNumber(bigList); 
			}
			
			removeDuplicates(result); //removes duplicate entries
			bufferedReader.close();
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
		
		writeToTextFile();
	}
	
	/*------------------------------------------------------*/
	
	public static void writeToTextFile() throws IOException, Exception {
		try {
			HashSet <ArrayList<String>> set = new HashSet<ArrayList<String>>();
			String [] trainData = new String[4];
			String fileName = "output.txt";
			File file = new File(fileName);
				
			FileWriter fw = new FileWriter(file, false);
			fw.write(y[0] + "\t" + y[1] + "\t" + y[2] + "\t" + y[3] + "\n");
			
			// adding the result list to output.txt
			for (int i = 0; i < result.size(); i++) {
				if(!set.contains(result.get(i))) {
					set.add(result.get(i));
					trainData[0] = result.get(i).get(0).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[1] = result.get(i).get(1).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[2] = result.get(i).get(2).toString().replaceAll("[\\[\\]\\,]", "");
					trainData[3] = result.get(i).get(3).toString().replaceAll("[\\[\\]\\,]", "");

					fw.write(trainData[0] + "\t\t");
					fw.write(trainData[1] + "\t\t");
					fw.write(trainData[2] + "\t\t");
					fw.write(trainData[3] + "\n");
				}
			}
			
			fw.close();
 		} 
					
		catch (IOException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.getMessage();
		}
	}
	
	/*------------------------------------------------------*/

	public static void letsCRUD() throws Exception, IOException {
		try{
			String options [] = {"DELETE", "UPDATE", "READ", "CREATE"};
			JFrame frame = new JFrame("Frame");
			int response = JOptionPane.showOptionDialog(null, "Select one of the following to modify data", "Train Entry Application",
			        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
			        null, options, options[options.length-1]);
			
			switch(response) {
				case 3: {
					create();
					break;
				}
					
				case 2: {
					read();
					break;
				}
				
				case 1: {
					update();
					break;
				}
				case 0: {
					delete();
					break;
				}
				
				default: {
					letsCRUD();								}
			}
		}
		
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

/*****************************************************************/

	//CRUD FUNCTIONS
	
	// create() - allows for creation of unique Entry, writes to trains.csv
	// read() - reads from trains.csv
	// update() - allows for updating of selected Entry in trains.csv
	// delete() - deletes selected Entry from trains.csv
	
	//all functions also modify output.txt appropriately, 
	// and keep data sorted by Run Number while also eliminating duplicate data
	
	/*------------------------------------------------------*/
	
	public static void create() throws IOException, Exception {
		System.out.println("==========");
		System.out.println("| CREATE |");
		System.out.println("==========");
		
		//creating a JPanel for user input
		
		String[] options = {"OK"};
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel("Enter a Train Entry: ");
		JTextField txt = new JTextField(25);
		panel.add(lbl);
		panel.add(txt);
		JOptionPane.showOptionDialog(null, panel, "CREATE ENTRY", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		String newEntry = txt.getText().toString(); 
		ArrayList<String> item = CSVtoArrayList(newEntry); //holds the potential new Entry
		String fileName = "trains.csv";
		String line;
		BufferedReader bufferedReader = null;
		ArrayList<ArrayList<String>> bigList = new ArrayList<ArrayList<String>>(); //list of all the entries
		
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));
			
			//read in the file
			while ((line = bufferedReader.readLine()) != null) {
				if(line.contains("TRAIN_LINE")) { //the first entry is the header so handled separately
					y = line.split(",");
				}

				else { //convert each line of the CSV to an ArrayList<String>
					ArrayList<String> list = CSVtoArrayList(line);
						if(item.equals(list)){ //checks if its a duplicate
							JOptionPane.showMessageDialog(new JFrame(), "DUPLICATE ENTRY! Please Try Again", "Dialog", JOptionPane.ERROR_MESSAGE);
							create(); //if duplicate, create another one
						}
						else {
							continue;
						}
				}
			}
		
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
			
		writeToCSV(item); //if success... write to CSV
		readFromCSVFile();
	}
	/*------------------------------------------------------*/
	
	public static void read() throws IOException, Exception {
		
		System.out.println("==========");
		System.out.println("|  READ  |");
		System.out.println("==========");
		StringBuilder sb = new StringBuilder();
		
		//Read File Line By Line from trains.csv
		String fileName = "trains.csv";
		String line;
		JFrame frame= new JFrame();
		BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(fileName));
				while ((line = bufferedReader.readLine()) != null)   {
					String str [] = line.split(",");
					
					if(str.length > 0 && !str[0].equals("TRAIN_LINE")) {
						sb.append(str[0] + "                 " + str[1] + "               " + str[2] + "           " + str[3]);
						sb.append(System.getProperty("line.separator"));
					}
					if(str[0].equals("TRAIN_LINE")) {
						sb.append(str[0] + " | " + str[1] + " | " + str[2] + " | " + str[3]);
						sb.append(System.getProperty("line.separator"));
						sb.append(System.getProperty("line.separator"));
					}
				}
				JOptionPane.showMessageDialog(frame, sb.toString());
				bufferedReader.close();

			} 

			catch (IOException e) {
				e.getMessage();
			}
			
			letsCRUD();
	}
	
	/*------------------------------------------------------*/
	
	public static void update() throws IOException, Exception {
		System.out.println("==========");
		System.out.println("| UPDATE |");
		System.out.println("==========");
		JFrame frame= new JFrame();
		
		String selection = comboBox(); //grabs selection from combo box
	   String updated = JOptionPane.showInputDialog(frame, "Edit Entry", selection); //allows you to update
		ArrayList<String> edit = CSVtoArrayList(updated); 
		deleteFromCSV(selection); //deletes old selection from trains.csv
		writeToCSV(edit); // and writes in the updated selection
	}
	
	/*------------------------------------------------------*/
	
	public static void delete() throws IOException, Exception {
		System.out.println("==========");
		System.out.println("| DELETE |");
		System.out.println("==========");
		JFrame frame = new JFrame();
		String selection = comboBox(); //grabs selection from combo box
		deleteFromCSV(selection); // deletes selection from csv file
		JOptionPane.showMessageDialog(null, selection + " deleted!"); //outputs confirmation
	}

/*****************************************************************/




//###############################################################//

	// ### HELPER FUNCTIONS ###
	
	// comboBox() - reads all items from CSV file for selection, returns selection
	// deleteFromCSV() - takes selection from comboBox and deletes it from CSV file
	// writeToCSV() - writes entry to CSV file trains.csv
	// CSVtoArrayList() - takes each entry from CSV and converts it to ArrayList<String>
	// sortByRunNumber() - takes each entry (format: ArrayList<String>) and sorts based on Run Number
	// indexOf() - finds index of the smallest (dictionary order) Run Number Value
	// removeDuplicates() - removes duplicates from CSV file, and writes to output.txt
	// stringify() - toString() representation of an Entry
	// createEntries() - creation of Entry Objects based on a list of Entries from CSVToArrayList()
	
	/*------------------------------------------------------*/
	
	public static String comboBox() throws IOException, Exception {
		ArrayList<String> updateList = new ArrayList<String>();
		String fileName = "trains.csv";
		String line;
		JFrame frame= new JFrame();
		BufferedReader bufferedReader = null;
		String selection = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(fileName));
				while ((line = bufferedReader.readLine()) != null)   {				
					if(!line.contains("TRAIN_LINE")) {
						updateList.add(line);
					}
				}
				 String [] options = new String[updateList.size()];
					options = updateList.toArray(options);
					
				 //JFrame frame = new JFrame("Input Dialog Example 3");
			    	selection = (String) JOptionPane.showInputDialog(frame, 
			        "Select Entry To Update",
			        "UPDATE",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        options, 
			        options[0]);
			
				bufferedReader.close();
			}
				
			catch(IOException e) {
				e.getMessage();
			}
			
			return selection;		
	}
		
	/*------------------------------------------------------*/
	
	// the only way to delete from the file a specific value is to create a new file ... 
	// ... and write all contents except input string  
	public static void deleteFromCSV(String deleteThis) throws IOException, Exception {
		File inputFile = new File("trains.csv");
		File tempFile = new File("temp.csv");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String lineToRemove = deleteThis;
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.equals(lineToRemove)) {
				continue; // if line == string to delete, skip over it
			 }
		    writer.write(currentLine + System.getProperty("line.separator")); //only write lines != to input string
		}
		writer.close(); 
		reader.close(); 
		
		boolean successful = tempFile.renameTo(inputFile); //rename back to trains.csv so we don't create extra files
	}
	
	/*------------------------------------------------------*/

	public static void writeToCSV(ArrayList<String> entry) throws IOException, Exception {
		try {
			File file = new File("trains.csv");
			 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw); // write entry which is an ArrayList<String> to file
				
			bw.write(entry.get(0) + ", "); 
			bw.write(entry.get(1) + ", ");
			bw.write(entry.get(2) + ", ");
			bw.write(entry.get(3) + "\n");
			
			bw.close();
 		} 
					
		catch (IOException e) {
			e.printStackTrace();
		}
		
		readFromCSVFile();
		letsCRUD();

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
	
// code doesn't work, my intention was to make an ArrayList of Entry objects...
// ... but the objects were getting overwritten
		
	public static void createEntries(ArrayList<ArrayList<String>> bList) {

		/* ArrayList<Entry> outList = new ArrayList<Entry>(); 
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
		
		
		for (int i = 0; i < outList.size(); i++) {
			
			//System.out.println(outList.get(i).stringify());
		
		}
		//System.out.println("OutList Size is: " + outList.size());
		*/
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
		
		result.add(bList.get(index)); //add it to the result list...
		bList.remove(bList.get(index)); //remove it from bList
	}
	
	/*------------------------------------------------------*/
	
	//get index of a particular String in the bigList
	public static int indexOf(ArrayList<ArrayList<String>> aL, String x) {
		int index = 0;
		for (int i = 0; i < aL.size(); i++) {
			if(aL.get(i).get(2).equals(x)){ 
				index = i;
				return index; // the index needed by sortByRunNumber()
			}
		}
		return -1;
	}

	/*------------------------------------------------------*/
	
	//removes duplicate entries based on comparing the i-th and i+1-th index
	//since the arrayList is now sorted it is easier to remove duplicates
	public static void removeDuplicates(ArrayList<ArrayList<String>> aL) {
		for (int i = 0; i < aL.size()-1; i++) {
			if(aL.get(i).containsAll(aL.get(i+1))) { //if two consecutive lists are indentical ...
				aL.remove(aL.get(i+1)); //... remove the second list
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

	//###############################################################//

}