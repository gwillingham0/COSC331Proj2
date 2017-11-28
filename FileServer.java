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
     public static void main(String[] args) throws IOException{
        int count = 0;
        System.out.println("-Server Online-\n");
        ServerSocket serverSocket = new ServerSocket(25565);//Establishes server socket on port 25565 to send files
        List<String> fileList = new ArrayList<>();
        File[] files = new File("C:/Users/Garrett/Documents/GitHub/COSC331Proj2/fileRepository").listFiles();
        //Lists the files in the fileRepository
        System.out.println("Available Files: ");
        for (File file : files){
          if(file.isFile()){
            fileList.add(file.getName());
            System.out.println(file.getName());
            count++;
          }
        }
        while(true) {
            //Sets up the InputStream
            InputStream in;
            //Starts setting up the OutputStreams the server will send the file data over
            OutputStream serverReply;
            try (Socket clientSocket = serverSocket.accept()) //Establishes the connection with the client
            {
                //Finishes up the OutputStream
                serverReply = clientSocket.getOutputStream();
                DataOutputStream reply = new DataOutputStream(serverReply);
                //Sends a count of how many files there are available then sends the list for the client to choose from
                reply.writeInt(count);
                reply.writeUTF("Available Files: ");
                while(count>0){
                    reply.writeUTF(fileList.get(count-1));
                    count--;
                }
                in = clientSocket.getInputStream(); //opens an input stream to receive the file name over
                DataInputStream clientData = new DataInputStream(in);//specifies a DataInputStream to receive the file name from the client
                String fileName = clientData.readUTF();//saves the fileName so the server can send the file data later
                if (fileList.contains(fileName)) {
                    //checks for the file
                    //locates the file
                    String filePath = "C:/Users/Garrett/Documents/GitHub/COSC331Proj2/fileRepository/" + fileName;
                    File requestedFile = new File(filePath);
                    byte[] byteArray = new byte[(int) requestedFile.length()];
                    try (
                        //reads in the file data
                        FileInputStream fis = new FileInputStream(requestedFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                        DataInputStream dis = new DataInputStream(bis);
                        dis.readFully(byteArray, 0, byteArray.length);
                        //Sends the file name to confirm followed by the file data
                        reply.writeUTF(fileName);
                        reply.writeLong(byteArray.length);
                        reply.write(byteArray, 0, byteArray.length);
                        reply.flush();
                        //closing streams
                        fis.close();
                        dis.close();
                                           }
                } else {
                    //returns an error to the user if the file isn't found
                    reply.writeUTF("Error: File Not Found");
                    reply.flush();
                }
                //closing streams
            } //opens an input stream to receive the file name over
          in.close();
          serverReply.close();
        }
      }
 }
