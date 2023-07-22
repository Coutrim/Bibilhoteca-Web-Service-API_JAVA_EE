# Back-end do projeto bibilhoteca-rest
## Esse é uma API desenvolvida com a arquitetura REST. No back-end: Java EE 8 com EJB3, RESTEasy (JAX-RS), Hibernate/JPA e o gerenciador de dependências Maven. O banco de dados é o MySQL 8.

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


## Validações das requisições POST e PUT
Para executar os métodos HTTP da aplicação com alguma ferramenta de testes de API, como o [Postman](https://www.postman.com/) por exemplo, é necessário habilitar todas opções sugeridas no cabeçalho (header) da requisição e com o atributo chave-valor "Content-Type: application/json".
No método POST, o corpo da requisição deve ser enviado dessa forma:

    {   
     "acertos":"1",
     "erros": "1",
     "ganhos":"2098",
     "data":"2022-04-01 14:30:00"
    
}

O campo **acertos** não pode ser nulo e deve ter entre 2 e 200 caracteres.<br>
O campo **erros** pode ser nulo.<br>
O campo **ganhos** pode ser nulo. Caso for preenchido, deve ser válido, contendo um "@" na string.<br>
A **data** pode ser nula. Caso for preenchida, deve ser válida, no formato "YYYY-MM-DD".<br>


O corpo da requisição no método PUT é similar ao POST. A diferença é que o ID do registro a ser editado deve ser passado como parâmetro na URL.

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
