## Роуты

**Auth** (`/api/auth`)
- `POST /register` — регистрация (`{username, password}`)
- `POST /login` — логин, выдает JWT

**Accounts** (`/api/accounts`)
- `POST` — создать счет
- `GET` — список моих счетов

**Transfers** (`/api/transfers`)
- `POST` — перевод (`{fromAccountId, toAccountId, amount}`)

**Transactions** (`/api/transactions`)
- `GET` — история (параметры `page`, `size`)
