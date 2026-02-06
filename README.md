# Vox
![Vox](vox/src/main/resources/static/imagens/voxtelaprincipal.png)
## Descrição do Projeto
O **Vox** é uma aplicação desenvolvida em Java com o framework Spring Boot. O objetivo do projeto é gerenciar processos administrativos e acadêmicos, incluindo reuniões, processos, usuários e assuntos relacionados a colegiados e coordenadores.

## Tecnologias Utilizadas
<div>
  <img src="https://img.shields.io/badge/Java%2021-007396?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
</div>

## Estrutura do Projeto
O projeto segue uma arquitetura MVC (Model-View-Controller) e está organizado da seguinte forma:

- **src/main/java**: Contém o código-fonte principal da aplicação.
  - `controller`: Controladores responsáveis por gerenciar as requisições HTTP.
  - `entity`: Entidades que representam os modelos de dados.
  - `repository`: Interfaces para acesso ao banco de dados.
  - `service`: Classes de serviço que implementam a lógica de negócios.
  - `validation`: Classes para validação de dados.
- **src/main/resources**: Contém os recursos estáticos e templates da aplicação.
  - `templates`: Arquivos HTML organizados em subpastas para diferentes funcionalidades.
  - `static`: Arquivos estáticos como CSS, JavaScript e imagens.
  - `application.properties`: Configurações da aplicação.

## Funcionalidades
- Gerenciamento de usuários (admin, coordenadores, professores).
- Controle de reuniões e processos administrativos.
- Interface web com templates organizados por funcionalidade.
- Integração com banco de dados para persistência de dados.

## Como Executar o Projeto
1. Certifique-se de ter o Java 21 instalado.
2. Clone este repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
3. Navegue até o diretório do projeto:
   ```bash
   cd Vox
   ```
4. Execute o comando para iniciar a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou, no Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```
5. Acesse a aplicação no navegador em: `http://localhost:8080`

## Estrutura de Templates
Os templates HTML estão organizados da seguinte forma:
- **auth**: Páginas de autenticação (ex.: login).
- **admin**: Páginas administrativas para gerenciar assuntos, colegiados e usuários.
- **processos**: Páginas para gerenciar processos administrativos.
- **reunioes**: Páginas para gerenciar reuniões de coordenadores e professores.
- **fragments**: Fragmentos reutilizáveis como cabeçalhos e barras de navegação.

## Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e enviar pull requests.

## Licença
Este projeto está licenciado sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

**Contribuição**
<table>
  <tr>
   <td align="center">
      <a href="https://github.com/jessyekessia" title="gitHub">
        <img src="https://avatars.githubusercontent.com/u/128109017?v=4" width="100px;" alt="Foto de Jessye"/><br>
        <sub>
          <b>Jessye Késsia Pereira</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Maira-larissa" title="gitHub">
        <img src="https://avatars.githubusercontent.com/u/131016411?v=4" width="100px;" alt="Foto de Maira"/><br>
        <sub>
          <b>Maira Larissa</b>
        </sub>
      </a>
    </td>
     <td align="center">
      <a href="https://github.com/iamjonn" title="gitHub">
        <img src="https://avatars.githubusercontent.com/u/110827553?v=4" width="100px;" alt="Foto de Jon"/><br>
        <sub>
          <b>Jonata Nascimento</b>
        </sub>
      </a>
    </td>
      </a>
    </td>
  </tr>
</table>


