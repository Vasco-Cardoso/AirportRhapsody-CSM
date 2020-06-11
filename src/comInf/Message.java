package comInf;

import clientSide.Passenger;

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



  /* Campos das mensagens */

   private int msgType = -1;

   private Passenger passId = null;

   private String fName = null;

   private int nIter = -1;

   private Luggages lug = null;

   private int n = -1;

   private Queue<Passenger> seats = null;


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

   public Message (int type, String name, int nIter)
   {
      msgType = type;
      fName= name;
      this.nIter = nIter;
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
}
