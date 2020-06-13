package serverSide;

import java.net.SocketTimeoutException;

public class StorageAreaMain {

    private static final int portNumb = 22000;
    public static boolean waitConnection;


    public static void main(String[] args) {

        StorageArea tempStorageArea;

        ServerCom scon_storageAreaStub, sconi_storageAreaStub;

        ClientProxy cliProxy;

        scon_storageAreaStub = new ServerCom (portNumb + 8);                     // criação do canal de escuta e sua associação
        scon_storageAreaStub.start ();

        tempStorageArea = new StorageArea();

        waitConnection = true;
        while (waitConnection)
        {
            try
            {
                System.out.println("SERVER AIRPORT: AFTER SCONIS");

                sconi_storageAreaStub = scon_storageAreaStub.accept ();                          // entrada em processo de escuta
                cliProxy = new ClientProxy (sconi_storageAreaStub, tempStorageArea);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        scon_storageAreaStub.end ();
    }
}
