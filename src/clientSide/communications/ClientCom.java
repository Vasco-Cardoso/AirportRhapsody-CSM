package clientSide.communications;

import java.io.*;
import java.net.*;

/**
 * Client communication channel data type Uses TCP sockets and object-based data
 * transfer
 *
 * @author Afonso Guimarães
 * @author Vasco Cardoso
 *
 * @version 1.0
 */

public class ClientCom
{
  /**
   *  Socket de comunicação
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  Nome do sistema computacional onde está localizado o servidor
   *    @serialField serverHostName
   */

   private String serverHostName = null;

  /**
   *  Número do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   *  Stream de entrada do canal de comunicação
   *    @serialField in
   */

   private ObjectInputStream in = null;

  /**
   *  Stream de saída do canal de comunicação
   *    @serialField out
   */

   private ObjectOutputStream out = null;

  /**
   *  Instanciação de um canal de comunicação.
   *
   *    @param hostName nome do sistema computacional onde está localizado o servidor
   *    @param portNumb número do port de escuta do servidor
   */

   public ClientCom (String hostName, int portNumb)
   {
      serverHostName = hostName;
      serverPortNumb = portNumb;
   }

  /**
   *  Abertura do canal de comunicação.
   *  Instanciação de um socket de comunicação e sua associação ao endereço do servidor.
   *  Abertura dos streams de entrada e de saída do socket.
   *
   *    @return <li>true, se o canal de comunicação foi aberto
   *            <li>false, em caso contrário
   */

   public boolean open ()
   {
      boolean success = true;
      SocketAddress serverAddress = new InetSocketAddress (serverHostName, serverPortNumb);

      try
      {
          commSocket = new Socket();
          commSocket.connect (serverAddress);
      }
      catch (UnknownHostException e)
      {
          e.printStackTrace ();
          System.exit (1);
      }
      catch (NoRouteToHostException e)
      {
          e.printStackTrace ();
          System.exit (1);
      }
      catch (ConnectException e)
      {
          if (e.getMessage ().equals ("Connection refused"))
            success = false;
            else
            {
                e.printStackTrace ();
                System.exit (1);
            }
      }
      catch (SocketTimeoutException e)
      {
            success = false;
      }
      catch (IOException e)                           // erro fatal --- outras causas
      {
            e.printStackTrace ();
            System.exit (1);
      }

      if (!success)
      {
          return success;
      }


      try
      {
            out = new ObjectOutputStream (commSocket.getOutputStream ());
      }
      catch (IOException e)
      {
            e.printStackTrace ();
            System.exit (1);
      }

      try
      {
            in = new ObjectInputStream (commSocket.getInputStream ());
      }
      catch (IOException e)
      {
            e.printStackTrace ();
            System.exit (1);
      }

      return (success);
   }


  /**
   *  Fecho do canal de comunicação.
   *  Fecho dos streams de entrada e de saída do socket.
   *  Fecho do socket de comunicação.
   */

   public void close ()
   {
      try
      { in.close();
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { out.close();
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { commSocket.close();
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Leitura de um objecto do canal de comunicação.
   *
   *    @return objecto lido
   */

   public Object readObject ()
   {
      Object fromServer = null;                            // objecto

      try
      {
          fromServer = in.readObject ();
      }
      catch (InvalidClassException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (ClassNotFoundException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      return fromServer;
   }

  /**
   *  Escrita de um objecto no canal de comunicação.
   *
   *    @param toServer objecto a ser escrito
   */

   public void writeObject (Object toServer)
   {
      try
      { out.writeObject (toServer);
      }
      catch (InvalidClassException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NotSerializableException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
   }
}
