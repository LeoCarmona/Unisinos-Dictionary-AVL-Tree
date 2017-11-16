package com.github.leocarmona.unisinos_dictionary_avl_tree;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * Dicionário contendo a palavra em inglês e suas respectivas definições.
 * 
 * @author leonardo.carmona
 *
 */
public class Dicionario implements Serializable {

    /**
     * Número de serialização do dicionário.
     */
    private static final long     serialVersionUID = 1L;

    /**
     * Collator responsável pela comparação das palavras sem acento.
     */
    private static final Collator COLLATOR;

    /**
     * Construtor para inicialização dos atributos estáticos.
     */
    static {
        COLLATOR = Collator.getInstance(Locale.US);
        COLLATOR.setStrength(Collator.PRIMARY);
    }

    /**
     * Palavra no idioma inglês.
     */
    protected String       palavra;

    /**
     * Lista que irá armazenar as possíveis traduções para a palavra em inglês.
     */
    protected List<String> definicoes;

    /**
     * Dicionário pai.
     */
    private Dicionario     pai;

    /**
     * Dicionário da esquerda.
     */
    private Dicionario     esquerda;

    /**
     * Dicionário da direita.
     */
    private Dicionario     direita;

    /**
     * Construtor responsável por inicializar o dicionário da palavra.
     * 
     * @param palavra
     *            Palavra a ser inserida.
     * 
     * @param definicoes
     *            Definições da palavra.
     */
    public Dicionario(String palavra, List<String> definicoes) {
        this.palavra = palavra;

        if (definicoes != null) {
            this.definicoes = definicoes;
        } else {
            this.definicoes = new ArrayList<String>();
        }
    }

    /**
     * Construtor responsável por inicializar o dicionário da palavra.
     * 
     * @param palavra
     *            Palavra a ser inserida.
     */
    public Dicionario(String palavra) {
        this.palavra = palavra;
        this.definicoes = new ArrayList<String>();
    }

    /**
     * Construtor responsável por inicializar o dicionário da palavra.
     */
    public Dicionario() {
        this.definicoes = new ArrayList<String>();
    }

    /**
     * Método responsável por adicionar novas definições a palavra atual.
     * 
     * @param novasDefinicoes
     *            Novas definições a serem adicionadas.
     * 
     * @return True caso as definições sejam adicionadas com sucesso. Caso contrário, false.
     */
    public boolean adicionarDefinicoes(List<String> novasDefinicoes) {
        if (novasDefinicoes == null) {
            return false;
        }

        List<String> definicoes = this.getDefinicoes();
        boolean adicionou = false;
        Dicionario.Utils.ajustarDefinicoes(novasDefinicoes);

        for (String novaDefinicao : novasDefinicoes) {
            boolean jaPossuiDefinicao = false;

            // Verifica se a nova definição já existe. Caso exista, continua tentando adicionar novas definições.
            for (String definicao : definicoes) {
                if (COLLATOR.compare(definicao, novaDefinicao) == 0) {
                    jaPossuiDefinicao = true;

                    break;
                }
            }

            // Se não possui definição, adiciona a nova definição
            if (!jaPossuiDefinicao) {
                this.getDefinicoes().add(novaDefinicao);
                // adicionou = this.getDefinicoes().add(novaDefinicao) || adicionou;
            }
        }

        return adicionou;
    }

    /**
     * Método responsável por adicionar uma nova definição para a palavra atual.
     * 
     * @param definicao
     *            Definição a ser adicionada.
     * 
     * @return True caso a definição seja adicionada com sucesso. Caso contrário, false.
     */
    public boolean adicionarDefinicao(String definicao) {
        if (StringUtils.isBlank(definicao)) {
            return false;
        }

        definicao = definicao.trim();

        final List<String> definicoes = this.getDefinicoes();

        // Verifica se a definição já existe. Caso exista, retorna false. (Não adicionado)
        for (String _definicao : definicoes) {
            if (COLLATOR.compare(_definicao, definicao) == 0) {
                return false;
            }
        }

        boolean adicionou = definicoes.add(definicao);

        // Se adicionou, re-ordena as definições do dicionário.
        if (adicionou) {
            Collections.sort(definicoes);
        }

        return adicionou;
    }

    /**
     * Método responsável por remover uma definição.
     * 
     * @param definicao
     *            Definição a ser removida.
     * 
     * @return True se a definição foi removida. Caso contrário, false.
     */
    public boolean removerDefinicao(String definicao) {
        boolean removeu = false;

        for (String _definicao : this.getDefinicoes()) {
            if (COLLATOR.compare(_definicao, definicao) == 0) {
                // Remove todas as definições ignorando acentos, letras maiusculas e minusculas.
                removeu = this.getDefinicoes().remove(_definicao) || removeu;
            }
        }

        return removeu;
    }

    /**
     * Método responsável por indicar se a palavra atual possui definições.
     * 
     * @return True se possuir definições. Caso contrário, false.
     * 
     * @see #getPalavra()
     * @see #getDefinicoes()
     */
    public boolean possuiDefinicoes() {
        return this.getDefinicoes() != null && this.getDefinicoes().size() > 0;
    }

