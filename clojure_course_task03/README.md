# clojure_course_task03

В файле находятся два DSL-а: один для формирования SQL-запроса select (реализован), второй -- для описания прав доступа пользователей к различным таблицам и колонкам (необходимо реализовать в рамках домашнего задания).

## Описание DSL для формирования select-запросов

Первые 80 строк файла реализуют запрос select. Пример использоавния.

    (let [proposal-fields-var [:all]]
      (select proposal
          (fields :all)
          (where {:price 11})
          (join agents (= agents.proposal_id proposal.id))
          (order :f3)
          (limit 5)
          (offset 5)))

Результат выполнения оператора -- следующий SQL-запрос.

"SELECT * FROM proposal  WHERE price = 11 JOIN agents ON agents.proposal_id = proposal.id ORDER BY f3 LIMIT 5 OFFSET 5"

Запрос select внутри себя ожидает лексическую переменную **table**-fields-var, где **table** -- имя таблицы. Поскольку в примере выше мы делаем запрос из таблицы proposal, то для корректной работы select-a необходимо, чтобы в области его видимости была переменная proposal-fields-var.

Если выполнить запрос без объявления этой переменной, то компилятор Clojure выдаст ошибку:
CompilerException java.lang.RuntimeException: Unable to resolve symbol: proposal-fields-var in this context

В переменной **table**-fields-var необходимо хранить вектор ключевых слов, которые соответствуют разрешенным столбцам таблицы.

Например, если таблица myusers содержит столбцы login, password, email, то в переменной myusers-fields-var должен быть вектор [:login, :password, :email]. Перечисленные в векторе столбцы разрешены для вывода оператором select.

Если необходимо запретить выводить некоторые столбцы таблицы, то можно перечислить только разрешенные. Например, если в переменной myusers-fields-var записать [:login, :password], то оператор select вернет записи только из двух значений -- колонок login и password.

Если необходимо разрешить выводить все колонки, то можно вместо перечисления этих колонок написать ключевое слово :all.

Оператор select автоматически находит пересечение множеств запрашиваемых и разрешенных колонок. Например:

    (let [proposal-fields-var [:person, :phone, :address, :price]]
      (select proposal
          (fields :person, :phone, :id)
          (where {:price 11})
          (join agents (= agents.proposal_id proposal.id))
          (order :f3)
          (limit 5)
          (offset 5)))

Результатом работы оператора select будет следующий запрос:
	  
"SELECT phone,person FROM proposal  WHERE price = 11 JOIN agents ON agents.proposal_id = proposal.id ORDER BY f3 LIMIT 5 OFFSET 5"

## Описание DSL для разграничения прав доступа

Вам предлагается реализовать DSL, с помощью которого можно описывать права на колонки и таблицы группы пользователей, а также причислять пользователей к одной или нескольким группам. Например:

    (group Agent
         proposal -> [person, phone, address, price]
         agents -> [client_id, proposal_id, agent])

Здесь описывается группа Agent, в которой разрешен доступ к таблицам proposal и agents, причем только к перечисленным колонкам.	 

Макрос group должен "запомнить" в какой-то переменной, какие таблицы и колонки разрешены для группы Agent. Кроме того, он должен создать две функции для запроса всех разрешенных колонок из таблиц: select-agent-proposal и select-agent-agents.

Описание пользователя выглядит так:

    (user Ivanov
        (belongs-to Agent))

Макрос user создает переменные Ivanov-proposal-fields-var и Ivanov-agents-fields-var, в которых перечислены колонки, разрешенные данному пользователю.


Запросы select предполагается использовать следующим образом:

    (with-user Ivanov
      (select proposal
            (fields :person, :phone)
            (join agents (= agents.proposal_id proposal.id))))

Макрос with-user должен найти подходящую переменную (в данном случае -- Ivanov-proposal-fields-var) и создать новую локальную переменную proposal-fields-var с тем же содержимым. Тогда select сможет выполниться.

Такое использование оператора select позволяет программисту не заботиться о безопасности и различных проверках разрешений на колонки, поскольку этим будет заниматься макрос with-user.

Таким образом, вам предлагается реализовать три макроса: group, user и with-user.

Больше примеров смотрите в core.clj.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
