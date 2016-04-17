(deftemplate product 
    (slot id)
    (slot name)
    (slot type)
    (slot price))

(deftemplate order
    (slot product-id)
    (slot quantity ))

(defrule create-order-for-product
    (product (id ?id) (type ?t&veg))
    =>
    (assert (order (product-id ?id) (quantity 1))))


(deftemplate sensorInfo
    (slot sensorId)
    (slot temperature)
    (slot needsHeating
    (type SYMBOL)
    (default no)))

(defrule set-is-heating
?s <- (sensorInfo (sensorId ?sensorId) (temperature ?temperature&:( or (< ?temperature 10 ) (> ?temperature 30) )))
=>
(assert (change (sensorId ?sensorId))
(retract ?s)))

(facts)