    /**
     * Método responsável por indicar se o dicionário atual é o dicionário pai (raiz).
     * 
     * @return Retorna true se o dicionário atual for o dicionário pai (raiz). Caso contrário, false.
     */
    public boolean ehDicionarioPai() {
        return this.getPai() == null;
    }

    /**
     * Método responsável por comparar a palavra do dicionário. Ignora acentos e letras maiúsculas e minusculas.
     * 
     * @param palavraComparada
     *            Palavra a ser comparada.
     * 
     * @return -1 se a palavra for menor que a do dicionário; 0 se igual; 1 se a palavra for maior que a do dicionário.
     */
    public int compararPalavra(String palavraComparada) {
        return COLLATOR.compare(this.getPalavra(), palavraComparada);
    }

    /**
     * Método responsável por calcular a altura do dicionário atual.
     * 
     * @return A altura do dicionário atual.
     */
    public int getAltura() {
        final Dicionario esquerda = this.getEsquerda();
        final Dicionario direita = this.getDireita();

        // Se não existirem os dicionários da esquerda e da direita, a altura deve ser 0.
        if (esquerda == null && direita == null) {
            return 0;
        }

        // Se somente existir o dicionário da direita, a altura será 1 mais a altura do dicionário da direita.
        if (esquerda == null && direita != null) {
            return 1 + direita.getAltura();
        }

        // Se somente existir o dicionário da esquerda, a altura será 1 mais a altura do dicionário da dicionário.
        if (esquerda != null && direita == null) {
            return 1 + esquerda.getAltura();
        }

        // Se ambos os dicionários existirem, a altura será 1 mais a altura do maior dicionário.
        return 1 + Math.max(esquerda.getAltura(), direita.getAltura());
    }

    /**
     * Método responsável por resgatar o balanceamento do dicionário.
     * 
     * @return O balanceamento do dicionário.
     */
    public int getBalanceamento() {
        int alturaEsquerda = this.getEsquerda() == null ? 0 : this.getEsquerda().getAltura();
        int alturaDireita = this.getDireita() == null ? 0 : this.getDireita().getAltura();

        return alturaEsquerda - alturaDireita;
    }

    /**
     * Método responsável por resgatar a palavra atual.
     * 
     * @return A palavra atual.
     */
    public String getPalavra() {
        return palavra;
    }

    /**
     * Método responsável por alterar a palavra atual.
     * 
     * @param palavra
     *            Nova palavra a ser alterada.
     */
    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    /**
     * Método responsável por resgatar as definições da palavra atual.
     * 
     * @return As definições da palavra atual.
     * 
     * @see getPalavra()
     */
    public List<String> getDefinicoes() {
        return definicoes;
    }

    /**
     * Método responsável por alterar as definições da palavra atual.
     * 
     * @param definicoes
     *            As novas definições da palavra atual.
     * 
     * @see getPalavra()
     */
    public void setDefinicoes(List<String> definicoes) {
        if (definicoes != null) {
            this.definicoes = definicoes;
        } else {
            this.definicoes = new ArrayList<>();
        }
    }

    /**
     * Método responsável por resgatar o dicionário pai.
     * 
     * @return O dicionário pai.
     */
    public Dicionario getPai() {
        return pai;
    }

    /**
     * Método responsável por alterar o dicionário pai.
     * 
     * @param pai
     *            Novo dicionário pai.
     */
    public void setPai(Dicionario pai) {
        this.pai = pai;
    }

    /**
     * Método responsável por resgatar o dicionário da esquerda.
     * 
     * @return O dicionário da esquerda.
     */
    public Dicionario getEsquerda() {
        return esquerda;
    }

    /**
     * Método responsável por alterar o dicionário da esquerda.
     * 
     * @param esquerda
     *            Novo dicionário da esquerda.
     */
    public void setEsquerda(Dicionario esquerda) {
        this.esquerda = esquerda;
    }

    /**
     * Método responsável por resgatar o dicionário da direita.
     * 
     * @return O dicionário da direita.
     */
    public Dicionario getDireita() {
        return direita;
    }

    /**
     * Método responsável por alterar o dicionário da direita.
     * 
     * @param direita
     *            Novo dicionário da direita.
     */
    public void setDireita(Dicionario direita) {
        this.direita = direita;
    }

    /**
     * Método responsável por representar o dicionário.
     * 
     * @return A representação do dicionário.
     */
    @Override
    public String toString() {
        return "Dicionario [palavra=" + palavra + ", definicoes=" + definicoes + ", esquerda=" + esquerda + ", direita=" + direita + "]";
    }

    /**
     * Classe responsável por fornecer utilitários para a classe {@link Dicionario}.
     * 
     * @author leonardo.carmona
     *
     */
    public static class Utils {

        /**
         * Método responsável por ajustar as definições.
         * 
         * @param definicoes
         *            Definições a serem ajustadas.
         */
        public static void ajustarDefinicoes(List<String> definicoes) {
            for (int i = 0; i < definicoes.size(); i++) {
                if (StringUtils.isBlank(definicoes.get(i))) {
                    definicoes.remove(i--);
                } else {
                    definicoes.set(i, definicoes.get(i).trim());
                }
            }
        }

    }

}
