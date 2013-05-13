(ns kickstartup.db)

(def default-conn {:classname "com.mysql.jdbc.Driver"
                   :subprotocol "mysql"
                   :user "USR"
                   :password "PSW"
                   :subname "//127.0.0.1:3306/db?useUnicode=true&characterEncoding=utf8"
                   :delimiters "`"})

; (def env (into {} (System/getenv)))

; (def dbhost (get env "OPENSHIFT_MYSQL_DB_HOST"))
; (def dbport (get env "OPENSHIFT_MYSQL_DB_PORT"))

; (def default-conn {:classname "com.mysql.jdbc.Driver"
; :subprotocol "mysql"
; :user "USR"
; :password "PSW"
; :subname (str "//" dbhost ":" dbport "/db?useUnicode=true&characterEncoding=utf8")
; :delimiters "`"})
