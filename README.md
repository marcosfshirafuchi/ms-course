# <a href="https://imgbb.com/"><img src="https://i.ibb.co/41NVDPM/image-2024-12-01-T01-17-16-558-Z.png" alt="image-2024-12-01-T01-17-16-558-Z" border="0"></a> Microsserviços Java com Spring Boot e Spring Cloud



Versões: Java 11 e Spring Boot 2.3.4! Aprenda Feign, Ribbon, Hystrix, OAuth, JWT, Eureka, API Gateway Zuul, e muito mais

#### Curso desenvolvido pelo:
- Nelio Alves
- O curso de Microsserviços Java com Spring Boot e Spring Cloud foi feito na versão: Java 11 e Spring boot 2.3.4.RELEASE.
- Obs: Adaptei para a versão Java 21 e Spring boot 3.5.6.


## Principais Tecnologias

- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original-wordmark.svg" title = "Java" /> <b> Java 21 :</b> Utilizaremos a versão LTS mais recente do Java para tirar vantagem das últimas inovações que essa linguagem robusta e amplamente utilizada oferece;
- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/intellij/intellij-original.svg" title = "IntelliJIDEA" /> <b> IntelliJIDEA :</b> Utilizei o IntelliJIDEA como a IDEA para fazer os programas em Java;
- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg" /><b> String Boot :</b> Utilizei o Spring boot na versão 3.5.6;
- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original-wordmark.svg" /><b> Postman :</b> Utilizei o Postman para testar as APIs dos projetos desenvolvidos no curso;
- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original-wordmark.svg" /><b> Docker :</b> Utilizei o Docker para rodar os projetos desenvolvidos no curso;
- <img width="70px" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postgresql/postgresql-original-wordmark.svg" /><b> Postgres :</b>  Utilizei o pgAdmin4 na versão
9.9;

## O que você aprenderá
<center> 
  <a href="https://ibb.co/dJkqps3d"><img src="https://i.ibb.co/XkFg5fdh/2-O-sistema-que-vamos-desenvolver-Parte-01.png" alt="2-O-sistema-que-vamos-desenvolver-Parte-01" border="0"></a><br><br>
  <a href="https://ibb.co/p60BNrnq"><img src="https://i.ibb.co/Gfx4G3Fm/imagem-1.png" alt="imagem-1" border="0"></a><br>
  <a href="https://ibb.co/rKypZdvZ"><img src="https://i.ibb.co/RGH6Cj7C/2-O-sistema-que-vamos-desenvolver-Parte-03.png" alt="2-O-sistema-que-vamos-desenvolver-Parte-03" border="0"></a><br><br>
  <a href="https://ibb.co/7JWQ78hc"><img src="https://i.ibb.co/nscBhZp2/2-O-sistema-que-vamos-desenvolver-Parte-04.jpg" alt="2-O-sistema-que-vamos-desenvolver-Parte-04" border="0"></a><br><br>
  <a href="https://ibb.co/mChJMTDN"><img src="https://i.ibb.co/gMtyB9j4/2-O-sistema-que-vamos-desenvolver-Parte-05.png" alt="2-O-sistema-que-vamos-desenvolver-Parte-05" border="0"></a><br><br>
  <a href="https://ibb.co/Xr2WRJrY"><img src="https://i.ibb.co/DHV12KHr/2-O-sistema-que-vamos-desenvolver-Parte-06.png" alt="2-O-sistema-que-vamos-desenvolver-Parte-06" border="0"></a><br><br>
  <a href="https://ibb.co/Y4MKTPLg"><img src="https://i.ibb.co/GfL13n5m/2-O-sistema-que-vamos-desenvolver-Parte-07.jpg" alt="2-O-sistema-que-vamos-desenvolver-Parte-07" border="0"></a><br><br>
  <a href="https://ibb.co/YTDYSJ4V"><img src="https://i.ibb.co/dJb1vXsH/2-O-sistema-que-vamos-desenvolver-Parte-08.jpg" alt="2-O-sistema-que-vamos-desenvolver-Parte-08" border="0"></a>
  </center>
  <br><br>

- Uma introdução a algumas das principais ferramentas do Spring Cloud para estruturação de um sistema em microsserviços
- Chamadas de API entre microsserviços por meio de clientes Feign
- Criar microsserviços escaláveis, com resolução de nomes e balanceamento de carga de forma transparente, usando servidor Eureka
- Roteamento transparente de microsserviços com Zuul API Gateway
- Configuração centralizada por meio de um servidor de configuração
- Autenticação e autorização compartilhada por meio do API Gateway, usando Oauth e JWT

