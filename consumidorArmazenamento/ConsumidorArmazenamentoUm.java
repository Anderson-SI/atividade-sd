package br.com.exemplo.prova.consumidorArmazenamento;

import br.com.exemplo.prova.classAuxiliares.SerializableImagem;
import com.rabbitmq.client.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
/*
 autore: Anderson Costa Moreira Santana
         Taylan Nalimar Cruz Celestino
*/
public class ConsumidorArmazenamentoUm {

    /*
    * Classe usada para consumir as mensagens e armazenar em um ditetório
    * */
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setPassword("guest");
        //factory.setUsername("guest");

        // caminho do diretório que vai armazenar as imagens processadas
        final String pathDiretorioImagensPretoBranco = "C:\\Users\\Ander\\Desktop\\UFS\\SD\\PROVA\\src\\main\\java\\br\\com\\exemplo\\prova\\imagens-tonsdecinza-01\\";

        final String FILA_MENSAGEM_PROCESSADA = "consumidorArmazenamentoUm";
        final String EXCHANGE_FANOUT = "exchangeFilaMensagemProcessada";
        SerializableImagem imagem = new SerializableImagem();

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(FILA_MENSAGEM_PROCESSADA, false, false, false, null);
        channel.queueBind(FILA_MENSAGEM_PROCESSADA, EXCHANGE_FANOUT, "");

        Consumer consumerImagem = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                // aqui é um artificio pra setar nomes aleatórios para as imagens
                // poderia usar os nomes originais mas isso iria render mais algumas
                // linhas de código poderia ser que não desse tempo para entregar a prova.
                Random ran = new Random();
                int num = ran.nextInt(10000);
                int num2 = ran.nextInt(5000);
                // nome do imagem
                String nome = num+""+num2+".jpg";
                BufferedImage mmm = imagem.getBufferedImage(body);
                String caminhoImagemPretoeBranco = pathDiretorioImagensPretoBranco + nome;
                ImageIO.write(mmm, "jpg", new File(caminhoImagemPretoeBranco));

                // artificio para simular um download
                // faço a thread esperar 10 segundos
                System.out.print("salvando imagem em tons de cinza ");
                String tempoEspera = "..........";
                for (char ch : tempoEspera.toCharArray()) {
                    if (ch == '.') {
                        try {
                            Thread.sleep(1000);
                            System.out.print(".");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                System.out.println(" Concluída!");
            }
        };

        channel.basicConsume(FILA_MENSAGEM_PROCESSADA, true, consumerImagem );
    }
}
