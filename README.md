# Sistema de Aluguel de Livros com Kafka e JWT

Este projeto implementa um sistema de aluguel de livros utilizando mensageria com Apache Kafka e autenticação via JWT.

### Descrição Geral
O sistema permite que usuários aluguem e devolvam livros, enviando notificações por mensageria Kafka
para registrar as ações. A autenticação e autorização são feitas via JWT, garantindo que apenas usuários
autenticados possam realizar ações no sistema.

## Pré-requisitos
Antes de rodar o projeto, certifique-se de ter o seguinte instalado:
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

### Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security 6.3.3**
- **Apache Kafka**
- **Lombok**
- **H2**
- **Swagger**
- **Junit**

### Instalação e Configuração

   1. Clone o repositório:
      ```bash
      git clone https://github.com/rusouza/catalogoLivro.git

   2. Configure o arquivo `application.properties`:

      ```
      server.port=8080
   
      ## DATASOURCE
      spring.datasource.url=jdbc:h2:mem:testdb;AUTO_RECONNECT=TRUE
      spring.datasource.username=seu-usuario
      spring.datasource.password=sua-senha
      spring.datasource.driverClassName=org.h2.Driver
   
      ## JPA
      spring.jpa.defer-datasource-initialization=true
      spring.jpa.show-sql=true
      spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
      spring.jpa.hibernate.ddl-auto=create
   
      ## H2
      spring.h2.console.enabled=true
      spring.h2.console.path=/h2-console
   
      ## KAFKA
      kafka.bootstrap-servers=localhost:9092
   
      ## JWT
      ## armazena o hash que vai ser usado para gerar o código para autenticar a sessão
      jwt.secret-key=sua-chavesecreta
      ## armazena o tempo (em milissegundos) que a sessão irá continuar ativa
      jwt.expiration-time=seu-tempo-em-milissegundos 
      ```
   
   3. Baixar e Rodar os consumidores Kafka
        
      1. **Baixar a versão Binary do Kafka:**
   
         - Vai no site do [Apache Kafka](https://kafka.apache.org/downloads) e baixa a versão mais recente.
         - Depois de baixar, extrai o arquivo.
      
      2. **Subir o Zookeeper:**
   
         - Antes de subir o Kafka, precisa rodar o Zookeeper, que ele usa pra gerenciar os brokers.
         ```bash
         .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

      3. **Subir o Kafka:**
   
         - Com o Zookeeper rodando, agora sobe o Kafka:
         ```bash
         .\bin\windows\kafka-server-start.bat .\config\server.properties

      4. **Verificar o tópico criado**
         
         >O Apache Kafka está configurado para enviar notificações sempre que um livro é alugado ou devolvido.
         Essas mensagens podem ser visualizadas usando o consumidor Kafka.

         - Aluguel:
            ```bash
           .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic livro-alugado --from-beginning
            ```
         - Devolução:
            ```bash
            .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic livro-devolvido --from-beginning
            ```
        > **OBS:** Os comandos fornecidos acima são pra rodar no terminal do Windows (CMD ou PowerShell).

### Endpoints Disponíveis:

   - `POST api/user/cadastro`: Cadastar os usuários.
   - `POST api/user/login`: Autentica o usuário e retorna o JWT.
   - `GET api/catalogo/livros`: Lista todos os livros disponíveis.
   - `GET api/catalogo/findByTitulo/{nome}`: Buscar livro pelo nome.
   - `PUT api/catalogo/alugar/{livroId}`: Aluga um livro.
   - `PUT api/catalogo/devolver/{livroId}`: Devolve um livro.
   - `DEL api/catalogo/deletar/livros/{id}`: Deletar os livros por ID.

    Acesse a documentação completa da API no Swagger: `http://localhost:8080/swagger-ui.html`

### Acesso ao Banco

   Acesse o banco e dados H2 na URL: `http://localhost:8080/h2-console/`