## Conteúdo do curso
- Introdução
- Fase 1: Comunicação simples, Feign, Ribbon
- Fase 2: Eureka, Hystriz, Zuul
- Fase 3: Configuração centralizada
- Fase 4: Autenticação e autorização
- Criando e testando containers Docker
- Seção Bônus



## Link do curso:

https://www.udemy.com/course/microsservicos-java-spring-cloud/?couponCode=KEEPLEARNINGBR

## Link do repositório dos arquivos de configuração
<b>MS COURSE CONFIGS:</b><br><br>
https://github.com/marcosfshirafuchi/ms-course-configs

## Criando e testando containers Docker

<center><a href="https://ibb.co/FLQBSByC"><img src="https://i.ibb.co/Q3W626Tt/containers-docker-page-0001.jpg" alt="containers-docker-page-0001" border="0"></a></center>

## Criar rede docker para sistema hr
```
docker network create hr-net
```

## Testando perfil dev com Postgresql no Docker
```
docker pull postgres:12-alpine

docker run -p 5432:5432 --name hr-worker-pg12 --network hr-net -e POSTGRES_PASSWORD=1234567 -e POSTGRES_DB=db_hr_worker postgres:12-alpine

docker run -p 5432:5432 --name hr-user-pg12 --network hr-net -e POSTGRES_PASSWORD=1234567 -e POSTGRES_DB=db_hr_user postgres:12-alpine
```

## hr-config-server
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8888
ADD ./target/hr-config-server-0.0.1-SNAPSHOT.jar hr-config-server.jar
ENTRYPOINT ["java","-jar","/hr-config-server.jar"]
``` 
```
mvnw clean package

docker build -t hr-config-server:v1 .

docker run -p 8888:8888 --name hr-config-server --network hr-net -e GITHUB_USER=acenelio -e GITHUB_PASS= hr-config-server:v1
```

## hr-eureka-server
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8761
ADD ./target/hr-eureka-server-0.0.1-SNAPSHOT.jar hr-eureka-server.jar
ENTRYPOINT ["java","-jar","/hr-eureka-server.jar"]
``` 
```
mvnw clean package

docker build -t hr-eureka-server:v1 .

docker run -p 8761:8761 --name hr-eureka-server --network hr-net hr-eureka-server:v1
```

## hr-worker
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ADD ./target/hr-worker-0.0.1-SNAPSHOT.jar hr-worker.jar
ENTRYPOINT ["java","-jar","/hr-worker.jar"]
``` 
```
mvnw clean package -DskipTests

docker build -t hr-worker:v1 .

docker run -P --network hr-net hr-worker:v1
```

## hr-user
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ADD ./target/hr-user-0.0.1-SNAPSHOT.jar hr-user.jar
ENTRYPOINT ["java","-jar","/hr-user.jar"]
``` 
```
mvnw clean package -DskipTests

docker build -t hr-user:v1 .

docker run -P --network hr-net hr-user:v1
```

## hr-payroll
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ADD ./target/hr-payroll-0.0.1-SNAPSHOT.jar hr-payroll.jar
ENTRYPOINT ["java","-jar","/hr-payroll.jar"]
``` 
```
mvnw clean package -DskipTests

docker build -t hr-payroll:v1 .

docker run -P --network hr-net hr-payroll:v1
```

## hr-oauth
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ADD ./target/hr-oauth-0.0.1-SNAPSHOT.jar hr-oauth.jar
ENTRYPOINT ["java","-jar","/hr-oauth.jar"]
``` 
```
mvnw clean package -DskipTests

docker build -t hr-oauth:v1 .

docker run -P --network hr-net hr-oauth:v1
```

## hr-api-gateway-zuul
```
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8765
ADD ./target/hr-api-gateway-zuul-0.0.1-SNAPSHOT.jar hr-api-gateway-zuul.jar
ENTRYPOINT ["java","-jar","/hr-api-gateway-zuul.jar"]
``` 
```
mvnw clean package -DskipTests

docker build -t hr-api-gateway-zuul:v1 .

docker run -p 8765:8765 --name hr-api-gateway-zuul --network hr-net hr-api-gateway-zuul:v1
```

## Alguns comandos Docker
Criar uma rede Docker
```
docker network create <nome-da-rede>
```
Baixar imagem do Dockerhub
```
docker pull <nome-da-imagem:tag>
```
Ver imagens
```
docker images
```
Criar/rodar um container de uma imagem
```
docker run -p <porta-externa>:<porta-interna> --name <nome-do-container> --network <nome-da-rede> <nome-da-imagem:tag> 
```
Listar containers
```
docker ps

docker ps -a
```
Acompanhar logs do container em execução
```
docker logs -f <container-id>
```






