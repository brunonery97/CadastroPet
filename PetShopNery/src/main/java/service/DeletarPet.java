package service;

import entities.Pet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class DeletarPet {

    private static final Path PATH_DIR = Path.of("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados");

    public static void deletarPet(Scanner sc){

        List<Pet> resultado = BuscarPet.buscarPet(sc);
        System.out.println();

        if (resultado.isEmpty()){
            System.out.println("Nenhum pet encontrado com essas caracteristicas.");
            return;
        }

        System.out.print("Escolha o número do Pet que você deseja deletar: ");
        int numDelet = sc.nextInt();
        sc.nextLine();
        if (numDelet < 1 || numDelet > resultado.size()){
            System.out.println("Opção inválida.");
            return;
        }

        Pet petParaDeletar = resultado.get(numDelet - 1);
        String nomeArquivoPet = petParaDeletar.getNomeSobrenome().replace(" ", "").toUpperCase();

        System.out.println("O Pet que você deseja deletar é " + nomeArquivoPet + " (s/n)?");
        String resp = sc.nextLine();
        if (resp.equalsIgnoreCase("s")) {

            try {
                Path arquivoParaDeletar = Files.list(PATH_DIR)
                        .filter(p -> p.getFileName().toString().toUpperCase()
                                .endsWith("-" + nomeArquivoPet + ".TXT"))
                        .findFirst()
                        .orElse(null);

                if (arquivoParaDeletar != null) {
                    Files.delete(arquivoParaDeletar);
                    System.out.println("*****PET DELETADO*****");
                } else {
                    System.out.println("*****ARQUIVO NÃO ENCONTRADO*****");
                }

            } catch (Exception e) {
                System.out.println("Erro ao deletar o pet: " + e.getMessage());
            }
        } else {
            System.out.println("PET NÃO DELETADO, ESCOLHA UMA NOVA OPÇÃO.");
        }
    }
}