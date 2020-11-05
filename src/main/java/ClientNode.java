import java.io.*;
import java.net.*;

public class ClientNode {
  private Integer nodeId;
  private Integer port;
  private Socket socket;

  public ClientNode(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setPort() {
    this.port = 6000 + this.nodeId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public Integer getPort() {
    return port;
  }

  public void establishServer() throws IOException {
    ServerSocket serverSocket = new ServerSocket(this.port);
    this.socket = serverSocket.accept();
  }

  public void closeServer() throws IOException {
    this.socket.close();
  }

  public void sendMessageToServer(String message, Integer serverPort) throws IOException{
    InetAddress local = InetAddress.getLocalHost();
    System.out.println("LocalHost: " + local);
    Socket clientSocket = new Socket(local, serverPort);
    DataOutputStream dous = new DataOutputStream(clientSocket.getOutputStream());
    dous.writeUTF(message);
    dous.flush();
    dous.close();

  }

  public Boolean pingServer() {
    while (true) {

    }

  }
}
