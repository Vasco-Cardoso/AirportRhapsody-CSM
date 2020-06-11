package serverSide;

import comInf.Luggages;
import clientSide.Passenger;
import comInf.Message;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LuggageCollectionPoint{

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition porterarrived = lock.newCondition();

    // Variables
    Queue<Luggages> luggagesQueue;
    private boolean morebags = true;

    // Constructor
    public LuggageCollectionPoint(){
        this.luggagesQueue = new LinkedList<>();
    }

    /**
     * Method that deposits a luggage on the luggagesQueue (bel_conveyor) to get picked
     * by a passenger. Gets called while there are bags to collect otherwise leaves.
     * @param lug
     */
    public void depositLuggage(Luggages lug){
        lock.lock();
        try{
            morebags = true;

            this.luggagesQueue.add(lug);

            // Signals passengers that a bag was deposited and they can check of its theirs.
            porterarrived.signalAll();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * Method that gets called by a clientSide.Passenger to go collect a bag, that only
     * happens if there is any bag on the conveyor that matches its ID
     * @param p Its the passenger we use to compare to the comInf.Luggages
     * @return the number of bags that a passenger was able to collect
     */
    public int goCollectABag(Passenger p) {
        int i = 0;
        lock.lock();
        try {
            //  Awaits for a bag to be deposited on the belt conveyor
            porterarrived.await();

            // While there are bags to collect the passengers try to do it.
            while (morebags)
            {
                for (Luggages l : this.luggagesQueue)
                {
                    int aux = l.getPassenger().getPId();
                    if (aux == p.getPId())
                    {
                        this.luggagesQueue.remove(l);
                        i++;
                    }
                }

                // If the passenger ends up collectiong every bag he leaves.
                if(i == p.getNum_bags())
                {
                    return i;
                }

                // Waits for more bags to collect.
                porterarrived.await();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            lock.unlock();
        }
        // Only passengers that couldnt collect all their bags get to this stage, and so they return
        // the number of bags they could so they can latter notice the reclaim office.

        return i;
    }

    /**
     * Method that flags that there are no more bags to colect and
     * signals every passenger of that.
     */
    public void noMoreBagsToCollect() {
        lock.lock();
        try
        {
            morebags = false;
            porterarrived.signalAll();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            lock.unlock();
        }
    }

    public int getSize()
    {
        return this.luggagesQueue.size();
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;

        switch (inMessage.getType()) {

            case Message.DL:
                depositLuggage(inMessage.getLuggage());
                outMessage = new Message(Message.ACK);
                break;

            case Message.GCAB:
                int nbags = goCollectABag(inMessage.getPass());
                outMessage = new Message(Message.ACK, nbags);
                break;

            case Message.NMBTC:
                noMoreBagsToCollect();
                outMessage = new Message(Message.ACK);
                break;

            case Message.GS:
                outMessage = new Message(Message.ACK, getSize());
                break;
        }

        return outMessage;
    }
}
