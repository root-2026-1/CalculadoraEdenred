---
name: backend-architecture
description: Arquitetura em camadas do backend Java/Spring Boot — responsabilidades, fluxo de dependências e decisões de design
metadata:
  type: project
---

O backend segue arquitetura em camadas com Spring Boot 3.5 e Java 17. O fluxo de dependências é unidirecional: Controller → Service → Repository → Model. Nenhuma camada acessa diretamente uma camada abaixo da sua vizinha imediata.

**controller/** recebe requisições HTTP, valida entrada superficialmente, delega ao service e retorna a resposta. Não contém lógica de negócio. Cada controller cobre um domínio: CalculoController (cálculo de emissões e exportação de relatório), CenarioController (cenários salvos), SimulacaoController (simulação de migração digital), TransactionController (transações).

**service/** concentra todas as regras de negócio e validações. É a única camada que pode combinar chamadas a múltiplos repositories. Destaques: CalculoEmissoesService calcula CO₂ por tipo de pagamento e período; SimulacaoService projeta cenários de migração digital; RelatorioExportacaoService orquestra a geração de PDF delegando para RelatorioPdfGeneratorImpl; CenarioService persiste e recupera cenários de simulação salvos pelo usuário.

**repository/** contém interfaces Spring Data JPA. Sem lógica — apenas contratos de acesso ao banco. EmissionFactorRepository, TransactionRepository, SavedScenarioRepository.

**model/** contém as entidades JPA mapeadas para o PostgreSQL: Transaction, EmissionFactor, SavedScenario. A subpasta enums/ define PaymentType (PIX, NFC, WALLET, QR_CODE, PHYSICAL, UNKNOWN) e Period (WEEKLY, MONTHLY, YEARLY).

**dto/** contém objetos de transferência usados nas bordas da API. Nunca são persistidos. Exemplos: CalculoRequest/CalculoResponse (entrada e saída do cálculo), ScoreDTO (score de sustentabilidade retornado ao frontend), SimulacaoRequest/SimulacaoResponse, SalvarCenarioRequest.

**exception/** define o tratamento global de erros: GlobalExceptionHandler captura exceções e retorna ErrorResponse padronizado; RelatorioGeracaoException é lançada quando a geração de PDF falha.

**config/** contém CorsConfig (libera o frontend React em localhost:5173) e DataInitializer (seed de dados iniciais — acessa repositories diretamente pois é inicialização de infraestrutura, não lógica de negócio).

O banco de dados é PostgreSQL rodando na porta 5432, banco `edenred`. As credenciais ficam em `src/main/resources/application.properties`, que não é versionado.
