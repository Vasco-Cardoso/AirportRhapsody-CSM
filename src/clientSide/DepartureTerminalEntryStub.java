package clientSide;

import comInf.Message;

import java.io.Serializable;

public class DepartureTerminalEntryStub implements Serializable {

    private String serverHostName = null;

    private int serverPortNumb;

    public DepartureTerminalEntryStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void arrivedTerminal(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.AT);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public void waitLastPassenger(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.WLP);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public void lastPassenger(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.LP);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }


    public int getNumPassengers() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.GNP);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();

        return inMessage.getN();
    }

    public void setEmpty() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.SE);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }
}
