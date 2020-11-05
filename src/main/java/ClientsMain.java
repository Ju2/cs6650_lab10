public class ClientsMain {
  public static void main(String[] args) throws Exception{
    if (args.length < 2) {
      throw new IllegalArgumentException("No enough arguments in the command line.");
    }
    Integer nodeId  = Integer.parseInt(args[0]);
    Integer numberOfNode = Integer.parseInt(args[1]);
    System.out.println(nodeId + " " + numberOfNode);
    ClientNode clientNode;
    if (nodeId == numberOfNode - 1) {
      clientNode = new ClientNode(nodeId, numberOfNode, true);
    } else {
      clientNode = new ClientNode(nodeId, numberOfNode, false);
    }
    try {
      clientNode.establishServer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
