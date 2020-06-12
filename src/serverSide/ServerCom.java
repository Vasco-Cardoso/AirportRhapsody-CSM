package serverSide;

import java.io.*;
import java.net.*;

/**
 *   Este tipo de dados implementa o canal de comunicação, lado do servidor, para uma comunicação baseada em passagem de
 *   mensagens sobre sockets usando o protocolo TCP.
 *   A transferência de dados é baseada em objectos, um objecto de cada vez.
 */

public class ServerCom
{
  /**
   *  Socket de escuta
   *    @serialField listeningSocket
   */

   private ServerSocket listeningSocket = null;

  /**
   *  Socket de comunicação
   *    @serialField commSocket
   */

   private Socket commSocket = null;

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
   *  Instanciação de um canal de comunicação (forma 1).
   *
   *    @param portNumb número do port de escuta do servidor
   */

   public ServerCom (int portNumb)
   {
      serverPortNumb = portNumb;
   }

  /**
   *  Instanciação de um canal de comunicação (forma 2).
   *
   *    @param portNumb número do port de escuta do servidor
   *    @param lSocket socket de escuta
   */

   public ServerCom (int portNumb, ServerSocket lSocket)
   {
      serverPortNumb = portNumb;
      listeningSocket = lSocket;
   }

  /**
   *  Estabelecimento do serviço.
   *  Instanciação de um socket de escuta e sua associação ao endereço da máquina local
   *  e ao port de escuta públicos.
   */

   public void start ()
   {
      try
      { listeningSocket = new ServerSocket (serverPortNumb);
        setTimeout (10000);
      }
      catch (BindException e)                         // erro fatal --- port já em uso
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)                           // erro fatal --- outras causas
      {
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Encerramento do serviço.
   *  Fecho do socket de escuta.
   */

   public void end ()
   {
      try
      { listeningSocket.close ();
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Processo de escuta.
   *  Criação de um canal de comunicação para um pedido pendente.
   *  Instanciação de um socket de comunicação e sua associação ao endereço do cliente.
   *  Abertura dos streams de entrada e de saída do socket.
   *
   *    @return canal de comunicação
   */

   public ServerCom accept () throws SocketTimeoutException
   {
      ServerCom scon;                                      // canal de comunicação

      scon = new ServerCom(serverPortNumb, listeningSocket);
      try
      { scon.commSocket = listeningSocket.accept();
      }
      catch (SocketTimeoutException e)
      { throw new SocketTimeoutException ("Timeout!");
      }
      catch (SocketException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.in = new ObjectInputStream (scon.commSocket.getInputStream ());
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.out = new ObjectOutputStream (scon.commSocket.getOutputStream ());
      }
      catch (IOException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      return scon;
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
   *  Definição de um time out de escuta.
   */

   public void setTimeout (int time)
   {
      try
      { listeningSocket.setSoTimeout (time);
      }
      catch (SocketException e)
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
      Object fromClient = null;                            // objecto

      try
      {
        fromClient = in.readObject();
      } catch (InvalidClassException e) {
        e.printStackTrace ();
        System.exit (1);
      } catch (IOException e) {
        e.printStackTrace ();
        System.exit (1);
      } catch (ClassNotFoundException e) {
        e.printStackTrace ();
        System.exit (1);
      }

      return fromClient;
   }

  /**
   *  Escrita de um objecto no canal de comunicação.
   *
   *    @param toClient objecto a ser escrito
   */

   public void writeObject (Object toClient)
   {
      try
      { out.writeObject (toClient);
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
