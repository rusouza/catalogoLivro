# Sistema de Aluguel de Livros com Kafka e JWT

Este projeto implementa um sistema de aluguel de livros utilizando mensageria com Apache Kafka e autenticação via JWT.

### Descrição Geral
O sistema permite que usuários aluguem e devolvam livros, enviando notificações por mensageria Kafka
para registrar as ações. A autenticação e autorização são feitas via JWT, garantindo que apenas usuários
autenticados possam realizar ações no sistema.

## Pré-requisitos
Antes de rodar o projeto, certifique-se de ter o seguinte instalado:
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [MailHog](https://github.com/mailhog/MailHog/releases)
- [Apache Kafka](https://kafka.apache.org/downloads)

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
      spring.datasource.username=usuario_do_banco
      spring.datasource.password=senha_do_banco
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
      
      ## Configurações de e-mail para o MailHog
      spring.mail.host=localhost
      spring.mail.port=1025
      spring.mail.username=
      spring.mail.password=
      spring.mail.properties.mail.smtp.auth=false
      spring.mail.properties.mail.smtp.starttls.enable=false
      spring.mail.properties.mail.smtp.connectiontimeout=5000
      spring.mail.properties.mail.smtp.timeout=5000
      spring.mail.properties.mail.smtp.writetimeout=5000
      ```
   
   3. Rodar os consumidores Kafka
        
      1. ### **Instalação**
   
         - Após baixar, extrai o arquivo (Verificar se baixou a versão Binary do Kafka).
      
      2. ### **Subir o Zookeeper:**
   
         - Antes de subir o Kafka, precisa rodar o Zookeeper, que ele usa pra gerenciar os brokers.
         - Com o terminal aberto acesse a pasta que você extraiu o Kafka e execute o comando abaixo.
         ```bash
         .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

      3. ### **Subir o Kafka:**
   
         - Com o Zookeeper rodando, agora sobe o Kafka:
         - Abre um novo terminal e execute o comando abaixo.
         ```bash
         .\bin\windows\kafka-server-start.bat .\config\server.properties

      4. ### **Verificar o tópico criado**
         
         >O Apache Kafka está configurado para enviar notificações sempre que um livro é alugado ou devolvido.
         Essas mensagens podem ser visualizadas usando o consumidor Kafka.

         #### Tópico de Aluguel:
            ```bash
           .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic livro-alugado --from-beginning
            ```
         #### Tópico de Devolução:
            ```bash
            .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic livro-devolvido --from-beginning
            ```
         > **OBS:** Os comandos fornecidos acima são pra rodar no terminal do Windows (CMD ou PowerShell).
   
   4. Rodar o MailHog
        - Após baixar o arquivo exe, execute e o mesmo estará disponivel: `http://localhost:8025/`

### Start da Aplicação

Após terminar as configurações starte a aplicação numa ide de sua preferência.

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