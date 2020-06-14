package serverSide.sharedRegions;

import comInf.Message;

import java.util.concurrent.locks.ReentrantLock;

public class ReclaimOffice {
    // Variables
    private int nReclaims;
    private int nLuggagesLost;

    // Locks
    ReentrantLock lock = new ReentrantLock();

    public ReclaimOffice() {
        this.nReclaims = 0;
        this.nLuggagesLost = 0;
    }

    public int getnReclaims() {
        return nReclaims;
    }

    public int getnLuggagesLost() {
        return nLuggagesLost;
    }

    /**
     *
     * @param n reports that there are 'n' more bags missing.
     */
    public void reportMissingBags(int n){
        lock.lock();
        try {
            System.out.println("REPORT MISSING BAGS: " + n);
            this.nLuggagesLost += n;
        }catch (Exception e){}
        finally {
            lock.unlock();
        }

    }

    public void terminate(){

        System.exit(1);
    }


    public Message processAndReply(Message inMessage) {

        Message outMessage = null;

        switch (inMessage.getType()) {

            case Message.RMB:
                reportMissingBags(inMessage.getN());
                outMessage = new Message(Message.ACK);
                break;

            case Message.GNLL:
                outMessage = new Message(Message.ACK, getnLuggagesLost());
                break;

            case Message.TERM:
                outMessage = new Message(Message.ACK);
                terminate();
                break;
        }

        return outMessage;
    }
}
