package service;

import entities.Pet;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArquivoPet {

    public static void salvarPetArquivoTXT (Pet pet){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        String nomePet = pet.getNomeSobrenome();
        String nomeArquivo = time + "-" + nomePet.toUpperCase().replace(" ", "") + ".TXT";

        List<String> linhas = new ArrayList<>();

        linhas.add("1 - " + pet.getNomeSobrenome());
        linhas.add("2 - " + pet.getTipo());
        linhas.add("3 - " + pet.getSexo());
        linhas.add("4 - " + pet.getEndereco().getRua() + ", " + pet.getEndereco().getNumero() + ", " + pet.getEndereco().getCidade());
        linhas.add("5 - " + pet.getIdade() + " anos");
        linhas.add("6 - " + pet.getPeso() + " Kg");
        linhas.add("7 - " + pet.getRaca());

        Path path = Path.of("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados\\" + nomeArquivo);
        try {
            Files.write(path, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
