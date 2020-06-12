package serverSide;

import java.net.SocketTimeoutException;

public class ServerAirport {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = 22001;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        ArrivalLounge arrivalLounge;
        LuggageCollectionPoint collPoint;
        ReclaimOffice reclaimOffice;
        ArrivalTransferTerminal arrTransQuay;
        DepartureTransferTerminal depTransQuay;
        ArrivalTerminalExit arrTermExit;
        DepartureTerminalEntry depTermEntrance;
        StorageArea tempStorageArea;

        ServerCom scon, sconi;                               // canais de comunicação
        ClientProxy cliProxy1,cliProxy2,cliProxy3,cliProxy4,cliProxy5,cliProxy6,cliProxy7,cliProxy8;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público

        arrivalLounge = new ArrivalLounge();;
        collPoint = new LuggageCollectionPoint();
        reclaimOffice = new ReclaimOffice();
        arrTransQuay = new ArrivalTransferTerminal();
        depTransQuay = new DepartureTransferTerminal();
        arrTermExit = new ArrivalTerminalExit();
        depTermEntrance = new DepartureTerminalEntry();
        tempStorageArea = new StorageArea();

        System.out.println("O serviço foi estabelicido");
        System.out.println("O servidor está em escuta");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try
            { sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy1 = new ClientProxy (sconi, arrivalLounge);  // lançamento do agente prestador do serviço
                cliProxy1.start ();
                cliProxy2 = new ClientProxy (sconi, collPoint);  // lançamento do agente prestador do serviço
                cliProxy2.start ();
                cliProxy3 = new ClientProxy (sconi, reclaimOffice);  // lançamento do agente prestador do serviço
                cliProxy3.start ();
                cliProxy4 = new ClientProxy (sconi, arrTransQuay);  // lançamento do agente prestador do serviço
                cliProxy4.start ();
                cliProxy5 = new ClientProxy (sconi, depTransQuay);  // lançamento do agente prestador do serviço
                cliProxy5.start ();
                cliProxy6 = new ClientProxy (sconi, arrTermExit);  // lançamento do agente prestador do serviço
                cliProxy6.start ();
                cliProxy7 = new ClientProxy (sconi, depTermEntrance);  // lançamento do agente prestador do serviço
                cliProxy7.start ();
                cliProxy8 = new ClientProxy (sconi, tempStorageArea);  // lançamento do agente prestador do serviço
                cliProxy8.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        scon.end ();                                         // terminação de operações

    }
}
