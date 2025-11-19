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

        
    }
}