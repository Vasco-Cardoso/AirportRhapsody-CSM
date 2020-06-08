package sharedRegions;

import commonInfrastructures.Luggages;

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
     * Allows to store luggages on the sharedRegions.StorageArea
     * @param lug
     */
    public void depositLuggage(Luggages lug){
        this.luggagesQueue.add(lug);
    }

    public int getSize(){
        return this.luggagesQueue.size();
    }


}
