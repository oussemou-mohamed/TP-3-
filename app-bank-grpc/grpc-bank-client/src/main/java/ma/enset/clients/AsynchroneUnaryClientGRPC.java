package ma.enset.clients;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;
import java.io.IOException;
public class AsynchroneUnaryClientGRPC {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub bankServiceStub= BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest convertCurrencyRequest=Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(6500)
                .setCurrencyFrom("DH")
                .setCurrencyTo("EUR")
                .build();

        bankServiceStub.convert(convertCurrencyRequest, new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("<<<****************Convert****************\\n\\r"+
                                        convertCurrencyResponse.toString());
            }
            @Override
            public void onError(Throwable throwable) { System.out.println(throwable.getMessage()); }
            @Override
            public void onCompleted() { System.out.println("Conversion => End of Conversation"); }
        });

        bankServiceStub.getAccount(Bank.GetAccountRequest.newBuilder().setId("7654").build(), new StreamObserver<Bank.GetAccountResponse>() {
                    @Override
                    public void onNext(Bank.GetAccountResponse getAccountResponse) {

                        System.out.println("<<<***********getAccount************\\n\\r"
                                          +getAccountResponse);
                    }
                    @Override
                    public void onError(Throwable throwable) {System.out.println(throwable.getMessage());}
                    @Override
                    public void onCompleted() { System.out.println("Get Account => End of conversation");  }
                });
        bankServiceStub.getListAccounts(Bank.GetListAccountsRequest.getDefaultInstance(), new
                StreamObserver<Bank.GetListAccountsResponse>() {
                    @Override
                    public void onNext(Bank.GetListAccountsResponse getListAccountsResponse) {
                        getListAccountsResponse.getAccountsList().forEach(acc->{
                            System.out.println("<<<*******************GetAccountList****************\\n\\r"+acc.toString());

                        });
                    }
                    @Override
                    public void onError(Throwable throwable) { System.out.println(throwable.getMessage()); }
                    @Override
                    public void onCompleted() { System.out.println("Get List Accounts => End of conversation"); }
                });
        System.in.read();
    }
}

