package com.github.leocarmona.unisinos_dictionary_avl_tree.arvore;

import static com.github.leocarmona.unisinos_dictionary_avl_tree.arvore.TipoDeRotacaoAvl.DIREITA;
import static com.github.leocarmona.unisinos_dictionary_avl_tree.arvore.TipoDeRotacaoAvl.DUPLA_DIREITA;
import static com.github.leocarmona.unisinos_dictionary_avl_tree.arvore.TipoDeRotacaoAvl.DUPLA_ESQUERDA;
import static com.github.leocarmona.unisinos_dictionary_avl_tree.arvore.TipoDeRotacaoAvl.ESQUERDA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Dicionario;

/**
 * Estrutura de dados responsável por representar uma árvore AVL.
 * 
 * @author leonardo.carmona
 *
 */
public class ArvoreAvl implements Serializable {

    /**
     * Número de serialização da árvore AVL.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Dicionário raiz da árvore AVL.
     */
    protected Dicionario      raiz;

    /**
     * Método responsável por adicionar um novo dicionário na árvore AVL.
     * 
     * @param dicionario
     *            Novo dicionário a ser adicionado na árvore AVL.
     */
    public void adicionar(Dicionario dicionario) {
        if (raiz != null) {
            this.adicionar(raiz, dicionario);
        } else {
            raiz = dicionario;
        }
    }

    /**
     * Método responsável por pesquisar um dicionário pela palavra.
     * 
     * @param palavra
     *            Palavra a ser pesquisada.
     * 
     * @return Um dicionário com as definições da palavra.
     */
    public Dicionario pesquisarDicionarioPelaPalavra(String palavra) {
        Dicionario dicionario = raiz;

        while (dicionario != null) {
            final int resultadoDaComparacao = dicionario.compararPalavra(palavra);

            // A palavra é menor que o dicionário atual, logo pesquisar pelo dicionário da esquerda (menor).
            if (resultadoDaComparacao < 0) {
                dicionario = dicionario.getEsquerda();

                continue;
            }

            // A palavra é maior que o dicionário atual, logo pesquisar pelo dicionário da direita (maior).
            if (resultadoDaComparacao > 0) {
                dicionario = dicionario.getDireita();

                continue;
            }

            // Encontrou o dicionário com a palavra atual.
            if (resultadoDaComparacao == 0) {
                return dicionario;
            }
        }

        // Retorna um dicionário com a palavra atual sem difinições. (Padrão para previnir NullPointerException)
        return new Dicionario(palavra);
    }

    /**
     * Método responsável por resgatar o conteúdo da árvore em ordem.
     * 
     * @return Todo o conteúdo da árvore em ordem.
     */
    public List<Dicionario> getConteudo() {
        List<Dicionario> listaDeDicionarios = new ArrayList<>();

        this.percorrerEmOrdem(raiz, listaDeDicionarios);

        return listaDeDicionarios;
    }

    /**
     * Método responsável por adicionar um novo dicionário (nodo) no atual.
     * 
     * @param dicionarioAtual
     *            Dicionário atual.
     * 
     * @param novoDicionario
     *            Novo dicionario a ser adicionado no dicionário atual.
     */
    protected void adicionar(Dicionario dicionarioAtual, Dicionario novoDicionario) {
        final int resultadoDaComparacao = dicionarioAtual.compararPalavra(novoDicionario.getPalavra());

        // O novo dicionário é menor que o dicionário atual (esquerda).
        if (resultadoDaComparacao < 0) {
            // O nodo a adicionar é menor que o nodo atual (esquerda)
            if (dicionarioAtual.getEsquerda() == null) {
                dicionarioAtual.setEsquerda(novoDicionario);
                novoDicionario.setPai(dicionarioAtual);
                this.balancear(novoDicionario);
            } else {
                this.adicionar(dicionarioAtual.getEsquerda(), novoDicionario);
            }

            return;
        }

        // O novo dicionário é maior que o dicionário atual (direita).
        if (resultadoDaComparacao > 0) {
            if (dicionarioAtual.getDireita() == null) {
                dicionarioAtual.setDireita(novoDicionario);
                novoDicionario.setPai(dicionarioAtual);
                this.balancear(novoDicionario);
            } else {
                this.adicionar(dicionarioAtual.getDireita(), novoDicionario);
            }

            return;
        }

        // Ambos os dicionários são iguais, logo devemos unificar as definições.
        if (resultadoDaComparacao == 0) {
            dicionarioAtual.adicionarDefinicoes(novoDicionario.getDefinicoes());
        }
    }

