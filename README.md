API de Clientes
======================================
Sistema Api de Clientes - nome do projeto = segurosunimed-test.


Introdução
-----------------------------------------
API dedicada a atender as necessidades dos processos de clientes e seus respectivos endereços.


Documentação API
-----------------------------------------
SWAGGER
http://localhost:8080/swagger-ui.html


Rodando o sistema
-----------------------------------------
Ao buildar o projeto já terá alguns registros salvos no banco H2 (dbService.instanciarDataBase()), deve ser chamado o
endpoint /login para geração de token manual. Deve-se utilizar o valor de user "lucas.t.banin@gmail.com" ou
"gustavo.t.banin@gmail.com" neste endpoint /login ou então criar um usuario manualmente no endpoint /usuarios para
posterior geração de token. Com esse token em mãos para cada endpoint solicitado abaixo utilizar no header o campo token
e o valor do token recuperado no endpoint /login.
- Cadastrar um cliente junto aos seus endereços;
- Atualizar os dados cadastrais de um cliente;
- Deletar um cliente;
- Listar todos os clientes cadastrados;
- Listar clientes de acordo com filtro de nome, email e genero;
- Listar todos os clientes cadastrados paginado;
- Buscar cliente por id;
- Buscar cliente por nome;
- Buscar cliente por email;
- Listar clientes por genero (M ou F);