package ma.enset.client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Chat;
import ma.enset.stubs.ChatServiceGrpc;
import java.io.IOException;
public class ClientChat {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",4444)
                .usePlaintext()
                .build();
        ChatServiceGrpc.ChatServiceStub chatServiceStub = ChatServiceGrpc.newStub(managedChannel);
        StreamObserver<Chat.Message> thisClient = new StreamObserver<>() {
            @Override
            public void onNext(Chat.Message message) {
                System.out.println("<<<<<<------" + message.getMsg());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("End of conversation");
            }
        };
        //StreamObserver<Chat.Message> messageStreamObserver = chatServiceStub.diffuseMessageBid(thisClient);
        //connecter
        chatServiceStub.connecter(Chat.Nom.newBuilder().setNom("moad").build(), thisClient);
        chatServiceStub.connecter(Chat.Nom.newBuilder().setNom("hassan").build(), thisClient);
        //Scanner scanner=new Scanner(System.in);
        /*while(true) {
            String request = scanner.nextLine();
            if (request.contains(">")){
            }else {
                Chat.Message message = Chat.Message.newBuilder()
                        .setMsg(request).build();
                messageStreamObserver.onNext(message);
            }
        }*/
        System.in.read();
    }
}
