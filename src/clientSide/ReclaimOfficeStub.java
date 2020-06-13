package clientSide;

import comInf.Message;

import java.io.Serializable;

public class ReclaimOfficeStub implements Serializable {

    private String serverHostName = null;

    private int serverPortNumb;

    public ReclaimOfficeStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void reportMissingBags(int n){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        System.out.println("--> STUB REPORT MISSING BAGS");
        outMessage = new Message (Message.RMB, n);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        System.out.println("--> STUB REPORT MISSING BAGS RETURN");

        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public int getnLuggagesLost() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.GNLL);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();

        return inMessage.getN();
    }
}
