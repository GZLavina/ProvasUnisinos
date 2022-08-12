Desenvolvido como avaliação para a disciplina de
Algoritmos e Programação: Estruturas Lineares

 - Objetivos:
 * Desenvolver um programa que simule o funcionamento de
um banco de dados, recebendo instruções SQL e produzindo 
o resultado correto. Por exemplo: 
"create table cidades (nome text,UF text,cod int)" deve gerar
uma tabela de cidades com as colunas "nome" (texto), "UF" (texto), 
e um código (inteiro).
 * Também deve ser possível inserir, remover e visualizar valores
nestas tabelas.
 * A remoção deve permitir que sejam excluídos diversos valores
que satisfaçam a condição dada.
 * Para a visualização (ss.query), é necessário que apareçam
apenas as colunas desejadas pelo usuário (* para ver todas).
 * É necessário que se trate qualquer tipo de exceção gerada por
instruções mal-formatadas.

 - IMPORTANTE:
 * O método connect(String nomeBaseDados) sempre carrega o arquivo
com o nome passado como parâmetro (se existir). Logo, executar 
a classe de teste múltiplas vezes com o mesmo nome de arquivo 
gerará repetições daqueles valores que não forem removidos durante
a execução.
 * Para economizar tempo, não era necessário programar
interação com o usuário. A classe de teste produz o retorno
de todas as funções disponíveis.