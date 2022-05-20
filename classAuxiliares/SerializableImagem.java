package br.com.exemplo.prova.classAuxiliares;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/*
 autore: Anderson Costa Moreira Santana
         Taylan Nalimar Cruz Celestino
*/
public class SerializableImagem implements Serializable {

    /*
    *       Essa classe é usada para fazer a serialização das imagens
    *       além disso tem um metodo para converter imagem colorida
    *       em tons de cinza
    * */

    private byte[] imagem;

    // Construtor
    public SerializableImagem() {

    }

    // Método que cria um lista de nomes de arquivos de um determinado diretório
    public static List listaNomesArquivos(String pathDiretorio) throws Exception {
        List<String> listaNomesImagens = new ArrayList<>();
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathDiretorio));
        for (Path file : stream) {
            listaNomesImagens.add(file.getFileName().toString());
        }
        return listaNomesImagens;
    }

    // Método para converter uma imagem em array de bytes
    public byte[] getSerializableImagem(BufferedImage bufferedImage) throws IOException {
        byte[] imagem;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        imagem = byteArrayOutputStream.toByteArray();
        return imagem;
    }

    // Metodo para converter um array de bytes em imagem
    public static BufferedImage getBufferedImage(byte[] imagem) throws IOException {
        InputStream input = new ByteArrayInputStream(imagem);
        BufferedImage bufferedImage = ImageIO.read(input);
        return bufferedImage;
    }


    // metodo para transformar imagens em tons de cinza
    public BufferedImage imgPretoBranco(BufferedImage original) throws IOException {
        int w = original.getWidth();
        int h = original.getHeight();

        BufferedImage processada = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int colorido = original.getRGB(x, y);
                int cinza = escalaDeCinza(colorido);
                processada.setRGB(x, y, cinza);
            }
        }
        return processada;
    }

    public static int escalaDeCinza(int pixel) {
        int[] rgb = derivar(pixel);
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];
        int cinza = (r + g + b) / 3;
        rgb[0] = cinza;
        rgb[1] = cinza;
        rgb[2] = cinza;
        return integrar(rgb);
    }

    public static int[] derivar(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;
        return new int[]{r, g, b};
    }

    public static int integrar(int[] rgb) {
        int r = (rgb[0] & 0xFF) << 16;
        int g = (rgb[1] & 0xFF) << 8;
        int b = (rgb[2] & 0xFF) << 0;
        return r | g | b;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

}
