package clientSide.stubs;

import clientSide.communications.ClientCom;
import comInf.Message;

import java.io.Serializable;

/**
 * Stub responsible to send the messages needed for the ReclaimOfficeStub to procede with actions
 */
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
        outMessage = new Message (Message.RMB, n);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

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

    public void terminate(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.TERM);
        con.writeObject (outMessage);
        con.close ();
    }
}
