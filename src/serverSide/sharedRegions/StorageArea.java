package serverSide.sharedRegions;

import comInf.Luggages;
import comInf.Message;

import java.util.LinkedList;
import java.util.Queue;

public class StorageArea {
    // Variables
    Queue<Luggages> luggagesQueue;

    public StorageArea()
    {
        luggagesQueue = new LinkedList<>();
    }

    /**
     * Allows to store luggages on the serverSide.sharedRegions.StorageArea
     * @param lug
     */
    public void depositLuggage(Luggages lug){
        this.luggagesQueue.add(lug);
    }

    public int getSize(){
        return this.luggagesQueue.size();
    }

    public void terminate(){

        System.exit(1);
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;

        switch (inMessage.getType()) {

            case Message.DL:
                depositLuggage(inMessage.getLuggage());
                outMessage = new Message(Message.ACK);
                break;

            case Message.GS:
                outMessage = new Message(Message.ACK, getSize());
                break;

            case Message.TERM:
                outMessage = new Message(Message.ACK);
                terminate();
                break;
        }

        return outMessage;
    }
}
