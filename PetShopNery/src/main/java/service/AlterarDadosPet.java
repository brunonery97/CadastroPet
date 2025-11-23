package service;

import entities.Endereco;
import entities.Pet;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class AlterarDadosPet {

    private static final Path PATH_DIR = Path.of("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados");

    public static void alterarDadosPet(Scanner sc) {

        List<Pet> resultado = BuscarPet.buscarPet(sc);

        if (resultado.isEmpty()) {
            System.out.println("Nenhum Pet foi encontrado.");
            return;
        }

        System.out.print("Escolha o número do Pet que você deseja alterar: ");
        int numAlterar;
        try {
            numAlterar = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Entrada inválida. Operação cancelada.");
            return;
        }

        if (numAlterar < 1 || numAlterar > resultado.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Pet petParaAlterar = resultado.get(numAlterar - 1);

        Path arquivoOriginal = localizarArquivoPorConteudo(petParaAlterar);
        if (arquivoOriginal == null) {
            System.out.println("Arquivo do Pet não encontrado.");
            return;
        }

        System.out.println("O Pet que você deseja alterar é " + petParaAlterar.getNomeSobrenome() + " (s/n)?");
        String resp = sc.nextLine();
        if (!resp.equalsIgnoreCase("s")) {
            System.out.println("Opção Inválida.");
            return;
        }

        System.out.println("\nO que deseja alterar?");
        System.out.println("1 - Nome");
        System.out.println("2 - Idade");
        System.out.println("3 - Peso (kg)");
        System.out.print("R: ");
        String opcaoStr = sc.nextLine().trim();
        int opcao;
        try {
            opcao = Integer.parseInt(opcaoStr);
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        switch (opcao) {
            case 1:
                System.out.print("Informe o novo Nome e Sobrenome: ");
                String novoNome = sc.nextLine();
                if (novoNome.isEmpty()) {
                    System.out.println("Nome vazio. Nenhuma alteração realizada.");
                    return;
                }
                petParaAlterar.setNomeSobrenome(novoNome);
                break;

            case 2:
                System.out.print("Informe a nova idade: ");
                String idadeStr = sc.nextLine();
                try {
                    double novaIdade = Double.parseDouble(idadeStr.replace(",", "."));
                    petParaAlterar.setIdade(novaIdade);
                } catch (Exception e) {
                    System.out.println("Idade inválida. Nenhuma alteração realizada.");
                    return;
                }
                break;

            case 3:
                System.out.print("Informe o novo peso (Kg): ");
                String pesoStr = sc.nextLine();
                try {
                    double novoPeso = Double.parseDouble(pesoStr.replace(",", "."));
                    petParaAlterar.setPeso(novoPeso);
                } catch (Exception e) {
                    System.out.println("Peso inválido. Nenhuma alteração realizada.");
                    return;
                }
                break;

            default:
                System.out.println("Opção inválida.");
                return;
        }

        System.out.println("\nResumo das alterações:");
        System.out.println(formatarResumoParaConsole(petParaAlterar));
        System.out.print("Deseja salvar as alterações (s/n)? ");
        String confirmar = sc.nextLine();
        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Operação Cancelada.");
            return;
        }

        boolean sucesso = salvarAlteracoesNoArquivo(arquivoOriginal, petParaAlterar);
        if (sucesso) {
            System.out.println("*****PET ATUALIZADO COM SUCESSO*****");
        } else {
            System.out.println("Erro ao salvar alterações.");
        }
    }

    private static Path localizarArquivoPorConteudo(Pet pet) {
        try {
            if (!Files.exists(PATH_DIR) || !Files.isDirectory(PATH_DIR)) return null;

            try (var stream = Files.list(PATH_DIR)) {
                for (Path p : (Iterable<Path>) stream::iterator) {
                    String fname = p.getFileName().toString().toLowerCase();
                    if (!fname.endsWith(".txt")) continue;

                    List<String> linhas = Files.readAllLines(p, StandardCharsets.UTF_8);
                    if (linhas.isEmpty()) continue;

                    String linha1 = linhas.get(0);
                    String nomeConteudo = extrairAposTracoLinha(linha1);

                    if (nomeConteudo.equalsIgnoreCase(pet.getNomeSobrenome())) {
                        boolean valido = true;
                        if (linhas.size() > 1 && pet.getTipo() != null) {
                            String tipoLinha = extrairAposTracoLinha(linhas.get(1));
                            if (!tipoLinha.equalsIgnoreCase(pet.getTipo().name())) valido = false;
                        }
                        if (linhas.size() > 2 && pet.getSexo() != null) {
                            String sexoLinha = extrairAposTracoLinha(linhas.get(2));
                            if (!sexoLinha.equalsIgnoreCase(pet.getSexo().name())) valido = false;
                        }
                        if (valido) return p;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao procurar arquivo por conteúdo: " + e.getMessage());
        }
        return null;
    }

    private static String extrairAposTracoLinha(String linha) {
        if (linha == null) return "";
        int idx = linha.indexOf(" - ");
        if (idx >= 0 && idx + 3 < linha.length()) return linha.substring(idx + 3);
        return linha;
    }

    private static boolean salvarAlteracoesNoArquivo(Path arquivoOriginal, Pet pet) {
        try {
            String rua = pet.getEndereco() != null && pet.getEndereco().getRua() != null ? pet.getEndereco().getRua() : "";
            String numero = pet.getEndereco() != null && pet.getEndereco().getNumero() != null ? pet.getEndereco().getNumero() : "";
            String cidade = pet.getEndereco() != null && pet.getEndereco().getCidade() != null ? pet.getEndereco().getCidade() : "";

            List<String> linhas = List.of(
                    "1 - " + (pet.getNomeSobrenome() != null ? pet.getNomeSobrenome() : ""),
                    "2 - " + (pet.getTipo() != null ? pet.getTipo().name() : ""),
                    "3 - " + (pet.getSexo() != null ? pet.getSexo().name() : ""),
                    "4 - " + (rua + ", " + numero + ", " + cidade).trim(),
                    "5 - " + (pet.getIdade() != null ? pet.getIdade().toString() + " anos" : ""),
                    "6 - " + (pet.getPeso() != null ? pet.getPeso().toString() + " Kg" : ""),
                    "7 - " + (pet.getRaca() != null ? pet.getRaca() : "")
            );

            String nomeArquivoOriginal = arquivoOriginal.getFileName().toString();
            String novoNomeArquivo;
            if (nomeArquivoOriginal.contains("-")) {
                String[] partes = nomeArquivoOriginal.split("-", 2);
                String timestamp = partes[0];
                String nomeSemEspaco = pet.getNomeSobrenome().replace(" ", "").toUpperCase();
                novoNomeArquivo = timestamp + "-" + nomeSemEspaco + ".TXT";
            } else {
                novoNomeArquivo = pet.getNomeSobrenome().replace(" ", "").toUpperCase() + ".TXT";
            }

            Path novoPath = arquivoOriginal.resolveSibling(novoNomeArquivo);

            Files.write(novoPath, linhas, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            if (!novoPath.equals(arquivoOriginal)) {
                Files.deleteIfExists(arquivoOriginal);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo: " + e.getMessage());
            return false;
        }
    }

    private static String formatarResumoParaConsole(Pet p) {
        String endereco = "NÃO INFORMADO";
        if (p.getEndereco() != null) {
            Endereco e = p.getEndereco();
            endereco = (e.getRua() != null ? e.getRua() : "") + ", " + (e.getNumero() != null ? e.getNumero() : "") + " - " + (e.getCidade() != null ? e.getCidade() : "");
        }
        String idadeStr = (p.getIdade() != null) ? (p.getIdade().doubleValue() == Math.rint(p.getIdade()) ? String.format("%.0f", p.getIdade()) : p.getIdade().toString()) + " anos" : "NÃO INFORMADO";
        String pesoStr = (p.getPeso() != null) ? p.getPeso().toString() + " kg" : "NÃO INFORMADO";
        return (p.getNomeSobrenome() != null ? p.getNomeSobrenome() : "NÃO INFORMADO")
                + " - " + (p.getTipo() != null ? p.getTipo().name() : "NÃO INFORMADO")
                + " - " + (p.getSexo() != null ? p.getSexo().name() : "NÃO INFORMADO")
                + " - " + endereco
                + " - " + idadeStr
                + " - " + pesoStr
                + " - " + (p.getRaca() != null ? p.getRaca() : "NÃO INFORMADO");
    }
}

