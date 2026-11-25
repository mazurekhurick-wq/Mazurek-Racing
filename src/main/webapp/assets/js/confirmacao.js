/*
 * Abre o modal de confirmação antes de excluir.
 * O botão "Excluir" da tabela precisa ter:
 *   data-form = id do <form> que faz o POST de exclusão
 *   data-nome = texto mostrado no modal (ex.: nome do piloto)
 */
function confirmarExclusao(botao) {
    const form = document.getElementById(botao.dataset.form);

    document.getElementById('modalExcluirTexto').textContent = botao.dataset.nome;
    document.getElementById('modalExcluirBotao').onclick = function () {
        form.submit();
    };

    new bootstrap.Modal(document.getElementById('modalExcluir')).show();
}
