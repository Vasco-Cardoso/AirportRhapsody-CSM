package serverSide.sharedRegions;

import clientSide.ClientAirport;
import comInf.Luggages;
import comInf.Message;
import comInf.MessageException;
import mainProgram.Airport;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ArrivalLounge{
    // Variables
    private int numPassengers;
    Queue<Luggages> luggagesQueue;

    // Locks and conditions
    ReentrantLock lock = new ReentrantLock();
    Condition lastPassenger = lock.newCondition();


    public ArrivalLounge()
    {
        this.numPassengers = 0;
        this.luggagesQueue = new LinkedList<>();
    }

    /**
     * Method used by the passengers to add the luggage to the Queue of the Arrival
     * Lounge.
     * @param lug is a luggage given by the passenger
     * */
    public void depositLuggage(Luggages lug)
    {
        lock.lock();
        try
        {
            this.luggagesQueue.add(lug);
        }
        catch (Exception e)
        {
            System.out.println(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method called by the porter, used to remove and return a piece of luggage
     * from the LuggagesQueue on the Arrival lounge, that will latter be sent to
     * either the store room or the belt conveyor
     * @return returns the piece of luggage removed from the queue
     * */
    public Luggages tryToCollectABag(){
        Luggages l = null;

        lock.lock();
        try
        {
            if (luggagesQueue.size() > 0)
            {
                l = luggagesQueue.peek();
                luggagesQueue.remove(l);
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

        return l;
    }

    /**
     * Disembarks passenger from the plane to the Arrival lounge. The last passenger signals
     * The last passenger signals the porter to wake up and proceed with his life.
     * */
    public void disembarkPassenger() {
        lock.lock();
        try {
            this.numPassengers++;
            // If he is the last one leaving the plane it signals the porter
            if (ClientAirport.nPassengers == this.numPassengers)
            {
                lastPassenger.signal();
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
    }

    /**
     * Method that stales the porter while the passengers are leaving the plane.
     * Gets woken but by the last passenger disembarked.
     */
    public void takeARest()
    {
        while(this.numPassengers != ClientAirport.nPassengers){
            lock.lock();
            try
            {
                lastPassenger.await();

                // This is the end of life check, if the flights are all done the porter gets waken
                // and his life ends.
                if(ClientAirport.airplanesDone){
                    break;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                lock.unlock();
            }
        }
        this.numPassengers = 0;
    }

    public int getnPassengers() {
        return this.numPassengers;
    }

    /**
     * Method called in the end of all the flights to free the porter from his TakeARest state.
     */
    public void signalEnd()
    {
        lock.lock();
        try
        {
            lastPassenger.signalAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;                           // mensagem de resposta
        Luggages l = null;

        switch (inMessage.getType ()){

            case Message.DL:
                l = inMessage.getLuggage();
                depositLuggage(l);
                outMessage = new Message (Message.ACK);       // gerar resposta
            break;

            case Message.TTCB:
                l = tryToCollectABag();
                outMessage = new Message (Message.ACK, l);
                break;

            case Message.DP:
                disembarkPassenger();
                outMessage = new Message (Message.ACK);
                break;

            case Message.TR:
                takeARest();
                outMessage = new Message (Message.ACK);
                break;

            case Message.SE:
                signalEnd();
                outMessage = new Message (Message.ACK);
                break;
        }

        return outMessage;
    }
}
