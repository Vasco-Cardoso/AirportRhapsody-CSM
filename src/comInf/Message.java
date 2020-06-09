package comInf;

import java.io.*;

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

   // Passenger

   public static final int WSID   =  6;

   public static final int GCAB    =  7;

   public static final int GH      =  8;

   public static final int RMP  =  9;

   public static final int TAB     =  10;

   public static final int ETB      = 11;

   public static final int LTB = 12;

   public static final int PNL   = 13;

   public static final int DL   = 14;

   public static final int DP   = 15;

   // Driver

   public static final int PTB   = 16;

    public static final int ABB   = 17;

    public static final int HDWE   = 18;

    public static final int GTDT   = 19;

   public static final int PTBALPO   = 20;

   public static final int GTAT   = 21;


    // Gerais

   public static final int ACK   = 22;



  /* Campos das mensagens */

   private int msgType = -1;

   private int passId = -1;

   private String fName = null;

   private int nIter = -1;

   private Luggages lug = null;


   public Message (int type)
   {
      msgType = type;
   }


   public Message (int type, int passId)
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

   public int getType() {
      return msgType;
   }

   public Luggages getLuggage() {
      return lug;
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
