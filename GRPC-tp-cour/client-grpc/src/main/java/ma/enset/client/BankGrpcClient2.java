package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

import java.io.IOException;

public class BankGrpcClient2 {//modelle asuncron  ****unary model*******
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)//connecter au serveur
                .usePlaintext()//modelle de cominication
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);//stabe (proxy) type blokon  et nous permet dacer ou methode
        Bank.ConvertCurrencyRequest request=Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(39)
                .build();
        asyncStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {//je envoi et jatend                   //ach fhamt :stremObserver ;il creée un objet dans un tred et il reste travaiyer et envoi un se objet ,en retoure il atend les reponce de serveur
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {//si il menvoi quelque chose il affich
                System.out.println("*************************");
                System.out.println(convertCurrencyResponse);
                System.out.println("*************************");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("END....");
            }
        });
        System.out.println(".......?");
        System.in.read();//atendre que litulisateur tape sur une touche
    }
}
