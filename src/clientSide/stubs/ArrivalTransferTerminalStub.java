package clientSide.stubs;

import clientSide.communications.ClientCom;
import clientSide.entities.Passenger;
import comInf.Message;

import java.io.Serializable;
import java.util.Queue;

public class ArrivalTransferTerminalStub implements Serializable {

    private String serverHostName = null;

    private int serverPortNumb;

    public ArrivalTransferTerminalStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void arrivedTerminal(Passenger p){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (1));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.AT, p);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public void enterTheBus(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (1));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.ETB);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }

    public void announcingBusBoarding(){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (1));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.ABB);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }


    public Queue<Passenger> getSpots() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (1));
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

        return inMessage.getSeats();
    }

    public void clearSpots() {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ())                                  // aguarda ligação
        {
            try {
                Thread.currentThread ().sleep ((long) (1));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.CS);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType () != Message.ACK)
        {
            System.exit (1);
        }
        con.close ();
    }
}
