//import model Livro
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Produtor {

    public static void main(String[] args) throws Exception{
        //Criacao de uma factory de conexao, responsavel por criar as conexoes
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //localizacao do gestor da fila (Queue Manager)
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        String NOME_FILA = "filaJson";

        List<Livro> livros = new ArrayList<>();
        for (int i=0; i<11; i++) {
            Livro livro = new Livro("Livro"+i, "Letras");
            livros.add(livro);
        }

        JSONWriter jsonWriter = new JSONWriter();
        String mensagem = jsonWriter.write(livros);

        try{
            //criacao de uma coneccao
            Connection connection = connectionFactory.newConnection();
            //criando um canal na conexao
            Channel channel = connection.createChannel();
            //Esse corpo especifica o envio da mensagem para a fila
            // Declaracao da fila. Se nao existir ainda no queue manager, serah criada. Se jah existir, e foi criada com
            // os mesmos parametros, pega a referencia da fila. Se foi criada com parametros diferentes, lanca excecao
            channel.queueDeclare(NOME_FILA, true, false, false, null);
            //publica uma mensagem na fila
            channel.basicPublish("", NOME_FILA, null, mensagem.getBytes());
            System.out.println("Enviei mensagem: " + mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
