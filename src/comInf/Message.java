package comInf;

import clientSide.entities.Passenger;

import java.io.*;
import java.util.Queue;

public class Message implements Serializable
{


   private static final long serialVersionUID = 1001L;

  /* Tipos das mensagens */

    // Porter

   public static final int TR  =  1;

   public static final int TTCB =  2;

   public static final int NMBTC  =  3;

   public static final int CITAS =  4;

   public static final int SE =  5;

   public static final int RMB = 6;

   // Passenger

   public static final int WSID   =  7;

   public static final int GCAB    =  8;

   public static final int GH      =  9;

   public static final int RMP  =  10;

   public static final int TAB     =  11;

   public static final int ETB      = 12;

   public static final int PNL   = 13;

   public static final int DL   = 14;                 // depositLuggage

   public static final int DP   = 15;                 // disenbarkPassenger

   public static final int AT   = 16;                 // arrivedTerminal

   public static final int WLP = 17;                  // waitLastPassenger

   public static final int LP = 18;                   // lastPassenger

   public static final int LDTT = 19;                 // leaveDepartureTransferTerminal

   public static final int LTB = 20;                  // leaveTheBus

   public static final int GNP = 21;

   // Driver

   public static final int PTB   = 22;

    public static final int ABB   = 23;

    public static final int HDWE   = 24;

    public static final int GTDT   = 25;

   public static final int PTBALPO   = 26;

   public static final int GTAT   = 27;

   public static final int GS = 28;

   public static final int CS = 29;

    // Gerais

   public static final int ACK   = 30;

   public static final int SCL = 31;

   public static final int GNLL = 32;

   // General Repository

   public static final int SFN = 33;

   public static final int SNB = 34;

   public static final int SNBC = 35;

   public static final int SNBS = 36;

   public static final int SWQ = 37;

   public static final int SBO = 38;

   public static final int SPST = 39;

   public static final int SPSI = 40;

   public static final int SDS = 41;

   public static final int SNLB = 42;

   public static final int CLR = 43;

   public static final int WRT = 44;

   public static final int SPOS = 45;

   public static final int SNBCL = 46;

   public static final int SNBST = 47;


  /* Campos das mensagens */

   private int msgType = -1;

   private Passenger passId = null;

   private String fName = null;

   private int nIter = -1;

   private Luggages lug = null;

   private int n = -1;

   private Queue<Passenger> seats = null;

   private String lugg = null;

   private int idx = -1;

   private boolean situation = false;

   private String state = null;

   private int ncollected = -1;

   private String porterState = null;

   private String driverState = null;


   public Message (int type)
   {
      msgType = type;
   }

   public Message (int type, int n)
   {
      msgType = type;
      this.n = n;
   }

   public Message (int type, Passenger passId)
   {
      msgType = type;
      this.passId = passId;
   }

   public Message (int type, String lug)
   {
      msgType = type;
      this.lugg = lug;
   }

    public Message (int type, Luggages lug)
    {
        msgType = type;
        this.lug= lug;
    }

   public Message (int type, Queue<Passenger> seats)
   {
      msgType = type;
      this.seats= seats;
   }

   public Message(int type, int idx, boolean passSituation) {

      msgType = type;
      this.idx = idx;
      this.situation = passSituation;
   }

   public Message(int type, boolean passSituation) {

      msgType = type;
      this.situation = passSituation;
   }

   public Message(int type, int idx, String passState) {

      msgType = type;
      this.idx = idx;
      this.state = passState;
   }

   public Message(int type, int idx, int nBagsCollected) {

      msgType = type;
      this.idx = idx;
      this.ncollected = nBagsCollected;
   }

   public Message(int type, String state, int pord){

      if(pord == 1){

         this.porterState = state;
      }else
         this.driverState = state;
   }

   public int getType() {
      return msgType;
   }

   public Luggages getLuggage() {
      return lug;
   }

   public int getN() {
      return n;
   }

   public Passenger getPass() {
      return passId;
   }

   public Queue<Passenger> getSeats(){

      return seats;
   }

   @Override
   public String toString() {
      return "Message{" +
              "msgType=" + msgType +
              ", passId=" + passId +
              ", fName='" + fName + '\'' +
              ", nIter=" + nIter +
              ", lug=" + lug +
              '}';
   }

   public int getIdx() {
      return idx;
   }

   public boolean getSituation() {
      return situation;
   }

   public String getState() {
      return state;
   }

   public int getNcollected() {
      return ncollected;
   }

   public String getPorterState() {
      return porterState;
   }

   public String getDriverState() {
      return driverState;
   }
}
