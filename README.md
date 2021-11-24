# aws-spring-microservices-study
Projeto realizado com base no curso [Criando microsserviços em Java com AWS ECS e Fargate](https://www.udemy.com/course/aws-ecs-fargate-java/), onde foi explorado a criação de microserviços utilizando Spring Boot e os principais recursos da AWS.

### Tecnologias usadas
* Spring Boot
* AWS ECS (Fargate)
* AWS RDS (MySQL/MariaDB)
* AWS DynamoDB
* AWS SQS
* AWS SNS
* AWS S3
* AWS VPC
* AWS CloudWatch
* AWS CloudFormation
* Docker
* jib (GoogleContainerTools)

### Arquitetura
![Arquitetura](/home/pedroulhoa/development/backend/aws-spring-microservices-study/architecture.png)

### Execução

Pré-requisitos:
- IDE (IntelliJ, Eclipse, others...)
- Java 17
- MySQL/MariaDB
- Maven
- Node/NPM
- AWS CLI
- AWS CDK
- LocalStack
- Docker

#### Local
Com o LocalStack é possível emular os recursos da AWS localmente, e testar nossa aplicação no ambiente local.

Subindo o LoacalStack com os recursos desejados:

`docker run --rm -p 4566:4566 -p 4571:4571 localstack/localstack -e "SERVICES=sns, sqs, dynamodb, s3"
`

Após a subida do LocalStack, confira as configurações de banco de dados no arquivo `application.yml` e execute o projeto
`product-api`, em seguida o projeto `log-events-consumer-api`, as classes de configurações dentro do pacote `local`
vão criar os recursos conforme desejado.

#### AWS
O projeto `aws_cdk_infrastructure`, contem toda infraestrutura necessária para todar o nosso projeto conforme desejado, 
você pode executar o mesmo, configurando sua conta da AWS no AWS CLI, e executando as stacks do projeto `aws_cdk_infrastructure`
através do AWS CDK.