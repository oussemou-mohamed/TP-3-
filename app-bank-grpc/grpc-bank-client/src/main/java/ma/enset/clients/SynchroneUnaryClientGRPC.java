package ma.enset.clients;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

import java.util.UUID;

public class SynchroneUnaryClientGRPC {
    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub=BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest convertCurrencyRequest= Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(6500)
                .setCurrencyFrom("DH")
                .setCurrencyTo("EUR")
                .build();
        System.out.println("**************Convert****************");
        Bank.ConvertCurrencyResponse convertResponse = bankServiceBlockingStub.convert(convertCurrencyRequest);
        System.out.println(convertResponse.toString());
        System.out.println("**************GetAccount*************");
        Bank.GetAccountResponse accountResponse = bankServiceBlockingStub.getAccount(
                         Bank.GetAccountRequest.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .build());
        System.out.println(accountResponse.getAccount());
        System.out.println("**************GetListeAccounts********");
        Bank.GetListAccountsResponse listAccountsResponse = bankServiceBlockingStub.getListAccounts(Bank.GetListAccountsRequest.getDefaultInstance());
        listAccountsResponse.getAccountsList().forEach(ac->{
            System.out.println("-----------------------------");
            System.out.println(ac);
        });
    }

}
