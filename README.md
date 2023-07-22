# Back-end do projeto bibilhoteca-rest
## Esse é uma API desenvolvida com a arquitetura REST. No back-end: Java EE 8 com EJB3, RESTEasy (JAX-RS), Hibernate/JPA e o gerenciador de dependências Maven. O banco de dados é o MySQL 8.



## Regras de negócio e validações
As regras de negócio do projeto de Gerenciamento de Biblioteca são as seguintes:

### 1 - Cadastro de Livros:
   Ao cadastrar um novo livro (endpoint POST /api/livros), os campos obrigatórios titulo, autor e disponivel devem ser fornecidos.
   O campo disponivel indica se o livro está disponível para empréstimo (true) ou se já está emprestado (false) no momento do cadastro.
   
### 2 - Realização de Empréstimo:

Ao realizar um empréstimo (endpoint POST /api/emprestimos), deve-se fornecer o usuario_id e o livro_id.
Verificar se o livro está disponível para empréstimo (disponivel = true). Caso contrário, retornar um erro adequado informando que o livro não está disponível para empréstimo no momento.

## 3 - Realização de Devolução:

Ao realizar a devolução de um livro (endpoint PUT /api/emprestimos/{emprestimoId}), atualizar a data de devolução (data_devolucao) para a data atual.
Verificar se o empréstimo com o ID fornecido (emprestimoId) existe e se está associado ao usuário correto. Caso contrário, retornar um erro adequado informando que o empréstimo não foi encontrado ou não pertence ao usuário em questão.
Listagem de Livros:

Ao listar todos os livros (endpoint GET /api/livros), retornar uma lista com as informações dos livros da biblioteca.

4 - Detalhes de um Livro:

Ao obter informações de um livro específico (endpoint GET /api/livros/{livroId}), retornar detalhes específicos desse livro, identificado pelo livroId.

5 - Validar Requisições:

Validar as requisições recebidas pela API para garantir que os campos obrigatórios estão sendo fornecidos corretamente.
Retornar respostas HTTP apropriadas (por exemplo, código 400 Bad Request) com mensagens de erro descritivas em caso de falhas nas validações.

6 - Tratamento de Erros:

Implementar um tratamento adequado para os erros, garantindo que as respostas de erro incluam informações úteis para o cliente, como mensagens descritivas sobre o problema ocorrido.
Utilizar códigos de status HTTP apropriados para cada tipo de erro (por exemplo, código 404 Not Found quando um recurso não for encontrado).

7 - Cadastro de Usuários:

Ao cadastrar um novo usuário (endpoint POST /api/usuarios), o campo nome deve ser fornecido como obrigatório.
Verificar se já existe um usuário com o mesmo nome cadastrado na base de dados. Caso já exista, retornar um erro informando que o nome de usuário já está em uso.


8 - Empréstimos Expirados:

Ao listar os empréstimos em andamento (por exemplo, endpoint GET /api/emprestimos), verificar se algum empréstimo possui a data de devolução (data_devolucao) expirada (data atual maior que a data de devolução).
Caso existam empréstimos expirados, retornar essas informações na resposta da API para que a biblioteca possa tomar as providências adequadas.




## Relacionamento das tabelas MySQL:

1 - One-to-Many (User-to-Loans): Cada usuário pode fazer vários empréstimos.

2 - Many-to-One (Loans-to-User e Loans-to-Books): Um empréstimo é feito por um único usuário e está associado a um único livro.

3 - Many-to-Many (Users-to-Books): Cada usuário pode emprestar vários livros e um livro pode ser emprestado por vários usuários.


## URLs:
|  URL |  Método | Descrição |
|----------|--------------|--------------|
|`http://localhost:8080/apostas-backend/api/apostas`                                 | GET | Retorna todas as apostas salvas no banco de dados |
|`http://localhost:8080/apostas-backend/api/apostas`                                 | POST | Salva uma aposta no banco de dados |
|`http://localhost:8080/apostas-backend/api/apostas/id`                              | GET | Retorna a aposta com o ID do parâmetro da URL |
|`http://localhost:8080/apostas-backend/api/apostas/id`                              | DELETE | Deleta o registro da aposta com o ID do parâmetro da URL |
|`http://localhost:8080/apostas-backend/api/apostas/id`                              | PUT | Atualiza o registro da aposta com o ID numérico do parâmetro da URL|



## Para executar o projeto:
O projeto foi desenvolvido para funcionar no servidor de aplicações WildFly 19, então algumas configurações extras são necessárias.
Na pasta do WildFly, ir até modules\layers\base\com e criar uma pasta chamada mysql, depois outra com o nome main.
O caminho completo fica assim: ...\wildfly-19.1.0.Final\modules\system\layers\base\com\mysql\main.
Baixar o arquivo jar do mysql-connector no [repositório do Maven](https://mvnrepository.com/artifact/mysql/mysql-connector-java) e colar na pasta main.
Ainda na pasta main, criar um arquivo chamado module.xml e colar o seguinte texto dentro:

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.0" name="com.mysql">
	<resources>
	  <resource-root path="mysql-connector-java-8.0.21.jar" />
	</resources>
	<dependencies>
	  <module name="javax.api"/>
	  <module name="javax.transaction.api"/>
	</dependencies>
</module>
```

É necessário adaptar a linha abaixo para a versão do MySQL que foi baixada
```
<resource-root path="mysql-connector-java-8.0.21.jar" />
```

Após isso, ir no diretório wildfly-19.1.0.Final\standalone\configuration e abrir o arquivo standalone.xml.
Dentro da tag datasources, adicione o seguinte texto:

```
<datasource jta="true" jndi-name="java:/jdbc/AgendaTelefonica" pool-name="AgendaTelefonica" enabled="true" use-java-context="true" use-ccm="true">
    <connection-url>jdbc:mysql://localhost:3306/agenda_telefonica?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;useTimezone=true&amp;serverTimezone=America/Sao_Paulo</connection-url>
    <driver>com.mysql</driver>
    <security>
        <user-name>root</user-name>
        <password>root</password>
    </security>
</datasource>
```
Substitua o nome de usuário e a senha para a versão utilizada no seu banco de dados.

Dentro da tag drivers, adicione o seguinte texto:
```
<driver name="com.mysql" module="com.mysql">
    <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
    <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
```

Após esses passos, o backend do projeto deve ser executado no servidor WildFly.
