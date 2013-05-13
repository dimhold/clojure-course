(ns clojure-course-task02.core
  (:gen-class))

(use 'clojure.java.io)

;;;; Некоторые размышления о программе "поиск файлов"

;; Я сильно сомневаюсь, что программа на clojure может превзойти по скорости утилиту find.
;; 1) У find есть бесчисленное множество оптимизационных хитростей, которые ускоряют, но и усложняют код.
;;      Например, если в директории 2 файла - директория "пустая", пропускаем ее (2 файла: [".", ".."])
;; 2) Не гоже в clojure работать с массивами, которые могут удачно "помещаться" в кэш процессора + указатели и т.д.
;; 3) Не равные условия в области регулярных выражений: find может только: ('*',  '?',  and '[]')


;; Возможные проблемы:
;; 1) Динамически меняющиеся файловые системы. Например /proc директория.
;; 2) POSIXие символические ссылки могут привести к зацикливанию программы.
;; 3) Отмонтированная файловая система, во время работы программы.


;; За счет чего можно попытаться ускорится:
;; 1) Распараллеливать обработку файлов в списке (=директории).
;;      Годится. Будем использовать pmap.
;; 2) Отказаться от регулярных выражений. Либо же применять регулярные выражения, основанные на ДКА.
;;    Java иcпользует классические регулярные выражения на НКА. Использовать что-то в духе re2 библиотеки?
;;    Лишнии зависимости и усложнения. Не годится.
;; 3) Пытаться опустить работу с файлами "поближе" к ос.
;;    Возможно jvm умеет оптимизировать код, вроде этого?
;;          (def file-filter (proxy [FilenameFilter] []
;;                  (accept [dir name]
;;                       ; REGEXP here)))
;;    Или даже лучше использовать FileVisitor из nio.
;;    Сомнительно. Не годится.
;; 4) Распараллелить рекурсивный обход директорий. Использовать агенты? (Или акторы из akka?)
;;      На мой взгяд, в реальных системах, глубина вложенности всегда стремится к 0. 
;;      Не могу придумать пример, когда в системе использовался бы "дикий" уровень вложенности.
;;      Остается вариант с просто большим кол-вом директорий.
;;    Годится. Имеет смысл распараллеть чтение директорий.

(defn file? [file]
  (. file isFile))

(defn get-files [dir]
  (pmap #(.getName %) (filter file? (file-seq (file dir)))))

(defn matches? [regexp arg]
  (if (re-matches (re-pattern regexp ) arg)
    true)) 

(defn find-files [file-name path]
  (filter (partial matches? file-name) (get-files path)))

(defn usage []
  (println "Usage: $ run.sh file_name path"))

(defn -main [file-name path]
  (if (or (nil? file-name)
          (nil? path))
    (usage)
    (do
      (println "Searching for " file-name " in " path "...")
      (dorun (map println (find-files file-name path))))))
