-----------------------------------------------------------
 1. REQUISITOS DE SISTEMA
-----------------------------------------------------------
- Java Development Kit (JDK) 17 ou superior.
- MySQL Server 8.0 rodando na porta padrão (3306).
- Maven (para gerenciamento de dependências).

-----------------------------------------------------------
 2. CONFIGURAÇÃO DO BANCO DE DADOS
-----------------------------------------------------------
O projeto já inclui o script completo para criação da estrutura.

1. Abra seu cliente MySQL (Workbench, DBeaver ou Terminal).
2. Localize o arquivo "tabelas.sql" na raiz deste projeto.
3. Execute todo o conteúdo do arquivo para criar o banco 
   'gestao_financeira_db' e suas tabelas.
4. Verifique a conexão no código:
   - Abra: src/main/java/com/gestaofinanceira/util/ConnectionFactory.java
   - Confirme se USER e PASSWORD correspondem ao seu MySQL local.

-----------------------------------------------------------
 3. COMO EXECUTAR
-----------------------------------------------------------
1. Abra o projeto na sua IDE de preferência (VS Code, Eclipse, IntelliJ).
2. Aguarde o Maven baixar todas as dependências listadas no pom.xml.
3. Localize a classe principal (onde está o método main da Interface Gráfica) 
   e execute.
   
*Nota: O sistema inicia automaticamente uma Thread em segundo plano 
para monitoramento de saldos negativos e prazos de metas.*

-----------------------------------------------------------
 4. FUNCIONALIDADES IMPLEMENTADAS
-----------------------------------------------------------
O sistema atende aos seguintes requisitos do Trabalho Final:

[x] Interface Gráfica (Swing/JFrame).
[x] Banco de Dados (MySQL com JDBC e DAO).
[x] Arquitetura em Camadas (Model, View, Controller/Service, DAO).
[x] Generics (Implementado na interface GenericDAO).
[x] Tratamento de Exceções (Ex: SaldoInsuficienteException).
[x] Manipulação de Arquivos (Exportação PDF e Excel XLS).
[x] Programação Concorrente (MonitoramentoService com Runnable).
[x] CRUDs Completos (Usuário, Conta, Despesa, Receita, Metas).
[x] Regras de Negócio (Cálculo de aporte de metas, trava de saldo, transferências).

-----------------------------------------------------------
 5. DEPENDÊNCIAS UTILIZADAS
-----------------------------------------------------------
- mysql-connector-j (Conexão Banco)
- jbcrypt (Segurança de Senha)
- itextpdf (Relatórios PDF)
- poi (Relatórios Excel)