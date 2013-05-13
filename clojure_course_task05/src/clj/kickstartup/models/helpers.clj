(ns hire-doer.models.helpers)

(def ^:dynamic *date-format* (java.text.SimpleDateFormat. "dd-MM-yyyy hh:ss"))

(defn format-date [date]
  (.format *date-format* date))

(defn parse-date [str-date]
  (.parse *date-format* str-date))
