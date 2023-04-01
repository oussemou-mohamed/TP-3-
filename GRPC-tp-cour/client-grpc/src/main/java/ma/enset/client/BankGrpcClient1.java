package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

public class BankGrpcClient1 {
    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)//connecter au serveur
                .usePlaintext()//modelle de cominication
                .build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub=BankServiceGrpc.newBlockingStub(managedChannel);//stabe (proxy) type blokon  et nous permet dacer ou methode
        Bank.ConvertCurrencyRequest request=Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(39)
                .build();
        Bank.ConvertCurrencyResponse currencyResponse = blockingStub.convert(request);//atennd la repence
        System.out.println(currencyResponse);
    }
}