    /**
     * Método responsável por balancear a árvore AVL.
     * 
     * @param dicionarioAtual
     *            Dicionário atual para balancear a árvore AVL.
     */
    protected void balancear(Dicionario dicionarioAtual) {
        switch (dicionarioAtual.getBalanceamento()) {
            // Se o balanceamento for igual a 2, indica que a árvore está desbalanceada.
            case 2:
                if (dicionarioAtual.getEsquerda().getBalanceamento() >= 0) {
                    this.rotacionar(DIREITA, dicionarioAtual);
                } else {
                    this.rotacionar(DUPLA_DIREITA, dicionarioAtual);
                }

                break;

            // Se o balanceamento for igual a -2, indica que a árvore está desbalanceada.
            case -2:
                if (dicionarioAtual.getDireita().getBalanceamento() <= 0) {
                    this.rotacionar(ESQUERDA, dicionarioAtual);
                } else {
                    this.rotacionar(DUPLA_ESQUERDA, dicionarioAtual);
                }

                break;

            // Não faz nada.
            default:
                break;
        }

        // Se o dicionário atual não possuir pai, o dicionário atual passa a ser o pai.
        if (dicionarioAtual.getPai() == null) {
            this.raiz = dicionarioAtual;
        }
        // Se não, balanceia o dicionário pai do dicionário atual.
        else {
            this.balancear(dicionarioAtual.getPai());
        }
    }

    /**
     * Método responsável por rotacionar a árvore AVL atual com o dicionário atual.
     * 
     * @param tipoDeRotacaoAvl
     *            Tipo de rotação a ser feita.
     * 
     * @param dicionarioAtual
     *            Dicionário atual a ser utilizado na rotação.
     */
    protected void rotacionar(TipoDeRotacaoAvl tipoDeRotacaoAvl, Dicionario dicionarioAtual) {
        switch (tipoDeRotacaoAvl) {
            // Rotaciona a árvore para a esquerda.
            case ESQUERDA:
                Dicionario direita = dicionarioAtual.getDireita();
                direita.setPai(dicionarioAtual.getPai());

                dicionarioAtual.setDireita(direita.getEsquerda());

                if (dicionarioAtual.getDireita() != null) {
                    dicionarioAtual.getDireita().setPai(dicionarioAtual);
                }

                direita.setEsquerda(dicionarioAtual);
                dicionarioAtual.setPai(direita);

                if (direita.getPai() != null) {
                    if (direita.getPai().getDireita() == dicionarioAtual) {
                        direita.getPai().setDireita(direita);
                    } else if (direita.getPai().getEsquerda() == dicionarioAtual) {
                        direita.getPai().setEsquerda(direita);
                    }
                }

                break;

            // Rotaciona a árvore para a direita.
            case DIREITA:
                Dicionario esquerda = dicionarioAtual.getEsquerda();
                esquerda.setPai(dicionarioAtual.getPai());

                dicionarioAtual.setEsquerda(esquerda.getDireita());

                if (dicionarioAtual.getEsquerda() != null) {
                    dicionarioAtual.getEsquerda().setPai(dicionarioAtual);
                }

                esquerda.setDireita(dicionarioAtual);
                dicionarioAtual.setPai(esquerda);

                if (esquerda.getPai() != null) {
                    if (esquerda.getPai().getDireita() == dicionarioAtual) {
                        esquerda.getPai().setDireita(esquerda);
                    } else if (esquerda.getPai().getEsquerda() == dicionarioAtual) {
                        esquerda.getPai().setEsquerda(esquerda);
                    }
                }

                break;

            // Rotaciona a árvore duplamente para a esquerda.
            case DUPLA_ESQUERDA:
                this.rotacionar(DIREITA, dicionarioAtual);
                this.rotacionar(ESQUERDA, dicionarioAtual);

                break;

            // Rotaciona a árvore duplamente para a direita.
            case DUPLA_DIREITA:
                this.rotacionar(ESQUERDA, dicionarioAtual);
                this.rotacionar(DIREITA, dicionarioAtual);

                break;

            // Não faz nada.
            default:
                break;
        }
    }

    /**
     * Método responsável por percorrer a raiz em ordem resgatando todos os dicionários dentro da lista desejada.
     * 
     * @param raiz
     *            Raiz desejada a ser percorrida em ordem.
     * 
     * @param listaDeDicionarios
     *            Lista onde será salvo os dicionários em ordem.
     */
    protected void percorrerEmOrdem(Dicionario raiz, List<Dicionario> listaDeDicionarios) {
        if (raiz == null) {
            return;
        }

        this.percorrerEmOrdem(raiz.getEsquerda(), listaDeDicionarios);
        listaDeDicionarios.add(raiz);
        this.percorrerEmOrdem(raiz.getDireita(), listaDeDicionarios);
    }

    /**
     * Método responsável por representar a árvore AVL.
     * 
     * @return A representação da árvore AVL.
     */
    @Override
    public String toString() {
        return "ArvoreAvl [raiz=" + raiz + "]";
    }

}
