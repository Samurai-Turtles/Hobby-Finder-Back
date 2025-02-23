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


