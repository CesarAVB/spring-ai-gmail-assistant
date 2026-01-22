# 📧 Gmail AI Assistant

Microserviço especializado em operações do Gmail usando IA (Google Gemini).

## 🎯 Funcionalidades

- 📬 Listar emails
- ✉️ Enviar emails
- 🔍 Buscar emails
- ✅ Marcar como lido
- 🗑️ Deletar emails
- 📊 Análise de caixa de entrada

## 🚀 Quick Start
```bash
# Configurar variáveis
export GEMINI_API_KEY=sua-chave

# Adicionar credentials.json do Gmail API
cp credentials.json src/main/resources/

# Rodar
mvn spring-boot:run

# Acesso
http://localhost:8082
http://localhost:8082/swagger-ui.html
```

## 📡 Endpoints

- `POST /api/v1/gmail/chat` - Chat com assistente
- `GET /api/v1/gmail/emails` - Listar emails
- `GET /api/v1/gmail/health` - Health check

## 🔧 Tecnologias

- Java 21
- Spring Boot 3.2.5
- LangChain4j 1.7.1
- Google Gemini AI
- Gmail API

## 📝 Exemplo de Uso
```bash
curl -X POST http://localhost:8082/api/v1/gmail/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Liste meus últimos 5 emails"}'
```