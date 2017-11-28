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
    int bytesRead;
    int current = 0;
    Socket clientSocket = new Socket("192.168.1.4", 25565);
    System.out.println("-Client Connected-");
    InputStream in = clientSocket.getInputStream();
    DataInputStream serverReply = new DataInputStream(in);
    int count = serverReply.readInt();
    System.out.println(serverReply.readUTF());
    while(count>0){
        System.out.println(serverReply.readUTF());
        count--;
    }
    System.out.println("Enter desired filename(ex. 'FinalStudyGuide.docx'): ");
    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
    String fileName = inFromClient.readLine();
    OutputStream out = clientSocket.getOutputStream();
    DataOutputStream fileRequest = new DataOutputStream(out);
    fileRequest.writeUTF(fileName);
    String returned = serverReply.readUTF();
    System.out.println(returned);
    if(returned.equals(fileName)){
        long size = serverReply.readLong();
        byte[] buffer = new byte[1024];
        OutputStream output = new FileOutputStream(returned);
        while (size>0 && (bytesRead=serverReply.read(buffer, 0, (int)Math.min(buffer.length, size)))!=-1){
          output.write(buffer, 0, bytesRead);
          size-=bytesRead;
        }
    }
    else{
        serverReply.readUTF();
    }
  }
}
