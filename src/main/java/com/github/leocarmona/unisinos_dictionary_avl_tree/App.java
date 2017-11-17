package com.github.leocarmona.unisinos_dictionary_avl_tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Dicionario;
import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Tradutor;

/**
 * Classe responsável pela inicialização externa do tradutor.
 * 
 * @author leonardo.carmona
 *
 */
public class App {

    private static final String   DIVISOR           = "==================================================";
    private static final Tradutor TRADUTOR          = new Tradutor();
    private static final File     DICIONARIO_PADRAO = new File("dicionario.dat");
    private static final Scanner  TECLADO           = new Scanner(System.in);

    /**
     * Ponto de entrada inicial do aplicativo.
     * 
     * @param args
     *            Argumentos para o aplicativo.
     */
    public static void main(String[] args) {
        App.carregarDicionarioPadrao();

        while (true) {
            App.exibirMenu();
        }
    }

    /**
     * Método responsável por carregar o dicionário padrão.
     */
    private static void carregarDicionarioPadrao() {
        System.out.println("Carregando o dicionário de: " + DICIONARIO_PADRAO.getAbsolutePath());

        if (DICIONARIO_PADRAO.exists()) {
            TRADUTOR.carregaDicionario(DICIONARIO_PADRAO.getAbsolutePath());
        } else {
            TRADUTOR.insereTraducao("be", new ArrayList<>(Arrays.asList("ser", "estar", "haver", "ficar", "existir")));
            TRADUTOR.insereTraducao("i", new ArrayList<>(Arrays.asList("eu")));
            TRADUTOR.insereTraducao("am", new ArrayList<>(Arrays.asList("sou")));

            salvarDicionario(false);
        }

        System.out.println("");
    }

    /**
     * Método responsável por exibir o menu.
     */
    private static void exibirMenu() {
        System.out.println(DIVISOR);
        System.out.println("Insira uma das opções:\n");
        System.out.println("1) Traduzir palavra");
        System.out.println("2) Inserir definição");
        System.out.println("3) Alterar definição");
        System.out.println("4) Remover definição");
        System.out.println("5) Listar todas as definições");
        System.out.println("6) Salvar dicionário");
        System.out.println("0) Sair");

        int resposta;

        while (true) {
            boolean erro = false;

            System.out.print("\nOpção: ");

            try {
                resposta = TECLADO.nextInt();
            } catch (Exception e) {
                TECLADO.next();
                erro = true;
                resposta = -1;
            }

            if (erro || resposta < 0 || resposta > 6) {
                continue;
            }

            break;
        }

        switch (resposta) {
            case 1:
                traduzirPalavra();
                break;

            case 2:
                inserirDefinicao();
                break;

            case 3:
                alterarDefinicao();
                break;

            case 4:
                removerDefinicao();
                break;

            case 5:
                listarDefinicoes();
                break;

            case 6:
                salvarDicionario(true);
                break;
        }

        if (resposta == 0) {
            System.out.print("Deseja salvar o dicionário antes de sair (s/n)? ");
            
            while (true) {
                String opc = TECLADO.next().toLowerCase();

                if ("s".equals(opc)) {
                    App.salvarDicionario(true);
                    break;
                }

                if ("n".equals(opc)) {
                    break;
                }
            }

            System.out.println("\n" + DIVISOR + "\n");

            System.exit(0);
        }

        System.out.println("\n" + DIVISOR + "\n");
    }

