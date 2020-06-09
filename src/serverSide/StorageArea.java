package serverSide;

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
     * Allows to store luggages on the serverSide.StorageArea
     * @param lug
     */
    public void depositLuggage(Luggages lug){
        this.luggagesQueue.add(lug);
    }

    public int getSize(){
        return this.luggagesQueue.size();
    }


    public Message processAndReply(Message inMessage) {
    }
}
