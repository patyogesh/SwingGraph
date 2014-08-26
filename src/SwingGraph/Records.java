package SwingGraph;
//import java.applet.Applet;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Vector;

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

    SerializablePatient(String name, String ID, /*int bht,*/ int testNumber) {
        this.name = name;
        this.ID = ID;
       // this.breathHoldTime = bht;
    }

    public void showDetails() {
        System.out.println("Name   : " + name);
        System.out.println("MRN : " + ID);
        System.out.println("Breath Hold Time : " + breathHoldTime);
        System.out.println("Test Number : " + testNumber);
        System.out.println("--------------------------");
    }
}

class ObjectSerializationDemo{
    void writeData(String name, String patientID, /* int breathHoldTime, */ int testNumber) {
        SerializablePatient db = new SerializablePatient(name, patientID, /*breathHoldTime, */ testNumber);
        try {
        	Path dirPath = FileSystems.getDefault().getPath("C:/ABC Files/"+name);
        	if(false == Files.isDirectory(dirPath)) {
        		Files.createDirectory(dirPath);
        	}
        	
            //FileOutputStream fileName = new FileOutputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+"txt");       
            
            try {
            	PrintStream out = new PrintStream(new FileOutputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+".txt"));
            	out.append(name).append("\t").
            		append(patientID).append("\t").
            		/* append(String.valueOf(breathHoldTime)).append("\t"). */
            		append(String.valueOf(testNumber));
            } catch (Exception e) {
            	e.printStackTrace();
            }

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
     void readData(String name, int testNumber) {
        try {
            FileInputStream in = new FileInputStream("C:/ABC Files/"+name+"/"+"testNumber_"+testNumber+"txt");
            try {
                ObjectInputStream sin = new ObjectInputStream(in);
                try {
                    SerializablePatient se = (SerializablePatient) sin
                            .readObject();
                    se.showDetails();

                    sin.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

     //Driver is here
//    public static void main(String[] args) {
//        ObjectSerializationDemo impl = new ObjectSerializationDemo();
//        impl.writeData();
//        impl.readData();
//
//    }
}