    /**
     * Método responsável por traduzir uma palavra.
     */
    private static void traduzirPalavra() {
        System.out.print("\nPalavra a ser traduzida: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = TRADUTOR.traduzPalavra(palavra);

        if (!definicoes.isEmpty()) {
            System.out.println("\nTraduções existentes: " + definicoes);
        } else {
            System.out.println("\nNão há traduções para a palavra '" + palavra + "'.");
            System.out.print("Deseja adicionar novas definições (s/n)? ");

            while (true) {
                String opc = TECLADO.next().toLowerCase();

                if ("s".equals(opc)) {
                    List<String> novasDefinicoes = App.perguntarDefinicoes();

                    TRADUTOR.insereTraducao(palavra, novasDefinicoes);

                    break;
                }

                if ("n".equals(opc)) {
                    break;
                }
            }
        }
    }

    /**
     * Método responsável por inserir novas definições.
     */
    private static void inserirDefinicao() {
        System.out.print("\nPalavra a ser traduzida: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = App.perguntarDefinicoes();

        TRADUTOR.insereTraducao(palavra, definicoes);
    }

    /**
     * Método responsável por alterar uma definição.
     */
    private static void alterarDefinicao() {
        System.out.print("\nPalavra: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = TRADUTOR.traduzPalavra(palavra);

        if (definicoes.isEmpty()) {
            System.out.println("A palavra '" + palavra + "' não possui definições.");
        } else {
            System.out.println("Definições existentes: " + definicoes);
            System.out.print("Alterar definição: ");
            
            String definicao = TECLADO.next();
            
            int indice = -1;
            
            for (int i = 0; i < definicoes.size(); i++) {
                if (definicao.equals(definicoes.get(i))) {
                    indice = i;
                    break;
                }
            }

            if (indice > -1) {
                System.out.print("Nova definição: ");
                String novaDefinicao = TECLADO.next();
                
                definicoes.remove(indice);
                TRADUTOR.insereTraducao(palavra, new ArrayList<>(Arrays.asList(novaDefinicao)));
                
                System.out.println("A definição '" + definicao + "' foi alterada para '" + novaDefinicao + "' com sucesso!");
            } else {
                System.out.println("A definição '" + definicao + "' não foi encontrada!");
            }
        }
    }

    /**
     * Método responsável por remover uma definição.
     */
    private static void removerDefinicao() {
        System.out.print("\nPalavra: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = TRADUTOR.traduzPalavra(palavra);

        if (definicoes.isEmpty()) {
            System.out.println("A palavra '" + palavra + "' não possui definições.");
        } else {
            System.out.println("Definições possíveis: " + definicoes);
            System.out.print("Remover definição: ");
            palavra = TECLADO.next().trim().replaceAll("\\s+", " ").toLowerCase();

            if (definicoes.remove(palavra)) {
                System.out.println("A definição '" + palavra + "' foi removida com sucesso!");
            } else {
                System.out.println("A definição '" + palavra + "' não foi encontrada!");
            }
        }
    }

    /**
     * Método responsável por listar as definições
     */
    private static void listarDefinicoes() {
        System.out.print("\nDefinições existentes: ");

        List<Dicionario> dicionarios = TRADUTOR.getConteudo();

        if (dicionarios.isEmpty()) {
            System.out.print("O dicionário está vazio!");
        } else {
            System.out.print(dicionarios.size() + "\n\n");

            for (Dicionario dicionario : dicionarios) {
                System.out.println("Palavra: [" + dicionario.getPalavra() + "]\t\tDefinições: " + dicionario.getDefinicoes());
            }
        }
    }

    /**
     * Método responsável por perguntar as definições.
     * 
     * @return As definições.
     */
    private static List<String> perguntarDefinicoes() {
        List<String> definicoes = new ArrayList<>();

        do {
            System.out.print("Definições separadas por vírgula: ");
            definicoes = new ArrayList<>(Arrays.asList(TECLADO.next().split(",")));

            Dicionario.Utils.ajustarDefinicoes(definicoes);
        } while (definicoes.isEmpty());

        return definicoes;
    }

    /**
     * Método responsável por salvar o dicionário.
     * 
     * @param exibeMensagem
     *            Se true, exibe a mensagem. Caso contrário, false.
     */
    private static void salvarDicionario(boolean exibeMensagem) {
        TRADUTOR.salvaDicionario(DICIONARIO_PADRAO.getAbsolutePath());

        if (exibeMensagem) {
            System.out.println("\nDicionário salvo com sucesso em " + DICIONARIO_PADRAO.getAbsolutePath() + "");
        }
    }

}
