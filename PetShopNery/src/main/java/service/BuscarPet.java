package service;

import entities.Endereco;
import entities.Pet;
import enums.Sexo;
import enums.Tipo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BuscarPet {

    private static final Path PATH_DIR = Paths.get("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados");

    public static List<Pet> buscarPet(Scanner sc) {

        List<Pet> resultados = new ArrayList<>();

        System.out.println("Escolha o tipo do animal (digite o número da opção): ");
        System.out.println("1 - Gato");
        System.out.println("2 - Cachorro");
        System.out.print("R: ");
        int tipoInt = sc.nextInt();
        sc.nextLine();

        Tipo tipoEscolhido = null;
        if (tipoInt == 1) tipoEscolhido = Tipo.GATO;
        else if (tipoInt == 2) tipoEscolhido = Tipo.CACHORRO;

        System.out.println("Escolha o 1º critério:");
        System.out.println("1 - Nome");
        System.out.println("2 - Sexo");
        System.out.println("3 - Idade");
        System.out.println("4 - Peso (kg)");
        System.out.println("5 - Raça");
        System.out.println("6 - Endereço (rua/numero/cidade)");
        System.out.print("R: ");
        int crit1 = sc.nextInt();
        sc.nextLine();
        String valorCrit1 = lerValorDoCriterio(sc, crit1);
        if (valorCrit1 == null) {
            System.out.println("Critério inválido. Busca cancelada.");
            return resultados;
        }

        int crit2 = 0;
        String valorCrit2 = null;

        System.out.println("Deseja adicionar um segundo critério (s/n)?");
        String opcao = sc.nextLine();
        if (opcao.equalsIgnoreCase("s")) {
            System.out.println("Escolha o 2º critério:");
            System.out.println("1 - Nome");
            System.out.println("2 - Sexo");
            System.out.println("3 - Idade");
            System.out.println("4 - Peso (kg)");
            System.out.println("5 - Raça");
            System.out.println("6 - Endereço (rua/numero/cidade)");
            System.out.print("R: ");
            crit2 = sc.nextInt();
            sc.nextLine();
            valorCrit2 = lerValorDoCriterio(sc, crit2);
            if (valorCrit2 == null) {
                System.out.println("Critério inválido. Busca cancelada.");
                return resultados;
            }
        }

        List<Pet> todosPets;
        try {
            todosPets = carregarPetsDosArquivos();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivos dos pets: " + e.getMessage());
            return resultados;
        }

        String valorNormalizado1 = normalizar(valorCrit1);
        String valorNormalizado2 = valorCrit2 != null ? normalizar(valorCrit2) : null;

        for (Pet p : todosPets) {
            if (p.getTipo() == null || p.getTipo() != tipoEscolhido) continue;

            boolean corresponde1 = combinarPorCriterio(p, crit1, valorNormalizado1);
            boolean corresponde2 = (crit2 == 0) ? true : combinarPorCriterio(p, crit2, valorNormalizado2);

            if (corresponde1 && corresponde2) resultados.add(p);
        }

        if (resultados.isEmpty()) {
            System.out.println("\nNenhum resultado encontrado.");
        } else {
            System.out.println("\nResultados encontrados:");
            int i = 1;
            for (Pet r : resultados) {
                System.out.println(formatoLinhaFinal(i++, r));
            }
        }

        return resultados;
    }

    private static String lerValorDoCriterio(Scanner sc, int crit) {
        switch (crit){
            case 1:
                System.out.print("Digite o nome: ");
                return sc.nextLine();
            case 2:
                System.out.print("Digite o sexo: ");
                return sc.nextLine();
            case 3:
                System.out.print("Digite a idade: ");
                return sc.nextLine();
            case 4:
                System.out.print("Digite o peso: ");
                return sc.nextLine();
            case 5:
                System.out.print("Digite a raça: ");
                return sc.nextLine();
            case 6:
                System.out.print("Digite parte do endereço (Rua/Numero/Cidade): ");
                return sc.nextLine();
            default:
                return null;
        }
    }

    private static List<Pet> carregarPetsDosArquivos() throws IOException {
        List<Pet> lista = new ArrayList<>();
        if (!Files.exists(PATH_DIR) || !Files.isDirectory(PATH_DIR)) return lista;

        try (Stream<Path> stream = Files.list(PATH_DIR)) {
            List<Path> arquivos = stream
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .collect(Collectors.toList());

            for (Path file : arquivos) {
                try {
                    List<String> linhas = Files.readAllLines(file, StandardCharsets.UTF_8)
                            .stream().map(String::trim).collect(Collectors.toList());
                    if (linhas.size() < 7) continue;

                    String nome = extrairAposTraco(linhas.get(0));
                    String tipoStr = extrairAposTraco(linhas.get(1));
                    String sexoStr = extrairAposTraco(linhas.get(2));
                    String enderecoLine = extrairAposTraco(linhas.get(3));
                    String idadeLine = extrairAposTraco(linhas.get(4));
                    String pesoLine = extrairAposTraco(linhas.get(5));
                    String raca = extrairAposTraco(linhas.get(6));

                    Tipo tipo = null;
                    Sexo sexo = null;
                    try { tipo = Tipo.valueOf(tipoStr.trim().toUpperCase()); } catch (Exception ignore) {}
                    try { sexo = Sexo.valueOf(sexoStr.trim().toUpperCase()); } catch (Exception ignore) {}

                    String rua = null, numero = null, cidade = null;
                    String[] partes = enderecoLine.split(",");
                    if (partes.length >= 1) rua = partes[0].trim();
                    if (partes.length >= 2) numero = partes[1].trim();
                    if (partes.length >= 3) cidade = partes[2].trim();
                    Endereco endereco = new Endereco(rua, numero, cidade);

                    Double idade = parseDoubleDeTexto(idadeLine);
                    Double peso = parseDoubleDeTexto(pesoLine);

                    Pet pet = new Pet(nome, tipo, sexo, endereco, idade, peso, raca);
                    lista.add(pet);
                } catch (Exception eFile) {
                    System.out.println("Aviso: não foi possível ler o arquivo " + file.getFileName() + " (" + eFile.getMessage() + ")");
                }
            }
        }
        return lista;
    }

    private static String extrairAposTraco(String linha) {
        if (linha == null) return "";
        int idx = linha.indexOf(" - ");
        if (idx >= 0 && idx + 3 < linha.length()) return linha.substring(idx + 3).trim();
        return linha.trim();
    }

    private static Double parseDoubleDeTexto(String s) {
        if (s == null) return null;
        String limpo = s.replaceAll("[^0-9,\\.]", "").replace(",", ".").trim();
        if (limpo.isEmpty()) return null;
        try { return Double.parseDouble(limpo); } catch (NumberFormatException e) { return null; }
    }

    private static String normalizar(String s) {
        if (s == null) return "";
        String semAcento = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toUpperCase().trim();
    }

    private static boolean combinarPorCriterio(Pet pet, int criterio, String valorNormalizado) {
        if (valorNormalizado == null || valorNormalizado.isEmpty()) return false;

        switch (criterio) {
            case 1: // Nome
                String nome = (pet.getNomeSobrenome() != null) ? pet.getNomeSobrenome() : "";
                return normalizar(nome).contains(valorNormalizado);

            case 2: // Sexo
                if (pet.getSexo() == null) return false;
                return normalizar(pet.getSexo().name()).equals(valorNormalizado);

            case 3: // Idade
                try {
                    double idadeReq = Double.parseDouble(valorNormalizado.replace(",", "."));
                    return pet.getIdade() != null && Double.compare(pet.getIdade(), idadeReq) == 0;
                } catch (Exception e) {
                    return false;
                }

            case 4: // Peso
                try {
                    double pesoReq = Double.parseDouble(valorNormalizado.replace(",", "."));
                    return pet.getPeso() != null && Double.compare(pet.getPeso(), pesoReq) == 0;
                } catch (Exception e) {
                    return false;
                }

            case 5: // Raça
                String raca = (pet.getRaca() != null) ? pet.getRaca() : "";
                return normalizar(raca).contains(valorNormalizado);

            case 6: // Endereço
                if (pet.getEndereco() == null) return false;
                String enderecoCompleto = ""
                        + (pet.getEndereco().getRua() != null ? pet.getEndereco().getRua() : "") + " "
                        + (pet.getEndereco().getNumero() != null ? pet.getEndereco().getNumero() : "") + " "
                        + (pet.getEndereco().getCidade() != null ? pet.getEndereco().getCidade() : "");
                return normalizar(enderecoCompleto).contains(valorNormalizado);

            default:
                return false;
        }
    }

    private static String formatoLinhaFinal(int index, Pet p) {
        String endereco = "NÃO INFORMADO";
        if (p.getEndereco() != null) {
            Endereco e = p.getEndereco();
            endereco = (e.getRua() != null ? e.getRua() : "") + ", " + (e.getNumero() != null ? e.getNumero() : "") + " - " + (e.getCidade() != null ? e.getCidade() : "");
        }
        String idadeStr = (p.getIdade() != null) ? formatarDoubleOpcional(p.getIdade()) + " anos" : "NÃO INFORMADO";
        String pesoStr = (p.getPeso() != null) ? formatarDoubleOpcional(p.getPeso()) + "kg" : "NÃO INFORMADO";

        return index + ". " + (p.getNomeSobrenome() != null ? p.getNomeSobrenome() : "NÃO INFORMADO")
                + " - " + (p.getTipo() != null ? p.getTipo().name() : "NÃO INFORMADO")
                + " - " + (p.getSexo() != null ? p.getSexo().name() : "NÃO INFORMADO")
                + " - " + endereco
                + " - " + idadeStr
                + " - " + pesoStr
                + " - " + (p.getRaca() != null ? p.getRaca() : "NÃO INFORMADO");
    }

    private static String formatarDoubleOpcional(Double d) {
        if (d == null) return "NÃO INFORMADO";
        if (d == Math.rint(d)) return String.format("%.0f", d);
        return d.toString();
    }
}
