package serverSide;

import java.net.SocketTimeoutException;

public class ServerAirport {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = 22000;
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

        ServerCom scon_arrivalLounge, sconi_arrivalLounge;                               // canais de comunicação
        ServerCom scon_luggageCollectionPoint, sconi_luggageCollectionPoint;                               // canais de comunicação
        ServerCom scon_reclaimOffice, sconi_reclaimOffice;                               // canais de comunicação
        ServerCom scon_arrivalTransferTerminal, sconi_arrivalTransferTerminal;                               // canais de comunicação
        ServerCom scon_departureTransferTerminal, sconi_departureTransferTerminal;                               // canais de comunicação
        ServerCom scon_arrivalTerminalExit, sconi_arrivalTerminalExit;                               // canais de comunicação
        ServerCom scon_departureTerminalEntry, sconi_departureTerminalEntry;                               // canais de comunicação
        ServerCom scon_storageAreaStub, sconi_storageAreaStub;                               // canais de comunicação

        ClientProxy cliProxy1,cliProxy2,cliProxy3,cliProxy4,cliProxy5,cliProxy6,cliProxy7,cliProxy8;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon_arrivalLounge = new ServerCom (portNumb + 1);       // criação do canal de escuta e sua associação
        scon_arrivalLounge.start ();                                       // com o endereço público

        scon_luggageCollectionPoint = new ServerCom (portNumb + 2);       // criação do canal de escuta e sua associação
        scon_luggageCollectionPoint.start ();

        scon_reclaimOffice = new ServerCom (portNumb + 3);                     // criação do canal de escuta e sua associação
        scon_reclaimOffice.start ();

        scon_arrivalTransferTerminal = new ServerCom (portNumb + 4);                     // criação do canal de escuta e sua associação
        scon_arrivalTransferTerminal.start ();

        scon_departureTransferTerminal = new ServerCom (portNumb + 5);                     // criação do canal de escuta e sua associação
        scon_departureTransferTerminal.start ();

        scon_arrivalTerminalExit = new ServerCom (portNumb + 6);                     // criação do canal de escuta e sua associação
        scon_arrivalTerminalExit.start ();

        scon_departureTerminalEntry = new ServerCom (portNumb + 7);                     // criação do canal de escuta e sua associação
        scon_departureTerminalEntry.start ();

        scon_storageAreaStub = new ServerCom (portNumb + 8);                     // criação do canal de escuta e sua associação
        scon_storageAreaStub.start ();

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
        {
            try
            {
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                //
                sconi_arrivalLounge = scon_arrivalLounge.accept ();                          // entrada em processo de escuta
                cliProxy1 = new ClientProxy (sconi_arrivalLounge, arrivalLounge);  // lançamento do agente prestador do serviço
                cliProxy1.start ();

                //
                sconi_luggageCollectionPoint = scon_luggageCollectionPoint.accept ();                          // entrada em processo de escuta
                cliProxy2 = new ClientProxy (sconi_luggageCollectionPoint, collPoint);  // lançamento do agente prestador do serviço
                cliProxy2.start ();

                //
                sconi_reclaimOffice = scon_reclaimOffice.accept ();                          // entrada em processo de escuta
                cliProxy3 = new ClientProxy (sconi_reclaimOffice, reclaimOffice);  // lançamento do agente prestador do serviço
                cliProxy3.start ();

                //
                sconi_arrivalTransferTerminal = scon_arrivalTransferTerminal.accept ();                          // entrada em processo de escuta
                cliProxy4 = new ClientProxy (sconi_arrivalTransferTerminal, arrTransQuay);  // lançamento do agente prestador do serviço
                cliProxy4.start ();

                //
                sconi_departureTransferTerminal = scon_departureTransferTerminal.accept ();                          // entrada em processo de escuta
                cliProxy5 = new ClientProxy (sconi_departureTransferTerminal, depTransQuay);  // lançamento do agente prestador do serviço
                cliProxy5.start ();

                sconi_arrivalTerminalExit = scon_arrivalTerminalExit.accept ();                          // entrada em processo de escuta
                cliProxy6 = new ClientProxy (sconi_arrivalTerminalExit, arrTermExit);  // lançamento do agente prestador do serviço
                cliProxy6.start ();

                sconi_departureTerminalEntry = scon_departureTerminalEntry.accept ();                          // entrada em processo de escuta
                cliProxy7 = new ClientProxy (sconi_departureTerminalEntry, depTermEntrance);  // lançamento do agente prestador do serviço
                cliProxy7.start ();

                sconi_storageAreaStub = scon_storageAreaStub.accept ();                          // entrada em processo de escuta
                cliProxy8 = new ClientProxy (sconi_storageAreaStub, tempStorageArea);  // lançamento do agente prestador do serviço
                cliProxy8.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }

        }

        // Ending all the scons
        scon_arrivalLounge.end ();
        scon_luggageCollectionPoint.end ();
        scon_reclaimOffice.end ();
        scon_arrivalTransferTerminal.end ();
        scon_departureTransferTerminal.end ();
        scon_arrivalTerminalExit.end ();
        scon_departureTerminalEntry.end ();
        scon_storageAreaStub.end ();

        System.out.println("O servidor foi desativado");
    }
}
