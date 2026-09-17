# SugestFIlmes

Sistema web de recomendação de filmes desenvolvido em Java MVC com JSP, MySQL e Tomcat, utilizando Maven para build e Docker para subir o ambiente.

## 1. Visão geral do projeto

O projeto tem como objetivo permitir que um usuário:

- faça login no sistema;
- visualize filmes e categorias;
- escolha categorias de interesse;
- receba recomendações com base nas categorias selecionadas;
- avalie filmes;
- visualize e gerencie suas avaliações;
- gerencie usuários, perfis, filmes e categorias conforme permissão do sistema.

O projeto simula um sistema de recomendação de filmes inspirado em plataformas de catálogo e seleção de preferências.

## 2. Tecnologias utilizadas

- Java 17
- Maven
- Jakarta Servlet / JSP
- Tomcat 10
- MySQL 8
- Docker e Docker Compose
- HTML, CSS e JavaScript básico

## 3. Estrutura do projeto

```text
SugestFIlmes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/mvc/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dao/
│   │   │       ├── filter/
│   │   │       ├── model/
│   │   │       └── service/
│   │   └── webapp/
│   │       ├── css/
│   │       └── WEB-INF/jsp/
│   └── test/
├── docker-compose.yml
├── init.sql
├── pom.xml
├── deploy/
├── .gitignore
└── README.md
```

## 4. Como o sistema funciona

### 4.1 Fluxo principal

1. O usuário acessa a aplicação pelo navegador.
2. O Tomcat recebe a requisição e direciona para o servlet correto.
3. O servlet atua como controlador, recebendo dados da requisição.
4. O controller chama a camada de serviço.
5. O serviço executa regras de negócio.
6. O DAO acessa o banco MySQL.
7. Os resultados voltam para a página JSP.
8. A JSP renderiza a interface HTML para o usuário.

### 4.2 Exemplo de fluxo real

- Usuário acessa `/login`
- `LoginServlet` recebe a requisição
- `UsuarioService.autenticar(...)` valida login e senha
- `UsuarioDAO.buscarPorLoginESenha(...)` consulta o banco
- se autenticado, a sessão é criada com o usuário logado
- o sistema redireciona para `/home`

## 5. Conceitos fundamentais

### 5.1 MVC

MVC significa Model, View e Controller.

- Model: representa os dados e regras do domínio. No projeto, são as classes em `model`, como `Usuario`, `Filme`, `Categoria`, `Avaliacao`.
- View: é a parte visual. No projeto, são as páginas JSP em `src/main/webapp/WEB-INF/jsp`.
- Controller: recebe as requisições do usuário e decide o que fazer. No projeto, ficam em `controller` e são classes como `HomeServlet`, `LoginServlet`, `FilmeServlet`, `CategoriaServlet`.

Exemplo prático do projeto:

- O usuário clica em "Filmes"
- o navegador chama `/filmes`
- `FilmeServlet` recebe a requisição
- ele chama `FilmeService`
- o service consulta o DAO
- o DAO lê no banco
- o resultado vai para a JSP de filmes

### 5.2 POO (Programação Orientada a Objetos)

A POO é a base da linguagem Java. Ela organiza o código em objetos que possuem:

- atributos (dados)
- métodos (ações)

No projeto, cada entidade é uma classe:

- `Usuario`
- `Filme`
- `Categoria`
- `Avaliacao`
- `Perfil`

Cada objeto encapsula sua informação e pode ter métodos específicos para manipulação e validação.

Exemplo:

```java
Usuario usuario = new Usuario();
usuario.setNome("Maria");
usuario.setLogin("maria");
usuario.setSenha("123456");
```

Isso mostra que o objeto representa um usuário do sistema e guarda os dados de forma organizada.

### 5.3 DAO

DAO significa Data Access Object.

É a classe responsável por acessar o banco de dados. No projeto, os DAOs ficam em `dao` e fazem:

- consultas SQL
- mapeamento do `ResultSet` para objetos Java
- inserção, edição e exclusão de registros

Exemplo:

- `UsuarioDAO` acessa a tabela `usuarios`
- `FilmeDAO` acessa a tabela `filmes`
- `CategoriaDAO` acessa a tabela `categorias`

### 5.4 Service

A camada de serviço contém as regras de negócio.

Exemplos:

- autenticar usuário
- validar senha
- validar login único
- verificar se um perfil existe
- listar filmes em destaque
- calcular recomendações por categoria

No projeto, isso aparece em classes como:

- `UsuarioService`
- `FilmeService`
- `CategoriaService`
- `RecomendacaoService`

### 5.5 Maven

Maven é uma ferramenta de automação de build e gerenciamento de dependências.

Ele faz, por exemplo:

- baixar bibliotecas (
  `jakarta.servlet`, `mysql-connector-j`, `jstl`, etc.);
- compilar o código Java;
- empacotar a aplicação em `.war`;
- rodar testes e build.

O projeto usa o arquivo `pom.xml` como configuração central.

Comandos principais:

```bash
mvn clean
mvn compile
mvn package
mvn test
```

### 5.6 Tomcat

Tomcat é o servidor de aplicação usado para executar a aplicação web Java.

