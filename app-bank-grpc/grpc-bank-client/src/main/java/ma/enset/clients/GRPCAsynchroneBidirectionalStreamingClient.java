package ma.enset.clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class GRPCAsynchroneBidirectionalStreamingClient {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub bankServiceStub=BankServiceGrpc.newStub(managedChannel);
        StreamObserver<Bank.AccountTransaction> accountTransactionStreamObserver =
                bankServiceStub.executeStreamOfTransaction(new StreamObserver<Bank.AccountTransaction>() {
                    @Override
                    public void onNext(Bank.AccountTransaction accountTransaction) {
                        System.out.println("<<<<============= Result Item od server stream =================");
                        System.out.println(accountTransaction);
                        System.out.println("======================================");
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                    @Override
                    public void onCompleted() {
                        System.out.println("End Of conversation");
                    }
                });
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            private int counter=0;
            @Override
            public void run() {
                ++counter;
                Bank.AccountTransaction accountTransaction= Bank.AccountTransaction.newBuilder()
                        .setAccountId(UUID.randomUUID().toString())
                        .setTransactionDate(System.currentTimeMillis())
                        .setAmount(Math.random()*98000)
                        .setType(Math.random()>0.5? Bank.TransactionType.DEBIT: Bank.TransactionType.CREDIT)
                        .build();
                accountTransactionStreamObserver.onNext(accountTransaction);
                System.out.println("*************----------->>>>-----------*****************");
                System.out.println("Submitting Transaction Number "+counter);
                System.out.println(accountTransaction);
                System.out.println("******************************");
                if(counter>10){
                    timer.cancel();
                    accountTransactionStreamObserver.onCompleted();
                }
            }
        }, 0, 1000);
        System.in.read();
    }
}
