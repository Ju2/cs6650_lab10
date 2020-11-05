import java.io.*;
import java.net.*;

public class ClientNode {
  private Integer nodeId;
  private Integer port;
  private Socket socket;
  private Integer numberOfNode;
  private Boolean isLeader;

  public ClientNode(Integer nodeId, Integer numberOfNode, Boolean isLeader) {
    this.nodeId = nodeId;
    this.numberOfNode = numberOfNode;
    this.isLeader = isLeader;
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

  public void establishServer() throws Exception {
    this.setPort();
    System.out.println("The establish server is called");
    ServerSocket serverSocket = new ServerSocket(this.port);
    Long start = System.currentTimeMillis();
    while (true) {
      Long latency = System.currentTimeMillis() - start;
      System.out.println("latency: " + latency);
      if (latency >= 5000) {
        start = System.currentTimeMillis();
        if (isLeader) {
          System.out.println(this.nodeId + ": I am Leader");
        } else {
          System.out.println(this.nodeId + ": I am the follower");
        }
      }
      this.socket = serverSocket.accept();
      Boolean isAlive = this.pingServer(this.numberOfNode - 1 + 6000);
      System.out.println("isAlive is: " + isAlive);
      if (!isAlive) {
        this.election();
      }
      ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
      String message  = (String) objectInputStream.readObject();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
      objectOutputStream.writeObject("Acknowledge");
      objectInputStream.close();
      objectOutputStream.close();
      this.socket.close();
      if (message.equalsIgnoreCase("exit")) {
        break;
      }
    }
    serverSocket.close();
  }

  public void closeServer() throws IOException {
    System.out.println("Shutting down the server!");
    this.socket.close();
  }

  public String sendMessageToAnotherServer(String message, Integer serverPort) throws Exception{
    InetAddress local = InetAddress.getLocalHost();
    System.out.println("LocalHost: " + local);
    Socket clientSocket = new Socket(local, serverPort);
    Boolean result = this.pingServer(serverPort);
    if (!result) {
      return null;
    }
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
    objectOutputStream.writeObject(message);
    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    String responseMessage = (String)objectInputStream.readObject();
    objectOutputStream.close();
    objectInputStream.close();
    return responseMessage;
  }

  public Boolean pingServer(Integer serverPort) throws IOException{
    InetAddress local = InetAddress.getLocalHost();
    Socket clientSocket = new Socket(local, serverPort);
    Boolean result = clientSocket.getInetAddress().isReachable(5000);
    clientSocket.close();
    return result;
  }

  public void election () {
    System.out.println(nodeId + " : started election");
    Boolean isSelected = true;
    for (int i = nodeId + 1; i < numberOfNode; i ++) {
      try {
        String tempResult = this.sendMessageToAnotherServer("ELECTION", 6000 + i);
        if (tempResult != null) {
          isSelected = false;
          System.out.println("Reply from node " + i + ", returning to Follower");
          break;
        }
      } catch (Exception exception) {
        continue;
      }
    }

    if (isSelected) {
      this.isLeader = true;
    }
  }
}
