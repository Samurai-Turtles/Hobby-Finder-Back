# Guia de Contribuição para Projeto Spring Boot

## 1. Estrutura do Projeto

- **Código-fonte:**
  - Local: `src/main/java/com/hobbyFinder`
  - Organização em pacotes seguindo a arquitetura em camadas:
    - **controller:** Classes que fornecem as rotas (ex.: `UserController`);
    - **services:** Lógica das rotas relacionadas a um `Controller`(ex.: `UserService`);
    - **repositories:** Entidades do tipo `repository`(ex.: `UserRepository`);
    - **models:** Entidades e DTOs (ex.: `User`, `UserResponseDTO`);
    - **config:** Configurações gerais (ex.: segurança, datasources);
    - **exception:** Tratamento de erros e exceções.
  - As entidades devem estar agrupadas por domínio, o que significa que as estruturas dos subdiretórios devem estar organizadas de forma similar ao seguinte exemplo:
  ```
    ./hobbyFinder
      ├── controller/
      │   ├── user/
      │   │   ├── UserAccessController.java
      │   ├── event/
      │   ... ... ...
      ├── services/
      │   ├── servicesInterface/
      │   │   ├── user/
      │   │   │   └── UserAccessInterface.java
      │   ├── servicesImplements/
      │   │   ├── user/
      │   │   │   └── UserAccessService.java
      │   ... ... ...
      ├── models/
      │   ├── dto/
      │   │   └── user/
      │   └── entities/
      ├── repositories/
      └── ... ... ...
    ```
  - Entidades `service` devem ter pouca responsabilidade, programe-as para uma interface e lembre dos princípios GRASP.

- **Recursos:**  
  - Local: `src/main/resources`
  - Arquivos de configuração: `application.properties`

- **Testes:**  
  - Local: `src/test/java/com/hobbyFinder/hobby/controllerTest`
  - Mantenha consistência na criação de testes para cada conjunto relacionado de rotas.

## 2. Convenções de Código

### Nomeação
- **Classes:** Utilizar _PascalCase_  
  - Ex.: `UserCRUDController`, `UserCRUDService`
  - Para classes do tipo DTO, incluir o método http na nomeclatura (`<Entidade><Método>DTO.java`)
- **Métodos e Variáveis:** Utilizar _camelCase_  
  - Ex.: `createUser()`, `findOrderById()`

## 3. Fluxo de Trabalho com Git

### Branches
- **main:** Contém o código estável, pronto para produção.
- **Dev:** Branch para integração e testes de novas funcionalidades.
- **feature/nome-da-feature:** Para desenvolvimento de novas funcionalidades.
- **refactor/nome-do-refactor:** Para refatoração de código, deixe explícita qual a região do código a ser refatorada.
- **fix/nome-do-fix:** Para correções de bugs ou de problemas graves, deixe explícito qual o problema a ser resolvido.
- Nenhuma branch deve ser criada a partir da main, exceto a branch `Dev`.

### Pull Requests e Code Review
- Sempre abrir um Pull Request (PR) direcionado à branch `Dev`.
- PRs devem conter uma descrição clara, referenciando as issues correspondentes.
- Exigir, pelo menos, dois revisores para aprovar o PR antes do merge. Priorize aqueles que estão designados como 'watchers' da sua task, no Taiga. Caso não haja, solicite revisão de um dos gerentes.

## 5. Commits e Documentação

### Mensagens de Commit
- Utilize mensagens de commit claras e seguindo o padrão:
  - `feat`: adiciona uma nova funcionalidade ao sistema;
  - `fix`: correção de algum bug;
  - `refactor`: refatoração de um método completo ou trecho de código;
  - `test`: adição de um novo teste;
  - `docs`: adição/edição de uma documentação;
  - `style`: alteração do estilo de código, sem alteração funcional.
- Mensagens devem explicar o "porquê" da alteração.

## 6. Critério de aceitação

Para melhor entendimento desta lista de critérios para aceitação de uma nova feature, será dividido em camadas.

<center>
  <table>
    <tr>
      <th>Nível de prioridade</th>
      <th>Como será o início de cada frase</th>
    </tr>
    <tr>
      <th>4</th>
      <th>Seja obrigatório ...</th>
    </tr>
    <tr>
      <th>3</th>
      <th>Seja essencial ...</th>
    </tr>
    <tr>
      <th>2</th>
      <th>Seja essencial ...</th>
    </tr>
      <tr>
      <th>1</th>
      <th>Seja indiferente ...</th>
    </tr>
  </table>
</center>

### 1. Geral

* Seja essencial a preferência por injeção via construtor;
* Seja obrigatório o uso de constantes, não será tolerado strings jogadas ao vento.

### 2. Data Transfer Objects

* Seja obrigatório o uso de notação do que geração de código utilitário para verificação de propriedades;
* Seja essencial a correspondência com o guia técnico;

### 3. Controller

* Seja essencial a correspondência com o guia técnico;
* Seja obrigatório a não geração de responsabilidade adicional ao controller;
  * Seja desejável o acoplamento em Services;
  * Possibilidade na geração de um Helper caso seja lógica de aplicação.
* Seja obrigatório a não proliferação de métodos;
  * Apenas existem os métodos definidos no guia técnico.
* Seja obrigatório manter o padrão dos outros controllers, tanto no uso de construção como de anotação.

### 4. Services

* Seja obrigatório a não geração de responsabilidade adicional ao service;
  * Seja obrigatório que os modelos conheçam seus próprios comportamentos; 
* Seja obrigatório do revisor sobre a qualidade desta camada:
  * Anotações a respeito devem ser associadas neste [documento](https://docs.google.com/document/d/13rtzf8i5HtF2g-9bMx7oQiAMRv1y2eDXjNTD29TFDZA/edit?tab=t.0);
* Seja essencial a não proliferação de dependências:
  * É possível que dependências além das que envolvem a entidade sejam chamadas, mas não é o preferível.

### 5. Repositórios

* Seja obrigatório seguir a JPA;

### 6. Modelos

* Seja obrigatório a utilização de notações para regras no banco;
* Seja essencial a não utilização de notações para identificação de atributos no banco;
* Seja obrigatório o uso do modelo para regras de negócio correspondentes a esta entidade;
* Seja obrigatório o uso de testes unitários para comportamentos de modelos;

### 7. Testes

* Seja obrigatório que os testes de integração sigam o comportamento esperado dito no guia técnico;
* Seja obrigatório que os testes de integração compreendam o comportamento esperado dito no guia técnico;
* Seja obrigatória a seguinte estrutura de pacote para testagem integrativa de cada feature:
  * Criação de classe estática para constantes;
  * Criação de classe auxiliar para gerar um estado inicial válido para testes;
  * Criação de classe de teste de integração por método do controller.
* Seja obrigatória a criação de classes de testes unitários para cada comportamento de um modelo, tais testes precisam estar juntos em um mesmo pacote.
  * Seja obrigatória a junção dos testes de integração com os unitários em um pacote maior que contenha a entidade. 