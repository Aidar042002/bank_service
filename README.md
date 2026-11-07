## Роуты

**Auth** (`/api/auth`)
- `POST /register` — регистрация (`{username, password}`)
- `POST /login` — логин, выдает JWT

**Accounts** (`/api/accounts`) — *нужен JWT*
- `POST /` — создать счет
- `GET /` — список моих счетов

**Transfers** (`/api/transfers`) — *нужен JWT*
- `POST /` — перевод (`{fromAccountId, toAccountId, amount}`)

**Transactions** (`/api/transactions`) — *нужен JWT*
- `GET /` — история (параметры `page`, `size`)
