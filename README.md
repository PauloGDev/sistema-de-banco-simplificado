Um sistema simplificado de transações. 

Tipos de usuário: COMMON e MERCHANT.
Apenas usuários comuns podem realizar envios, mercadores apenas recebem dinheiro.

Os usuários são validados por CPF e E-mail, logo, não podem existir dados duplicados.

Exemplo de Json para implementar usuários:
 {
    "id": 1,
    "firstName": "Carlos",
    "lastName": "Silva",
    "document": "433.345.143-42",
    "email": "Carlos@email.com",
    "password": "12345678",
    "balance": 30.00,
    "userType": "COMMON"
  },
