package com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.leocarmona.unisinos_dictionary_avl_tree.arvore.ArvoreAvl;

/**
 * Classe responsável por traduzir as palavras de uma @l{@link ArvoreAvl} de {@link Dicionario}.
 * 
 * @author leonardo.carmona
 * 
 * @see ArvoreAvl
 * @see Dicionario
 */
public class Tradutor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Árvore AVl de tradução.
     */
    protected ArvoreAvl       arvore;

    /**
     * Construtor responsável por inicializar o tradutor a partir de um arquivo.
     * 
     * @param arq
     *            Nome do arquivo a ser carregado.
     */
    public Tradutor(String arq) {
        this.arvore = new ArvoreAvl();

        this.carregaDicionario(arq);
    }

    /**
     * Construtor responsável por inicializar um tradutor sem palavras.
     */
    public Tradutor() {
        this.arvore = new ArvoreAvl();
    }

    /**
     * Método responsável por traduzir uma palavra.
     * 
     * @param palavra
     *            Palavra a ser traduzida.
     * 
     * @return As traduções da palavra desejada.
     */
    public List<String> traduzPalavra(String palavra) {
        return arvore.pesquisarDicionarioPelaPalavra(palavra).getDefinicoes();
    }

    /**
     * Método responsável por inserir uma nova tradução.
     * 
     * @param palavra
     *            Palavra em inglês.
     * 
     * @param definicoes
     *            Respectivas definições.
     */
    public void insereTraducao(String palavra, List<String> definicoes) {
        this.arvore.adicionar(new Dicionario(palavra, definicoes));
    }

    /**
     * Método responsável por salvar o arquivo de dicionário.
     * 
     * @param arq
     *            Arquivo onde será salvo o dicionário.
     */
    public void salvaDicionario(String arq) {
        FileWriter fw = null;

        try {
            File arquivo = new File(arq);
            File caminho = arquivo.getParentFile();

            if (caminho != null) {
                caminho.mkdirs();
            }

            fw = new FileWriter(arquivo);
            List<Dicionario> conteudo = this.arvore.getConteudo();

            for (Dicionario dicionario : conteudo) {
                StringBuilder linha = new StringBuilder("<palavra_ingles>").append(dicionario.getPalavra());
                int contador = 1;

                for (String traducao : dicionario.getDefinicoes()) {
                    linha.append("<traducao").append(contador++).append(">").append(traducao);
                }

                fw.write(linha.append("\n").toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método responsável por carregar o dicionário para a árvore AVL.
     * 
     * @param arq
     *            Nome do arquivo a ser carregado.
     * 
     * @return True se o dicionário foi carregado. Caso contrário, false.
     */
    public boolean carregaDicionario(String arq) {
        try {
            int linhaCount = 1;

            for (String linha : FileUtils.readLines(new File(arq), "UTF-8")) {
                Dicionario dicionario = lerLinhaDoArquivo(linha);

                if (dicionario != null) {
                    this.insereTraducao(dicionario.getPalavra(), dicionario.getDefinicoes());
                } else {
                    System.err.println("Não foi encontrado uma palavra em inglês na linha " + linhaCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    protected Dicionario lerLinhaDoArquivo(String linha) {
        String palavra = Tradutor.Utils.getPalavraEntre(linha, "<palavra_ingles>", "<traducao1>"), traducao, palavraInicio, palavraFim;
        List<String> definicoes;
        int traducaoAtual;

        if (palavra == null) {
            return null;
        }

        definicoes = new ArrayList<>();
        palavraInicio = "<traducao1>";
        palavraFim = "<traducao2>";
        traducaoAtual = 2;

        while ((traducao = Tradutor.Utils.getPalavraEntre(linha, palavraInicio, palavraFim)) != null) {
            definicoes.add(traducao);

            palavraInicio = "<traducao" + (traducaoAtual++) + ">";
            palavraFim = "<traducao" + traducaoAtual + ">";
        }

        return new Dicionario(palavra, definicoes);
    }

    /**
     * Método responsável por representar o tradutor.
     * 
     * @return A representação do tradutor.
     */
    @Override
    public String toString() {
        return "Tradutor [arvore=" + arvore + "]";
    }

    /**
     * Classe responsável por fornecer utilitários para a classe {@link Tradutor}.
     * 
     * @author leonardo.carmona
     *
     */
    public static class Utils {

        /**
         * Método responsável por resgatar uma palavra entre duas palavras.
         * 
         * @param linha
         *            Linha.
         * 
         * @param palavraInicio
         *            Palavra inicial.
         * 
         * @param palavraFim
         *            Palavra final
         * 
         * @return Se encontrar, retorna a palavra. Caso contrário, null.
         */
        public static String getPalavraEntre(String linha, String palavraInicio, String palavraFim) {
            int inicio = linha.lastIndexOf(palavraInicio);
            int fim;

            if (inicio < 0) {
                return null;
            }

            inicio += palavraInicio.length();
            fim = linha.indexOf(palavraFim);

            if (fim < 0) {
                return linha.substring(inicio, linha.length());
            }

            return linha.substring(inicio, fim);
        }

    }

}
