  (ns clojure-course-task03.core
    (:require [clojure.set]))

  (use 'clojure.string)

  (defn join* [table-name conds]
    (let [op (first conds)
          f1 (name (nth conds 1))
          f2 (name (nth conds 2))]
      (str table-name " ON " f1 " " op " " f2)))

  (defn where* [data]
    (let [ks (keys data)
          res (reduce str (doall (map #(let [src (get data %)
                                             v (if (string? src)
                                                 (str "'" src "'")
                                                 src)]
                                         (str (name %) " = " v ",")) ks)))]
      (reduce str (butlast res))))

  (defn order* [column ord]
    (str (name column)
         (if-not (nil? ord) (str " " (name ord)))))

  (defn limit* [v] v)

  (defn offset* [v] v)

  (defn -fields** [data]
    (reduce str (butlast (reduce str (doall (map #(str (name %) ",") data))))))

  (defn fields* [flds allowed]
    (let [v1 (apply hash-set flds)
          v2 (apply hash-set allowed)
          v (clojure.set/intersection v1 v2)]
      (cond
       (and (= (first flds) :all) (= (first allowed) :all)) "*"
       (and (= (first flds) :all) (not= (first allowed) :all)) (-fields** allowed)
       (= :all (first allowed)) (-fields** flds)
       :else (-fields** (filter v flds)))))

  (defn select* [table-name {:keys [fields where join order limit offset]}]
    (-> (str "SELECT " fields " FROM " table-name " ")
        (str (if-not (nil? where) (str " WHERE " where)))
        (str (if-not (nil? join) (str " JOIN " join)))
        (str (if-not (nil? order) (str " ORDER BY " order)))
        (str (if-not (nil? limit) (str " LIMIT " limit)))
        (str (if-not (nil? offset) (str " OFFSET " offset)))))


  (defmacro select [table-name & data]
    (let [;; Var containing allowed fields
          fields-var# (symbol (str table-name "-fields-var"))

          ;; The function takes one of the definitions like (where ...) or (join ...)
          ;; and returns a map item [:where (where* ...)] or [:join (join* ...)].
          transf (fn [elem]
                   (let [v (first elem)
                         v2 (second elem)
                         v3 (if (> (count elem) 2) (nth elem 2) nil)
                         val (case v
                                 fields (list 'fields* (vec (next elem)) fields-var#)
                                 offset (list 'offset* v2)
                                 limit (list 'limit* v2)
                                 order (list 'order* v2 v3)
                                 join (list 'join* (list 'quote v2) (list 'quote v3))
                                 where (list 'where* v2))]
                     [(keyword v) val]))

          ;; Takes a list of definitions like '((where ...) (join ...) ...) and returns
          ;; a vector [[:where (where* ...)] [:join (join* ...)] ...].
          env* (loop [d data
                      v (first d)
                      res []]
                 (if (empty? d)
                   res
                   (recur (next d) (second d) (conj res (transf v)))))

          ;; Accepts vector [[:where (where* ...)] [:join (join* ...)] ...],
          ;; returns map {:where (where* ...), :join (join* ...), ...}
          env# (apply hash-map (apply concat env*))]
      
      `(select* ~(str table-name)  ~env#)))


  ;; Examples:
  ;; -------------------------------------

  (let [proposal-fields-var [:person, :phone, :address, :price]]
    (select proposal
            (fields :person, :phone, :id)
            (where {:price 11})
            (join agents (= agents.proposal_id proposal.id))
            (order :f3)
            (limit 5)
            (offset 5)))

  (let [proposal-fields-var [:person, :phone, :address, :price]]
    (select proposal
            (fields :all)
            (where {:price 11})
            (join agents (= agents.proposal_id proposal.id))
            (order :f3)
            (limit 5)
            (offset 5)))

  (let [proposal-fields-var [:all]]
    (select proposal
            (fields :all)
            (where {:price 11})
            (join agents (= agents.proposal_id proposal.id))
            (order :f3)
            (limit 5)
            (offset 5)))


  (comment
    ;; Описание и примеры использования DSL
    ;; ------------------------------------
    ;; Предметная область -- разграничение прав доступа на таблицы в реелтерском агенстве
    ;;
    ;; Работают три типа сотрудников: директор (имеет доступ ко всему), операторы ПК (принимают заказы, отвечают на тел. звонки,
    ;; передают агенту инфу о клиентах), агенты (люди, которые лично встречаются с клиентами).
    ;;
    ;; Таблицы:
    ;; proposal -> [id, person, phone, address, region, comments, price]
    ;; clients -> [id, person, phone, region, comments, price_from, price_to]
    ;; agents -> [proposal_id, agent, done]

    ;; Определяем группы пользователей и
    ;; их права на таблицы и колонки
    (group Agent
           proposal -> [person, phone, address, price]
           agents -> [clients_id, proposal_id, agent])

    ;; Предыдущий макрос создает эти функции
    (select-agent-proposal) ;; select person, phone, address, price from proposal;
    (select-agent-agents)  ;; select clients_id, proposal_id, agent from agents;

    (group Operator
           proposal -> [:all]
           clients -> [:all])

    ;; Предыдущий макрос создает эти функции
    (select-operator-proposal) ;; select * proposal;
    (select-operator-clients)  ;; select * from clients;



    (group Director
           proposal -> [:all]
           clients -> [:all]
           agents -> [:all])

    ;; Предыдущий макрос создает эти функции
    (select-director-proposal) ;; select * proposal;
    (select-director-clients)  ;; select * from clients;
    (select-director-agents)  ;; select * from agents;
    

    ;; Определяем пользователей и их группы

    (user Ivanov
          (belongs-to Agent))

    (user Sidorov
          (belongs-to Agent))

    (user Petrov
          (belongs-to Operator))

    (user Directorov
          (belongs-to Operator,
                      Agent,
                      Director))


    ;; Оператор select использует внутри себя переменную <table-name>-fields-var.
    ;; Для указанного юзера макрос with-user должен определять переменную <table-name>-fields-var
    ;; для каждой таблицы, которая должна содержать список допустимых полей этой таблицы
    ;; для этого пользователя.

    ;; Агенту можно видеть свои "предложения"
    (with-user Ivanov
      (select proposal
              (fields :person, :phone, :address, :price)
              (join agents (= agents.proposal_id proposal.id))))

    ;; Агенту не доступны клиенты
    (with-user Ivanov
      (select clients
              (fields :all)))  ;; Empty set

    ;; Директор может видеть состояние задач агентов
    (with-user Directorov
      (select agents
              (fields :done)
              (where {:agent "Ivanov"})
              (order :done :ASC)))
    
    )

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; TBD: Implement the following macros
  ;;

(defn get-vars [pattern]
  "Return all defined variable in namespace by pattern"
  (filter (fn [variable]
   (if (re-matches (re-pattern (str pattern ".+")) (str variable))
      true)) (keys (ns-publics *ns*))))

(defn- get-user-var [variable group user-name]
  (replace-first (str variable) group (str user-name "-")))

(defmacro group
  "define variables: <group>-<table>-fields-var and select-<group>-<table>"
  ([group-name] nil)
  ([group-name table arrow fields & body]
     `(let [fields# ~(mapv keyword fields)]
        (def ~(symbol (str group-name "-" table "-fields-var")) fields#)
        (defn ~(symbol (lower-case (str "select-" group-name "-" table))) []
          (let [~(symbol (str table "-fields-var")) fields#]
            (select ~table (~'fields :all))))
        (group ~group-name ~@body))))

(defmacro user [user-name [belongs & groups]]
  "define variables: <user>-<table>-fields-var"
  `(do ~@(for [group groups :let [group (str group "-")]
             variable (get-vars group)]
         `(def ~(symbol (get-user-var variable group user-name)) ~variable))))

(defmacro with-user [user-name [sql-command table :as body]]
  `(let [~(symbol (str table "-fields-var")) ~(symbol (str user-name "-" table "-fields-var"))]
     ~body))
