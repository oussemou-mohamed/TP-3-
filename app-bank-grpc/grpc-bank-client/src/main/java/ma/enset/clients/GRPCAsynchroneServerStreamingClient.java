package ma.enset.clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.UUID;

public class GRPCAsynchroneServerStreamingClient {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext() .build();
        BankServiceGrpc.BankServiceStub bankServiceStub=BankServiceGrpc.newStub(managedChannel);
        bankServiceStub.getStreamOfAccountTransactions(
                Bank.GetStreamOfAccountTransactionRequest.newBuilder()
                                                         .setAccountId(UUID.randomUUID().toString())
                                                         .build(),
                new StreamObserver<Bank.AccountTransaction>() {
            @Override
            public void onNext(Bank.AccountTransaction accountTransaction) {
                System.out.println("***********************");
                System.out.println(accountTransaction);
                System.out.println("***********************");
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("============ Error ==============");
                System.out.println(throwable.getMessage());
                System.out.println("=================================");
            }
            @Override
            public void onCompleted() {
                System.out.println("End of server streaming");
            }
        });
        System.in.read();
    }
}
