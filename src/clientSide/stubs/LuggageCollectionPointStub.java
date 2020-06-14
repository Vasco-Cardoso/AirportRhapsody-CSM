package clientSide.stubs;

import clientSide.communications.ClientCom;
import clientSide.entities.Passenger;
import comInf.Luggages;
import comInf.Message;

import java.io.Serializable;

/**
 * Stub responsible to send the messages needed for the LuggageCollectionPointStub to procede with actions
 */
public class LuggageCollectionPointStub implements Serializable {

    private String serverHostName = null;

    private int serverPortNumb;

    public LuggageCollectionPointStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void depositLuggage(Luggages l){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.DL, l);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public int goCollectABag(Passenger p){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.GCAB, p);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();

        return inMessage.getN();
    }

    public void noMoreBagsToCollect(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.NMBTC);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public int getSize() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.GS);
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
