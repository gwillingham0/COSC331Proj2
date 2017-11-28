package cosc331proj2;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Garrett
 */
public class ClientSide {
  public static void main(String[] args) throws Exception{
    //initializes variables that will be used in writing the file
    int bytesRead;
    int current = 0;
    //Opens the connection with the host computer
    Socket clientSocket = new Socket("192.168.1.4", 25565);
    System.out.println("-Client Connected-");
    //Sets up the InputStream the server will reply on
    InputStream in = clientSocket.getInputStream();
    DataInputStream serverReply = new DataInputStream(in);
    //Receives the total number of files and sets it to count to be used for the loop
    int count = serverReply.readInt();
    //Receives a header saying "Available Files: "
    System.out.println(serverReply.readUTF());
    //Receives the list of available files
    while(count>0){
        System.out.println(serverReply.readUTF());
        count--;
    }
    //Requests the desired file name from the user
    System.out.println("Enter desired filename(ex. 'FinalStudyGuide.docx'): ");
    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
    String fileName = inFromClient.readLine();
    //Opens a DataOutputStream to send the request to the server
    OutputStream out = clientSocket.getOutputStream();
    DataOutputStream fileRequest = new DataOutputStream(out);
    //Sends the request to the server
    fileRequest.writeUTF(fileName);
    //Receives the requested fileName
    String returned = serverReply.readUTF();
    //Prints the filename to the command line so the user knows it is the file they requested
    System.out.println(returned);
    //Checks to make sure that the file name returned by the server matches the one requested
    //Also, serves as error checking as explained below
    if(returned.equals(fileName)){
        //Receives the size of the file from the server
        long size = serverReply.readLong();
        byte[] buffer = new byte[1024];
        //Establishes the filepath so all the saved files are in one place
        File requestedFilePath = new File("C:/Users/Garrett/Documents/GitHub/COSC331Proj2/clientFileRepository/" + returned);
        OutputStream output = new FileOutputStream(requestedFilePath);
        //Loops through and writes the file contents byte by byte to the filepath established earlier
        while (size>0 && (bytesRead=serverReply.read(buffer, 0, (int)Math.min(buffer.length, size)))!=-1){
          output.write(buffer, 0, bytesRead);
          size-=bytesRead;
        }
    }
    else{
        //If the server returned anything but the file name it is printed here
        //In the event of a user requesting a missing file, the server would return an error message to be printed
        System.out.println(serverReply.readUTF());
    }
  }
}
