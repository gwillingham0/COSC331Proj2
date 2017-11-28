package cosc331proj2;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Garrett
 */
public class FileServer {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) throws Exception{
       boolean connect = true;
       System.out.println("-Server Online-\n");
       ServerSocket serverSocket = new ServerSocket(25565);//Establishes server socket on port 25565 to send files
       List<String> fileList = new ArrayList<String>();
       File[] files = new File("C:/Users/Garrett/Documents/GitHub/COSC331Proj2/fileRepository").listFiles();
       System.out.println("Available Files: ");
       for (File file : files){
         if(file.isFile()){
           fileList.add(file.getName());
           System.out.println(file.getName());
         }
       }
       while(true) {
         Socket clientSocket = serverSocket.accept();//Establishes the connection with the client

         InputStream in = clientSocket.getInputStream();//opens an input stream to receive the file name over
         DataInputStream clientData = new DataInputStream(in);//specifies a DataInputStream to receive the file name from the client
         String fileName = clientData.readUTF();//saves the fileName so the server can send the file data later

         //opening the OutputStreams the server will send the file data over
         OutputStream serverReply = clientSocket.getOutputStream();
         DataOutputStream reply = new DataOutputStream(serverReply);

         if(fileList.contains(fileName)){//checks for the file
           //locates the file
           String filePath = "C:/Users/Garrett/Documents/GitHub/COSC331Proj2/fileRepository/" + fileName;
           File requestedFile = new File(filePath);
           byte[] byteArray = new byte[(int) requestedFile.length()];
           //reads in the file data
           FileInputStream fis = new FileInputStream(requestedFile);
           BufferedInputStream bis = new BufferedInputStream(fis);
           DataInputStream dis = new DataInputStream(bis);
           dis.readFully(byteArray, 0, byteArray.length);
           //sends fileName to confirm then sends the file data
           reply.writeUTF(fileName);
           reply.writeLong(byteArray.length);
           reply.write(byteArray, 0, byteArray.length);
           reply.flush();
           //closing streams
           bis.close();
           fis.close();
         }
         else{//returns an error to the user if the file isn't found
           reply.writeChars("Error: File Not Found");
           reply.flush();
         }
         //closing streams
         clientSocket.close();
         in.close();
         serverReply.close();
       }
     }
}
