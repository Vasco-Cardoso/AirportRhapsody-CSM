package serverSide.repository;

import comInf.Luggages;
import comInf.Message;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.locks.ReentrantLock;

public class GeneralRepository {

    ReentrantLock lock = new ReentrantLock();

    private int nFlight;
    private int nBags;
    private int nBagsConveyor;
    private int nBagsStoreroom;
    private String [] waitingQueue;
    private String[] busOcupation;
    private String[] passSituation;
    private String[] passState;
    private String driverState;
    private String porterState;
    private int[] nBagsCollected;
    private int[] nBagsStart;
    private int nTotalBags;
    private int nLostBags;
    private int nFDTPass;
    private int nTRTPass;
    private File fich;

    private static int WQidx = 0;
    private static int Bidx = 0;

    public GeneralRepository(int nSeats, int nPass, int waitingQ, String fName){

        this.busOcupation = new String[nSeats];
        for(int i=0; i<nSeats; i++){

            this.busOcupation[i] = "-";
        }
        this.passSituation = new String[nPass];
        this.passState = new String[nPass];
        this.nBagsCollected = new int[nPass];
        this.nBagsStart = new int[nPass];
        this.waitingQueue = new String[waitingQ];
        for(int i=0; i<waitingQ; i++){

            this.waitingQueue[i] = "-";
        }
        this.nTotalBags = 0;
        this.nLostBags = 0;
        this.nFDTPass = 0;
        this.nTRTPass = 0;
        this.nBagsConveyor = 0;
        this.nBagsStoreroom = 0;
        this.fich = new File(fName);
    }

