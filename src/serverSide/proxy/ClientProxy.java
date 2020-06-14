package serverSide.proxy;

import comInf.Message;
import serverSide.communications.ServerCom;
import serverSide.repository.GeneralRepository;
import serverSide.sharedRegions.*;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema dos Barbeiros
 *   Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos
 *   threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class ClientProxy extends Thread
{

    private int type = -1;

  /**
   *  Contador de threads lançados
   *
   *    @serialField nProxy
   */

   private static int nProxy = 0;

  /**
   *  Canal de comunicação
   *
   *    @serialField sconi
   */

   private ServerCom sconi;

  /**
   *  Interface à barbearia
   *
   *    @serialField bShopInter
   */

   private ArrivalLounge arrivalLounge;
   private LuggageCollectionPoint collPoint;
   private ReclaimOffice reclaimOffice;
   private ArrivalTransferTerminal arrTransQuay;
   private DepartureTransferTerminal depTransQuay;
   private ArrivalTerminalExit arrTermExit;
   private DepartureTerminalEntry depTermEntrance;
   private StorageArea tempStorageArea;

   private GeneralRepository genRepo;

  /**
   *  Instanciação do interface.
   *
   */

   public ClientProxy (ServerCom sconi, ArrivalLounge arrivalLounge)
   {
      super ("Proxy_" + ClientProxy.getProxyId ());

      this.sconi = sconi;
      this.arrivalLounge = arrivalLounge;
      this.type = 1;
   }

    public ClientProxy (ServerCom sconi, LuggageCollectionPoint luggageCollectionPoint)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.collPoint = luggageCollectionPoint;
        this.type = 2;
    }

    public ClientProxy (ServerCom sconi, ArrivalTerminalExit arrivalTerminalExit)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.arrTermExit = arrivalTerminalExit;
        this.type = 3;
    }

    public ClientProxy (ServerCom sconi, ArrivalTransferTerminal arrivalTransferTerminal)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.arrTransQuay = arrivalTransferTerminal;
        this.type = 4;
    }

    public ClientProxy (ServerCom sconi, DepartureTerminalEntry departureTerminalEntry)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.depTermEntrance = departureTerminalEntry;
        this.type = 5;
    }

    public ClientProxy (ServerCom sconi, DepartureTransferTerminal departureTransferTerminal)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.depTransQuay = departureTransferTerminal;
        this.type = 6;
    }

    public ClientProxy (ServerCom sconi, ReclaimOffice reclaimOffice)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.reclaimOffice = reclaimOffice;
        this.type = 7;
    }

    public ClientProxy (ServerCom sconi, StorageArea storageArea)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.tempStorageArea = storageArea;
        this.type = 8;
    }

    public ClientProxy (ServerCom sconi, GeneralRepository genrepo)
    {
        super ("Proxy_" + ClientProxy.getProxyId ());

        this.sconi = sconi;
        this.genRepo = genrepo;
        this.type = 9;
    }


  /**
   *  Ciclo de vida do thread agente prestador de serviço.
   */

   @Override
   public void run ()
   {
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                                     // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente

       try
      {
          switch (this.type){
              case 1:
                  outMessage = arrivalLounge.processAndReply (inMessage);
                  break;
              case 2:
                  outMessage = collPoint.processAndReply (inMessage);
                  break;
              case 3:
                  outMessage = arrTermExit.processAndReply (inMessage);
                  break;
              case 4:
                  outMessage = arrTransQuay.processAndReply (inMessage);
                  break;
              case 5:
                  outMessage = depTermEntrance.processAndReply (inMessage);
                  break;
              case 6:
                  outMessage = depTransQuay.processAndReply (inMessage);
                  break;
              case 7:
                  outMessage = reclaimOffice.processAndReply (inMessage);
                  break;
              case 8:
                  outMessage = tempStorageArea.processAndReply (inMessage);
                  break;
              case 9:
                  outMessage = genRepo.processAndReply(inMessage);
              default:
                  System.out.println("ERROR");
                  break;
          }
      }
      catch (Exception e)
      {
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

  /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */

   private static int getProxyId ()
   {
      Class<?> cl = null;                                  // representação do tipo de dados ClientProxy na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = Class.forName ("serverSide.proxy.ClientProxy");
      }
      catch (ClassNotFoundException e)
      {
        e.printStackTrace ();
        System.exit (1);
      }

      synchronized (cl)
      {
          proxyId = nProxy;
        nProxy += 1;
      }

      return proxyId;
   }

  /**
   *  Obtenção do canal de comunicação.
   *
   *    @return canal de comunicação
   */

   public ServerCom getScon ()
   {
      return sconi;
   }
}