Ele recebe as requisições HTTP, executa os servlets e entrega a resposta em HTML.

No projeto, o Tomcat é subido pelo Docker com o arquivo:

```yaml
services:
  tomcat:
    image: tomcat:10.1-jdk21
    ports:
      - "8080:8080"
```

## 6. Regras de negócio presentes no projeto

### 6.1 Login e autenticação

- login e senha são obrigatórios;
- o sistema verifica se o usuário existe no banco;
- só permite acesso se login e senha coincidirem;
- o acesso às rotas protegidas é controlado por `AuthFilter`.

### 6.2 Usuários

- nome, login e senha são obrigatórios;
- login deve ser único;
- senha mínima é de 6 caracteres;
- perfil do usuário deve existir;
- existem perfis como Administrador, Professor e Aluno.

### 6.3 Filmes

- título, descrição, gênero, ano e diretor são obrigatórios;
- um filme pode estar associado a várias categorias;
- o sistema permite listar, editar e excluir filmes.

### 6.4 Categorias

- cada categoria tem um nome único;
- a categoria é usada como critério para recomendação.

### 6.5 Recomendações

- o usuário escolhe categorias;
- o sistema busca filmes que contenham essas categorias;
- a recomendação é feita com base nas categorias selecionadas.

### 6.6 Avaliações

- cada usuário pode avaliar um filme uma única vez;
- a nota deve estar entre 1 e 5;
- o sistema mostra a média das avaliações;
- uma avaliação possui comentário e data.

## 7. Casos de uso do sistema

### Caso de uso 1: login

- Usuário entra com login e senha.
- Sistema valida dados.
- Se correto, entra na home.
- Se errado, mostra erro.

### Caso de uso 2: escolher categorias

- Usuário seleciona uma ou mais categorias.
- Sistema busca filmes compatíveis.
- O sistema mostra os resultados.

### Caso de uso 3: avaliar filme

- Usuário acessa o filme.
- Insere nota e comentário.
- Sistema salva avaliação no banco.

### Caso de uso 4: gerenciar usuários

- Administrador ou usuário autorizado cadastra, altera ou exclui usuários.
- O sistema valida dados antes de persistir no banco.

## 8. Banco de dados

O banco é modelado pelo arquivo `init.sql`.

Principais tabelas:

- `perfis`
- `usuarios`
- `filmes`
- `categorias`
- `filmes_categorias`
- `avaliacoes`

### Dados iniciais

Usuários padrão:

```text
admin / 123456
joao / 123456
maria / 123456
```

Esses usuários são criados automaticamente no init.sql.

## 9. Como rodar o projeto

### Requisitos

- Java 17+
- Maven
- Docker Desktop instalado
- Git

### 1. Clonar o projeto

```bash
git clone https://github.com/AlfredoFariaF/SugestFIlmes.git
cd SugestFIlmes
```

### 2. Subir os containers

```bash
docker compose up -d
```

### 3. Compilar o projeto

```bash
mvn clean package
```

### 4. Acessar no navegador

```text
http://localhost:8080/mvc/login
```

## 10. Testes automatizados

Este projeto inclui testes de comportamento em JUnit para validar regras de negócio importantes do sistema.

Os testes cobrem:

- autenticação com usuário válido;
- autenticação com senha inválida;
- senha curta rejeitada;
- recomendação por categorias;
- avaliação com nota inválida.

Arquivo de teste:

```text
src/test/java/br/com/mvc/service/RegraDeNegocioTest.java
```

Como executar:

```bash
cd SugestFIlmes
mvn test -q -DDB_HOST=localhost
```

Importante: o parâmetro `-DDB_HOST=localhost` é usado para que o Maven conecte ao MySQL local do Docker durante a execução dos testes.

## 11. Fundamentos conceituais do projeto

Os principais conceitos aplicados ao sistema são:

- MVC: a aplicação é dividida em Model, View e Controller, separando dados, interface e fluxo de controle.
- DAO: a camada de acesso a dados é responsável por consultas e manipulação das tabelas do banco.
- Service: a camada de regras de negócio valida operações como autenticação, cadastro e avaliação.
- Maven: gerencia dependências e empacotamento da aplicação Java.
- Tomcat: servidor web que executa a aplicação Java na web.
- JSP: página dinâmica responsável por renderizar a interface visual do sistema.
- Filtragem de autenticação: o filtro impede acesso a páginas protegidas sem login válido.
- POO: os objetos do domínio representam entidades do sistema, como usuários, filmes, categorias e avaliações.
- JUnit: permite validar o comportamento real das regras do sistema por meio de testes automatizados.

## 12. Conclusão

Este projeto demonstra uma aplicação web Java com arquitetura MVC, persistência em MySQL, autenticação, recomendação por categorias e avaliação de filmes. Ele é um bom exemplo de projeto acadêmico de desenvolvimento web com foco em organização, separação de responsabilidades e regras de negócio.

## 13. Observações finais

- O projeto está funcional.
- A base da arquitetura está correta.
- O entendimento dos conceitos de MVC e POO é essencial para responder perguntas do professor.
- O arquivo `README.md` pode ser usado como material de suporte para explicação oral e entrega do trabalho.

