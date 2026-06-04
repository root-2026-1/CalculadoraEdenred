---
name: architecture
description: Arquitetura do projeto — backend Java/Spring Boot em camadas e frontend React com roteamento e contexto de autenticação
metadata:
  type: project
---

# Backend

O backend segue arquitetura em camadas com Spring Boot 3.5 e Java 17. O fluxo de dependências é unidirecional: Controller → Service → Repository → Model. Nenhuma camada acessa diretamente uma camada abaixo da sua vizinha imediata.

**controller/** recebe requisições HTTP, valida entrada superficialmente, delega ao service e retorna a resposta. Não contém lógica de negócio. Cada controller cobre um domínio: CalculoController (cálculo de emissões e exportação de relatório), CenarioController (cenários salvos), SimulacaoController (simulação de migração digital), TransactionController (transações).

**service/** concentra todas as regras de negócio e validações. É a única camada que pode combinar chamadas a múltiplos repositories. Destaques: CalculoEmissoesService calcula CO₂ por tipo de pagamento e período; SimulacaoService projeta cenários de migração digital; RelatorioExportacaoService orquestra a geração de PDF delegando para RelatorioPdfGeneratorImpl; CenarioService persiste e recupera cenários de simulação salvos pelo usuário.

**repository/** contém interfaces Spring Data JPA. Sem lógica — apenas contratos de acesso ao banco. EmissionFactorRepository, TransactionRepository, SavedScenarioRepository.

**model/** contém as entidades JPA mapeadas para o PostgreSQL: Transaction, EmissionFactor, SavedScenario. A subpasta enums/ define PaymentType (PIX, NFC, WALLET, QR_CODE, PHYSICAL, UNKNOWN) e Period (WEEKLY, MONTHLY, YEARLY).

**dto/** contém objetos de transferência usados nas bordas da API. Nunca são persistidos. Exemplos: CalculoRequest/CalculoResponse (entrada e saída do cálculo), ScoreDTO (score de sustentabilidade retornado ao frontend), SimulacaoRequest/SimulacaoResponse, SalvarCenarioRequest.

**exception/** define o tratamento global de erros: GlobalExceptionHandler captura exceções e retorna ErrorResponse padronizado; RelatorioGeracaoException é lançada quando a geração de PDF falha.

**config/** contém CorsConfig (libera o frontend React em localhost:5173) e DataInitializer (seed de dados iniciais — acessa repositories diretamente pois é inicialização de infraestrutura, não lógica de negócio).

O banco de dados é PostgreSQL rodando na porta 5432, banco `edenred`. As credenciais ficam em `src/main/resources/application.properties`, que não é versionado.

# Frontend

React 19 com Vite, roteamento via react-router-dom v7. Sobe em localhost:5173 e consome a API REST do backend em localhost:8080.

**App.jsx** define as rotas: `/login` (pública), `/dashboard`, `/simulador` e `/cenarios` (protegidas por ProtectedRoute dentro do Layout). Qualquer rota desconhecida redireciona para `/dashboard`.

**components/** cada componente tem sua própria subpasta com `.jsx` e `.css`. Componentes de página: Dashboard (painel principal com gráfico histórico, ScoreCard, ImpactCard e TransactionHistory), Simulador (simulação de migração digital com sliders e comparativo por tipo de pagamento), Cenarios (lista e comparação de cenários salvos). Componentes estruturais: Layout (sidebar + topbar, compartilhado entre todas as rotas protegidas), ProtectedRoute (redireciona para `/login` se não autenticado), Login (formulário de entrada).

**context/AuthContext.jsx** é o único estado global da aplicação. Armazena `empresa` (objeto com id e nome da empresa logada) e `token` JWT. Persiste no localStorage para sobreviver a refreshes. Expõe `login`, `logout` e `isAuthenticated`. Todos os componentes que precisam de identidade ou token consomem via `useAuth()`.

**services/** separa dois concerns: `auth.js` lida com login/logout e persistência do token no localStorage; `api.js` centraliza todas as chamadas HTTP ao backend, anexando o token JWT automaticamente via `authHeaders()`. Nenhum componente faz fetch diretamente — tudo passa por `api.js`.

**lib/sustainability.js** define os três níveis de sustentabilidade (Semente 0–33, Broto 34–66, Árvore 67–100) e a função `getLevel(score)`. É um módulo utilitário puro sem dependências de React, compartilhado entre Dashboard e Layout.
