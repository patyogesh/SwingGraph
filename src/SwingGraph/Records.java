package SwingGraph;
//import java.applet.Applet;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JLabel;

/**************************************************
 * 
 * @author yogesh
 * This file contains class and methods for patient
 * info and reading/writing it to file.
 * Driver for this is at the bottom which should be
 * called from appropriate places
 **************************************************/

class SerializablePatient implements Serializable {
    String 	name;
    String 	ID;
    int 	breathHoldTime;
    int     testNumber;

    SerializablePatient(String name, String ID, int bht, int testNumber) {
        this.name = name;
        this.ID = ID;
        this.breathHoldTime = bht;
        this.testNumber = testNumber;
    }

    public void showDetails() {
        System.out.println("Name   : " + name);
        System.out.println("MRN : " + ID);
        System.out.println("Breath Hold Time : " + breathHoldTime);
        System.out.println("Test Number : " + testNumber);
        System.out.println("--------------------------");
    }
}

class ObjectSerializationDemo extends TestApp{
    String 	name;
    String 	ID;
    int 	breathHoldTime;
    int     testNumber;
    
    void setPatName(String n) {
    	name = n;
    }
    String getPatName() {
    	return name;
    }
    
    void setPatID(String pid) {
    	ID = pid;
    }
    String getPatID() {
    	return ID;
    }
    
    void setBreathHoldTime(int bht) {
    	breathHoldTime = bht;
    }
    int getBreathHoldTime() {
    	return breathHoldTime;
    }
    
    void setTestID(int tid) {
    	testNumber = tid;
    }
    int getTestID() {
    	return testNumber;
    }
    
    void writeData(String name, String patientID, int breathHoldTime, int testNumber) {
        try {
        	Path dirPath = FileSystems.getDefault().getPath("C:/ABC Files/"+name);
        	if(false == Files.isDirectory(dirPath)) {
        		Files.createDirectory(dirPath);
        	}
        	
            FileOutputStream fileName = new FileOutputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+".txt");       
            
            //sampleLogFileName = new FileOutputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+"_log"+".txt");
            
            try {
            	PrintStream out = new PrintStream(fileName);	
            	//outSampleLogFileName = new PrintStream(sampleLogFileName);
            	
            	out.append(name).append("\t").
            		append(patientID).append("\t").
            		append(String.valueOf(breathHoldTime)).append("\t").
            		append(String.valueOf(testNumber));
            	
            	out.close();
            	fileName.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
   
    
    void readData(String name, int testNumber) {
    	try {
    		try {
    			Path dirPath = FileSystems.getDefault().getPath("C:/ABC Files/"+name);
    			if(false == Files.isDirectory(dirPath)) {
    				System.out.println("Record Does not Exist, Create New one !");
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		System.out.println("Record Exists");
    		FileInputStream in = new FileInputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+".txt");
    		System.out.println("File to Open : " + "C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+".txt");

    		Scanner sc = new Scanner(new BufferedReader(new FileReader("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+".txt")));
    		
    		if(sc.hasNext()) {  			
    			setPatName(sc.next());
    		}
    		if(sc.hasNext()) {
    			setPatID(sc.next());    			
    		}
    		if(sc.hasNext()) {
    			setBreathHoldTime(sc.nextInt());
    		}
    		if(sc.hasNext()) {
    			setTestID(sc.nextInt());
    		}
    		
    		

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

     //Driver is here
/*    public static void main(String[] args) {
        ObjectSerializationDemo impl = new ObjectSerializationDemo();
        impl.writeData("Jo","j123", 30, 1);
        impl.readData("Jo", 1);

    }*/
}