    public void setnFlight(int nFlight) {
        lock.lock();
        try{

            this.nFlight = nFlight;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setnBags(int nBags) {

        lock.lock();
        try{
            this.nBags = nBags;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setnBagsConveyor(int nBagsConveyor) {


        lock.lock();
        try{

            this.nBagsConveyor = nBagsConveyor;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setnBagsStoreroom(int nBagsStoreroom) {


        lock.lock();
        try{

            this.nBagsStoreroom = nBagsStoreroom;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setWaitingQueue(String waitingQueue) {

        lock.lock();
        try{

            if(waitingQueue.equals("-"))
            {
                for(int i=0; i<this.waitingQueue.length-1; i++)
                {
                    this.waitingQueue[i] = this.waitingQueue[i+1];
                }
                this.waitingQueue[this.waitingQueue.length-1] = "-";
                WQidx--;
            }else {

                this.waitingQueue[WQidx] = waitingQueue;
                WQidx++;
            }
        }catch (Exception e){}
        finally {

            lock.unlock();
        }

    }

    public void setBusOcupation(String busOcupation) {


        lock.lock();
        try{

            if(busOcupation.equals("-"))
            {
                for(int i=0; i<this.busOcupation.length-1; i++)
                {
                    this.busOcupation[i] = this.busOcupation[i+1];
                }
                this.busOcupation[this.busOcupation.length-1] = "-";
                Bidx--;
            }else {

                this.busOcupation[Bidx] = busOcupation;
                Bidx++;
            }
        }catch (Exception e){}
        finally {

            lock.unlock();
        }

    }

    public void setPassSituation(int idx, boolean passSituation) {

        lock.lock();
        try{

            if(passSituation){
                this.passSituation[idx] = "TRT";
                this.nTRTPass++;
            }else{
                this.passSituation[idx] = "FDT";
                this.nFDTPass++;
            }
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setPassState(int idx, String passState) {

        lock.lock();
        try{

            this.passState[idx] = passState;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setDriverState(String driverState) {

        lock.lock();
        try{

            this.driverState = driverState;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }

    }

    public void setPorterState(String porterState) {

        lock.lock();
        try{

            this.porterState = porterState;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setnBagsCollected(int idx, int nBagsCollected) {

        lock.lock();
        try {
            this.nBagsCollected[idx] = nBagsCollected;
            this.nLostBags = this.nTotalBags - nBagsCollected;
        }
        catch (Exception e){}
        finally {
            lock.unlock();
        }
    }

    public void setnBagsStart(int idx, int nBagsStart) {

        lock.lock();
        try{

            this.nBagsStart[idx] = nBagsStart;
            this.nTotalBags = this.nTotalBags + nBagsStart;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public void setnLostBags(int nLostBags) {

        lock.lock();
        try{

            this.nLostBags = nLostBags;
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return String.format("PLANE         PORTER                  DRIVER" +
                        "\n%2d %2d   %s %2d %2d     %s %s %s %s %s %s %s   %s %s %s" +
                        "\n                    PASSENGER           " +
                        "\nSt1 Si1 NR1 NA1   St2 Si2 NR2 NA2   St3 Si3 NR3 NA3  St4 Si4 NR4 NA4  St5 Si5 NR5 NA5  St6 Si6 NR6 NA6" +
                        "\n%s  %3s %1d   %1d     %s %3s  %1d   %1d     %s %3s  %1d   %1d    %s %3s  %1d   %1d    %s %3s  %1d   %1d    %s %3s  %1d   %1d",
                this.nFlight, this.nBags, this.porterState, this.nBagsConveyor, this.nBagsStoreroom, this.driverState,
                this.waitingQueue[0], this.waitingQueue[1], this.waitingQueue[2], this.waitingQueue[3], this.waitingQueue[4], this.waitingQueue[5],
                this.busOcupation[0], this.busOcupation[1], this.busOcupation[2],
                this.passState[0], this.passSituation[0], this.nBagsStart[0], this.nBagsCollected[0],
                this.passState[1], this.passSituation[1], this.nBagsStart[1], this.nBagsCollected[1],
                this.passState[2], this.passSituation[2], this.nBagsStart[2], this.nBagsCollected[2],
                this.passState[3], this.passSituation[3], this.nBagsStart[3], this.nBagsCollected[3],
                this.passState[4], this.passSituation[4], this.nBagsStart[4], this.nBagsCollected[4],
                this.passState[5], this.passSituation[5], this.nBagsStart[5], this.nBagsCollected[5]);
    }

    // Final report
    public String finalReport(){
        return  "\nFinal Report" +
                "\nN. of passengers which have this airport as their final destination = " + this.nFDTPass +
                "\nN. of passengers in transit = " + this.nTRTPass +
                "\nN. of bags that should have been transported in the planes hold = " + this.nTotalBags +
                "\nN- of bags that were lost = " + this.nLostBags;
    }

    public void clear(){

        for(int i=0; i<this.busOcupation.length; i++)
        {
            this.busOcupation[i] = "-";
        }

        for(int i=0; i<this.passSituation.length; i++)
        {
            this.passSituation[i] = "---";
        }

        for(int i=0; i<this.passState.length; i++)
        {
            this.passState[i] = "----";
        }

        for(int i=0; i<this.waitingQueue.length; i++)
        {
            this.waitingQueue[i] = "-";
        }
    }

    public void write(boolean end){

        lock.lock();
        try{

            String head = ("                                        AIRPORT RHAPSODY - Description of the internal state of the problem" +
                    "\nPLANE         PORTER                  DRIVER                                                            PASSENGER" +
                    "\nFN  BN   Stat CB SR    Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3    St1  Si1 NR1 NA1    St2  Si2 NR2 NA2    St3  Si3 NR3 NA3    St4  Si4 NR4 NA4    St5  Si5 NR5 NA5    St6  Si6 NR6 NA6");
            String s = String.format("\n%2d  %2d   %4s %2d %2d    %4s  %s  %s  %s  %s  %s  %s   %s  %s  %s     %4s %4s  %1d  %1d     %4s %4s  %1d  %1d     %4s %4s  %1d  %1d     %4s %4s  %1d  %1d     %4s %4s  %1d  %1d     %4s %4s  %1d  %1d ",
                    this.nFlight, this.nBags, this.porterState, this.nBagsConveyor, this.nBagsStoreroom, this.driverState,
                    this.waitingQueue[0], this.waitingQueue[1], this.waitingQueue[2], this.waitingQueue[3], this.waitingQueue[4], this.waitingQueue[5],
                    this.busOcupation[0], this.busOcupation[1], this.busOcupation[2],
                    this.passState[0], this.passSituation[0], this.nBagsStart[0], this.nBagsCollected[0],
                    this.passState[1], this.passSituation[1], this.nBagsStart[1], this.nBagsCollected[1],
                    this.passState[2], this.passSituation[2], this.nBagsStart[2], this.nBagsCollected[2],
                    this.passState[3], this.passSituation[3], this.nBagsStart[3], this.nBagsCollected[3],
                    this.passState[4], this.passSituation[4], this.nBagsStart[4], this.nBagsCollected[4],
                    this.passState[5], this.passSituation[5], this.nBagsStart[5], this.nBagsCollected[5]);

            try {
                FileWriter fw;
                if(fich.createNewFile()){
                    fw = new FileWriter(fich);
                    fw.write(head + s);
                    fw.close();
                }
                else{
                    fw = new FileWriter(fich, true);
                    if(end){
                        fw.append(this.finalReport());
                    }
                    else{
                        fw.append(s);
                    }
                    fw.close();
                }

            } catch (Exception e) {
                //TODO: handle exception
            }
        }catch (Exception e){}
        finally {

            lock.unlock();
        }
    }

    public Message processAndReply(Message inMessage) {

        Message outMessage = null;                           // mensagem de resposta
        Luggages l = null;

        switch (inMessage.getType ()){

            case Message.SFN:
                setnFlight(inMessage.getN());
                outMessage = new Message (Message.ACK);       // gerar resposta
                break;

            case Message.SNB:
                setnBags(inMessage.getN());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SNBC:
                setnBagsConveyor(inMessage.getN());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SNLB:
                setnLostBags(inMessage.getN());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SNBS:
                setnBagsStoreroom(inMessage.getN());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SWQ:
                setWaitingQueue(inMessage.getOcc());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SBO:
                setBusOcupation(inMessage.getOcc());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SPSI:
                setPassSituation(inMessage.getIdx(), inMessage.getSituation());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SPST:
                setPassState(inMessage.getIdx(), inMessage.getState());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SDS:
                setDriverState(inMessage.getDriverState());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SPOS:
                setPorterState(inMessage.getPorterState());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SNBCL:
                setnBagsCollected(inMessage.getIdx(), inMessage.getNcollected());
                outMessage = new Message (Message.ACK);
                break;
            case Message.SNBST:
                setnBagsStart(inMessage.getIdx(), inMessage.getNcollected());
                outMessage = new Message (Message.ACK);
                break;
            case Message.CLR:
                clear();
                outMessage = new Message (Message.ACK);
                break;
            case Message.WRT:
                write(inMessage.getSituation());
                outMessage = new Message (Message.ACK);
                break;
        }

        return outMessage;
    }
}
